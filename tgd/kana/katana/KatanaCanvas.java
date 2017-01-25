package com.tgd.kana.katana;

import com.tgd.kana.Kana;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;

import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class KatanaCanvas extends Canvas implements Runnable
	{
	private Kana kana;
	private Text text;
	
	private Random random;
	
	private Thread animator;
	private Graphics2D g2d;
	private Image image;
	private KatanaImageLoader katanaImageLoader;
	
	private KatanaAudioLoader katanaAudioLoader;
	
	private int counter;
	private int numberOfCorrectAnswer;
	
	private BufferedImage characterImage;
	private String character;
	private int characterIndex;
	private ArrayList<Integer> characterIndexes;
	
	public static int TOTAL_NUMBER_OF_CHARACTERS;
	
	public static final int PWIDTH = 298;
	public static final int PHEIGHT = 298;
	
	private JFrame frame;
	
	protected long period;
	
	private static Integer lastKey = null;
	private static boolean repressKey = true;
	private boolean shiftDown;
	
	private static final int NO_DELAYS_PER_YIELD = 16;
	private static final int NUM_FPS = 10;
	private static final int MAX_FRAME_SKIPS = 5;
	private static final long MAX_STATS_INTERVAL = 1000000000L;
	
	private static long katanaStartTime;
	
	private long hiraganaEventStartTime;
	private int  timeSpentInGame = 0; // in seconds
	private long prevStatsTime;
	private long totalElapsedTime = 0L;
	private long totalFramesSkipped = 0L;
	private long framesSkipped = 0L;
	
	private long preFrameTime;
	private long postFrameTime;
	private long timeDifference;
	private long sleepTime;
	
	private long overSleepTime = 0L;
	
	private long excess = 0L;
	
	private int noDelay = 0;
	
	private volatile boolean running;
	private volatile boolean displayResults;
	
	public KatanaCanvas(Kana kana, long period, int totalCharacters)
		{
		this.period = period;
		this.TOTAL_NUMBER_OF_CHARACTERS = totalCharacters;
		
		if(kana != null)
			{
			initHiraganaPanel(kana);
			}
			
		text = new Text();
		}
		
	private void initHiraganaPanel(Kana kana)
		{
		//setDoubleBuffered(true);
		setBackground(Color.black);
		
		this.kana = kana;
		
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		
		katanaImageLoader = new KatanaImageLoader();
		
		
		random = new Random();
		
		characterIndexes = new ArrayList<>();
		
		for(int i=0; i<katanaImageLoader.getNumberOfCharacters(); i++)
			characterIndexes.add(i);
			
		for(int i=0; i<katanaImageLoader.getNumberOfCharacters(); i++)
			{
			int randomIndex = random.nextInt(characterIndexes.size());
			int randomValue = characterIndexes.get(randomIndex);
			int nextRandomIndex = random.nextInt(characterIndexes.size());
			
			characterIndexes.set(randomIndex, characterIndexes.get(nextRandomIndex));
			characterIndexes.set(nextRandomIndex, randomValue);
			}
			
		int randomIndex = random.nextInt(characterIndexes.size());	
		
		characterImage = katanaImageLoader.getCharacters().get(characterIndexes.get(randomIndex)).getImage();
		character = katanaImageLoader.getCharacters().get(characterIndexes.get(randomIndex)).getCharacter();
		characterIndex = katanaImageLoader.getCharacters().get(characterIndexes.get(randomIndex)).getIndex();
		
		System.out.println("Audio Character: " + character);
		
		katanaAudioLoader = new KatanaAudioLoader(character);
		
		characterIndexes.remove(randomIndex);
		//background = new Background();
		
		//addControls();
		
		setFocusable(true);
		requestFocus();
		
		addInputs();
		}
		
	public void initKatanaSubPanel(Kana kana, JFrame frame)
		{
		KatanaSubCanvas katanaSubCanvas = new KatanaSubCanvas(this, period);
		katanaSubCanvas.add(katanaAudioLoader);
		
		this.frame = new JFrame();
		this.frame.setResizable(false);
		this.frame.setTitle("Katana Input");
		this.frame.add(katanaSubCanvas);
		this.frame.pack();
		
		this.frame.setLocation(frame.getX() + frame.getWidth() / 2 - this.frame.getWidth() / 2, frame.getY() + frame.getHeight());
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
		}
		
	public void nextCharacter()
		{
		if(characterIndexes.size() == 0)
			{
			for(int i=0; i<katanaImageLoader.getNumberOfCharacters(); i++)
				characterIndexes.add(i);
			
			for(int i=0; i<katanaImageLoader.getNumberOfCharacters(); i++)
				{
				int randomIndex = random.nextInt(characterIndexes.size());
				int randomValue = characterIndexes.get(randomIndex);
				int nextRandomIndex = random.nextInt(characterIndexes.size());
				
				characterIndexes.set(randomIndex, characterIndexes.get(nextRandomIndex));
				characterIndexes.set(nextRandomIndex, randomValue);
				}
			}
		
		int randomIndex = random.nextInt(characterIndexes.size());	
		
		characterImage = katanaImageLoader.getCharacters().get(characterIndexes.get(randomIndex)).getImage();
		character = katanaImageLoader.getCharacters().get(characterIndexes.get(randomIndex)).getCharacter();
		characterIndex = katanaImageLoader.getCharacters().get(characterIndexes.get(randomIndex)).getIndex();
		
		characterIndexes.remove(randomIndex);
		
		katanaAudioLoader.setNewMedia(character);
		}
		
	private void initGraphics2D()
		{
		/*g2d = renderingStrategy.getGraphics2D();
		
		if(g2d != null)
			this.g2d = g2d;
		else
			System.err.println("Error, g2d is null");*/
		}
		
	public void addNotify()
		{
		System.out.println("Running");
		super.addNotify();
		startThread();
		}
		
	public void startThread()
		{
		if(animator == null && !running)
			{
			animator = new Thread(this, "Game Thread");
			animator.start();
			}
		}
		
	public void run()
		{
		running = true;
		hiraganaEventStartTime = System.nanoTime(); // this game event is starting now
		preFrameTime = hiraganaEventStartTime;
		
		while(running)
			{
			update();
			render();
			//repaint();
			sleep();
			}
		}
		
	private void update()
		{
		if(counter == TOTAL_NUMBER_OF_CHARACTERS)
			displayResults = true;
		}
		
	public void render()
		{
		BufferStrategy bufferStrategy = getBufferStrategy();
		
		if(bufferStrategy == null)
			{
			createBufferStrategy(3);
			return;
			}
			
		g2d = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		draw();
		
		g2d.dispose();
		
		bufferStrategy.show();
		}
		
	public void draw()
		{
		if(displayResults)
			{
			drawResults();
			}
		else
			{
			drawCharacter();
			}
		}
		
	private void drawCharacter()
		{
		g2d.setColor(Color.black);
		g2d.fillRect(0,0,PWIDTH,PHEIGHT);
		
		g2d.drawImage(characterImage, 0, 0, null);
		g2d.drawString(numberOfCorrectAnswer + "/" + counter + "/" + katanaImageLoader.getNumberOfCharacters(), 245, 280);
		
		//g2d.fillRect(mouse.getX() - 2, mouse.getY() - 2, 4, 4);
		//g2d.drawString("Button: " + mouse.getButton(), PWIDTH / 60, (PHEIGHT / 20));

		g2d.setColor(Color.black);
		}
		
	private void drawResults()
		{
		g2d.setColor(Color.white);
		g2d.fillRect(0,0,PWIDTH,PHEIGHT);
		
		g2d.setColor(Color.black);
		g2d.drawString("Number of Correct Answers: " + numberOfCorrectAnswer, 50, 100);
		g2d.drawString("Total Number of Character: " + TOTAL_NUMBER_OF_CHARACTERS, 50, 130);
		
		g2d.setColor(Color.black);
		}
		
	public void paintComponent(Graphics g)
		{
		//super.paintComponent(g);
		System.out.println("paintComponent");
		try
			{
			if((image != null) && (g != null))
				g.drawImage(image,0,0,null);
				
			Toolkit.getDefaultToolkit().sync();
			
			g.dispose();
			}
		catch(Exception e)
			{
			System.out.println("Graphics context error " + e);
			}
		}
		
	private void sleep()
		{
		postFrameTime = System.nanoTime();
		timeDifference = postFrameTime - preFrameTime;
		sleepTime = (period - timeDifference) - overSleepTime;
		
		if(sleepTime > 0)
			{
			try
				{
				Thread.sleep(sleepTime/1000000L);
				}
			catch(InterruptedException ex)
				{
				System.err.println("An Interrupt Exception has occured for this event");
				}
			
			overSleepTime = (System.nanoTime() - postFrameTime) - sleepTime;	
			}
		else
			{
			excess = excess - sleepTime;
			overSleepTime = 0L;
			
			if(++noDelay >= NO_DELAYS_PER_YIELD)
				{
				Thread.yield();
				noDelay = 0;
				}
			}
				
		preFrameTime = System.nanoTime();
			
		int skips = 0;
			
		while((excess > period) && (skips < MAX_FRAME_SKIPS))
			{
			excess = excess - period;
			update();
			skips++;
			}
				
		framesSkipped = framesSkipped + skips;					
		}
		
	protected void addInputs()
		{
		shiftDown = false;
		
		addKeyListener(new KeyAdapter()
			{
			public void keyPressed(KeyEvent e)
				{
				processKeys(e);
				}
			});
			
		addKeyListener(new KeyAdapter()
			{
			public void keyReleased(KeyEvent e)
				{
				processKeyReleased(e);
				}
			});
			
		/*Mouse mouse = Mouse.getInstance();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);*/
		}
		
	private void processKeys(KeyEvent e)
		{
		int keyPressed = e.getKeyCode();
		
		switch(keyPressed)
			{
			case KeyEvent.VK_SHIFT: shiftDown = true;
				break;
			case KeyEvent.VK_MINUS: text.add('-');
				break;
			case KeyEvent.VK_BACK_SLASH: text.add('\\');
				break;
			case KeyEvent.VK_EQUALS: text.add(shiftDown ? '+' : '=');
				break;
			case KeyEvent.VK_BRACELEFT: text.add('[');
				break;
			case KeyEvent.VK_BRACERIGHT: text.add(']');
				break;
			case KeyEvent.VK_SEMICOLON: text.add(';');
				break;
			case KeyEvent.VK_COMMA: text.add(',');
				break;
			case KeyEvent.VK_PERIOD: text.add('.');
				break;
			case KeyEvent.VK_SLASH: text.add('/');
				break;
			case KeyEvent.VK_1: text.add('1');
				break;
			case KeyEvent.VK_2: text.add('2');
				break;
			case KeyEvent.VK_3: text.add('3');
				break;
			case KeyEvent.VK_4: text.add('4');
				break;
			case KeyEvent.VK_5: text.add('5');
				break;
			case KeyEvent.VK_6: text.add('6');
				break;
			case KeyEvent.VK_7: text.add('7');
				break;
			case KeyEvent.VK_8: text.add('8');
				break;
			case KeyEvent.VK_9: text.add('9');
				break;
			case KeyEvent.VK_0: text.add('0');
				break;
			case KeyEvent.VK_Q: text.add('q');
				break;
			case KeyEvent.VK_W: text.add('w');
				break;
			case KeyEvent.VK_E: text.add('e');
				break;
			case KeyEvent.VK_R: text.add('r');
				break;
			case KeyEvent.VK_T: text.add('t');
				break;
			case KeyEvent.VK_Y: text.add('y');
				break;
			case KeyEvent.VK_U: text.add('u');
				break;
			case KeyEvent.VK_I: text.add('i');
				break;
			case KeyEvent.VK_O: text.add('o');
				break;
			case KeyEvent.VK_P: text.add('p');
				break;
			case KeyEvent.VK_A: text.add('a');
				break;
			case KeyEvent.VK_S: text.add('s');
				break;
			case KeyEvent.VK_D: text.add('d');
				break;
			case KeyEvent.VK_F: text.add('f');
				break;
			case KeyEvent.VK_G: text.add('g');
				break;
			case KeyEvent.VK_H: text.add('h');
				break;
			case KeyEvent.VK_J: text.add('j');
				break;
			case KeyEvent.VK_K: text.add('k');
				break;
			case KeyEvent.VK_L: text.add('l');
				break;
			case KeyEvent.VK_Z: text.add('z');
				break;
			case KeyEvent.VK_X: text.add('x');
				break;
			case KeyEvent.VK_C: text.add('c');
				break;
			case KeyEvent.VK_V: text.add('v');
				break;
			case KeyEvent.VK_B: text.add('b');
				break;
			case KeyEvent.VK_N: text.add('n');
				break;
			case KeyEvent.VK_M: text.add('m');
				break;
			case KeyEvent.VK_SPACE : text.add(' ');
				break;
			case KeyEvent.VK_BACK_SPACE : text.remove();
				break;
			case KeyEvent.VK_ENTER : text.newLineOrNewCommand();
				break;
			}
		}
		
	private void processKeyReleased(KeyEvent e)
		{
		int keyReleased = e.getKeyCode();
		
		switch(keyReleased)
			{
			case KeyEvent.VK_SHIFT: shiftDown = false;
				break;
			}
		}
		
	public KatanaImageLoader getKatanaImageLoader()
		{
		return katanaImageLoader;
		}
		
	public int getCharacterIndex()
		{
		return characterIndex;
		}
		
	public String getCharacter()
		{
		return character;
		}
		
	public void setNumberOfCorrectAnswer(int numberOfCorrectAnswer)
		{
		this.numberOfCorrectAnswer = numberOfCorrectAnswer;
		}
		
	public void setCounter(int counter)
		{
		this.counter = counter;
		}
	}