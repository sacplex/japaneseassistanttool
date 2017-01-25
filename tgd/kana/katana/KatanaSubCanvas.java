package com.tgd.kana.katana;

import com.tgd.kana.input.Mouse;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class KatanaSubCanvas extends Canvas implements Runnable
	{
	public static final int PWIDTH = 100;
	public static final int PHEIGHT = 100;
	
	private KatanaCanvas katanaCanvas;
	
	private Random random;
	
	private int numberOfOptions = 3;
	private int selectedOption;
	
	private ArrayList<KatanaCharacter> iconOptions;
	private ArrayList<Rectangle> collisionBoxes;
	
	private KatanaIconLoader katanaIconLoader;
	private KatanaAudioLoader katanaAudioLoader;
	
	private ArrayList<Integer> characterIndexes;
	private int counter;
	private int correctOption;
	private int numberOfCorrectAnswer;
	
	private Thread animator;
	private Graphics2D g2d;
	private Image image;
	
	private Mouse mouse;
	
	private long period;
		
	private static final int NO_DELAYS_PER_YIELD = 16;
	private static final int NUM_FPS = 10;
	private static final int MAX_FRAME_SKIPS = 5;
	private static final long MAX_STATS_INTERVAL = 1000000000L;
	
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
	
	public KatanaSubCanvas(KatanaCanvas katanaCanvas, long period)
		{
		this.katanaCanvas = katanaCanvas;
		
		setPreferredSize(new Dimension(PWIDTH * numberOfOptions, PHEIGHT));
		
		iconOptions = new ArrayList<>(numberOfOptions);
		
		initKatanaIconLoader();
		
		addInputs();
		}
		
	private void initKatanaIconLoader()
		{
		katanaIconLoader = new KatanaIconLoader();
			
		characterIndexes = new ArrayList<>();
		
		for(int i=0; i<katanaCanvas.getKatanaImageLoader().getNumberOfCharacters(); i++)
			characterIndexes.add(i);
		
		random = new Random();
		
		for(int i=0; i<katanaCanvas.getKatanaImageLoader().getNumberOfCharacters()-1; i++)
			{
			int randomIndex = random.nextInt(katanaCanvas.getKatanaImageLoader().getNumberOfCharacters()-1);
			int randomValue = characterIndexes.get(randomIndex);
			int nextRandomIndex = random.nextInt(characterIndexes.size());
			
			characterIndexes.set(randomIndex, characterIndexes.get(nextRandomIndex));
			characterIndexes.set(nextRandomIndex, randomValue);
			}
			
		for(int i=0; i<numberOfOptions; i++)
			{
			iconOptions.add(katanaIconLoader.getCharacters().get(characterIndexes.get(i)));
			}
			
		boolean removeAndReplace = true;
			
		for(int i=0; i<numberOfOptions; i++)
			{
			if(katanaCanvas.getCharacter().equals(iconOptions.get(i).getCharacter()))
				{
				removeAndReplace = false;
				break;
				}
			}
			
		if(removeAndReplace)
			{		
			correctOption = random.nextInt(numberOfOptions);
		
			iconOptions.remove(correctOption);
			
			iconOptions.add(correctOption, katanaIconLoader.getCharacters().get(katanaCanvas.getCharacterIndex()));
			}
			
		collisionBoxes = new ArrayList<>();
		
		for(int i=0; i<numberOfOptions; i++)
			{
			collisionBoxes.add(new Rectangle((i*100),0,PWIDTH,PHEIGHT));
			}
		}
		
	public void addNotify()
		{
		System.out.println("Running");
		super.addNotify();
		startThread();
		}
		
	public void add(KatanaAudioLoader katanaAudioLoader)
		{
		this.katanaAudioLoader = katanaAudioLoader;
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
		if(mouse.getState() == Mouse.MOUSE_STATE.PRESSED)
			{
			System.out.println("Click");
			
			for(int i=0; i<collisionBoxes.size(); i++)
				{
				if(collisionBoxes.get(i).contains(mouse.getX(), mouse.getY()))
					{
					counter++;
					selectedOption = i;
					
					checkAnswer(i);
					
					break;
					}
				}
			
			mouse.reset();
			}
		}
		
	private void checkAnswer(int selectedOption)
		{
		if(iconOptions.get(selectedOption).getCharacter().equals(katanaCanvas.getCharacter()))
			{
			System.out.println("Correct");
			katanaAudioLoader.play();
			numberOfCorrectAnswer++;
			}
		else
			{
			System.out.println("Incorrect");
			}
		
		katanaCanvas.setNumberOfCorrectAnswer(numberOfCorrectAnswer);
		katanaCanvas.setCounter(counter);	
		katanaCanvas.nextCharacter();
		
		for(int i=0; i<katanaCanvas.getKatanaImageLoader().getNumberOfCharacters()-1; i++)
			{
			int randomIndex = random.nextInt(katanaCanvas.getKatanaImageLoader().getNumberOfCharacters()-1);
			int randomValue = characterIndexes.get(randomIndex);
			int nextRandomIndex = random.nextInt(characterIndexes.size());
			
			characterIndexes.set(randomIndex, characterIndexes.get(nextRandomIndex));
			characterIndexes.set(nextRandomIndex, randomValue);
			}
			
		iconOptions.clear();
			
		for(int i=0; i<numberOfOptions; i++)
			{
			iconOptions.add(katanaIconLoader.getCharacters().get(characterIndexes.get(i)));
			}
		
		boolean removeAndReplace = true;
			
		for(int i=0; i<numberOfOptions; i++)
			{
			if(katanaCanvas.getCharacter().equals(iconOptions.get(i).getCharacter()))
				{
				removeAndReplace = false;
				break;
				}
			}
			
		if(removeAndReplace)
			{		
			correctOption = random.nextInt(numberOfOptions);
		
			iconOptions.remove(correctOption);
			
			iconOptions.add(correctOption, katanaIconLoader.getCharacters().get(katanaCanvas.getCharacterIndex()));
			}
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
		g2d.setColor(Color.black);
		g2d.fillRect(0,0,PWIDTH,PHEIGHT);
		
		for(int i=0; i < numberOfOptions; i++)
			{
			g2d.drawImage(iconOptions.get(i).getImage(), (i * 100), 0, null);
			}		
		
		//g2d.fillRect(mouse.getX() - 2, mouse.getY() - 2, 4, 4);
		//g2d.drawString("Button: " + mouse.getButton(), PWIDTH / 60, (PHEIGHT / 20));

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
		mouse = Mouse.getInstance();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		}
	}