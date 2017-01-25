package com.tgd.kana.words;

import com.tgd.kana.input.Mouse;

import com.tgd.kana.hiragana.HiraganaAudioLoader;
import com.tgd.kana.hiragana.HiraganaCharacter;
import com.tgd.kana.hiragana.HiraganaIconLoader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class WordsSubCanvas extends Canvas implements Runnable
	{
	public static int PWIDTH = 200;
	public static final int PHEIGHT = WordsLoader.NUMBER_OF_CHOICES * 60;
	
	private JFrame subFrame;
	private WordsCanvas wordsCanvas;
	
	private Random random;
	
	private int numberOfOptions = 3;
	private int selectedOption;
	
	private ArrayList<HiraganaCharacter> iconOptions;
	private ArrayList<Rectangle> collisionBoxes;
	
	private HiraganaIconLoader hiraganaIconLoader;
	private WordsAudioLoader wordsAudioLoader;
	
	private ArrayList<Integer> characterIndexes;
	private int counter;
	private int correctOption;
	private int numberOfClicks;
	private int numberOfCorrectAnswer;
	
	private Thread animator;
	private Graphics2D g2d;
	private BufferStrategy bufferStrategy;
	private FontRenderContext context;
	private Image image;
	private Font font;
	private Font resultsFont;
	
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
	private volatile boolean resizeCanvas = true;
	private volatile boolean correct;
	
	public WordsSubCanvas(WordsCanvas wordsCanvas, long period, FontRenderContext context)
		{
		this.wordsCanvas = wordsCanvas;
		this.context = context;
		
		font = new Font("SansSerif", 9, 32);
		
		setPreferredSize(new Dimension(determineWidth(), PHEIGHT));
		
		iconOptions = new ArrayList<>(numberOfOptions);
		
		initHiraganaIconLoader();
		
		addInputs();		
		}
		
	private void initHiraganaIconLoader()
		{
		hiraganaIconLoader = new HiraganaIconLoader();
			
		characterIndexes = new ArrayList<>();
		
		System.out.println("size: " + wordsCanvas.getWordsLoader().getChoices().size());
		
		for(int i=0; i<wordsCanvas.getWordsLoader().getChoices().size(); i++)
			characterIndexes.add(i);
		
		random = new Random();
		
		for(int i=0; i<wordsCanvas.getWordsLoader().getChoices().size()-1; i++)
			{
			int randomIndex = random.nextInt(wordsCanvas.getWordsLoader().getChoices().size());
			int randomValue = characterIndexes.get(randomIndex);
			int nextRandomIndex = random.nextInt(characterIndexes.size());
			
			characterIndexes.set(randomIndex, characterIndexes.get(nextRandomIndex));
			characterIndexes.set(nextRandomIndex, randomValue);
			}
			
		boolean removeAndReplace = true;

		collisionBoxes = new ArrayList<>();
		}
		
	public void add(WordsAudioLoader wordsAudioLoader)
		{
		this.wordsAudioLoader = wordsAudioLoader;
		}
		
	public void addFrame(JFrame frame)	
		{
		this.subFrame = frame;
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
		if(mouse.getState() == Mouse.MOUSE_STATE.PRESSED && !wordsCanvas.finished())
			{			
			for(int i=0; i<wordsCanvas.getWordsLoader().getChoices().size(); i++)
				{
				System.out.println(wordsCanvas.getWordsLoader().getChoices().get(i).getEnglish());
				}
						
			for(int i=0; i<wordsCanvas.getWordsLoader().getChoices().size(); i++)
				{
				if(wordsCanvas.getWordsLoader().getChoices().get(i).getRectangle() == null)
					System.out.println("rectangle is null " + i);
				
				if(wordsCanvas.getWordsLoader().getChoices().get(i).getRectangle().contains(mouse.getX(), mouse.getY()))
					{
					counter++;
					selectedOption = i;
					System.out.println("Click");
					checkAnswer(i);
					
					break;
					}
				}
			mouse.reset();
			}
		
		if(wordsAudioLoader != null)				
			wordsAudioLoader.update();
		}
		
	private void checkAnswer(int selectedOption)
		{
		numberOfClicks++;
		
		if(wordsCanvas.getWordsLoader().getChoices().get(selectedOption).getEnglish().equals(wordsCanvas.getCorrectWord().getEnglish()))
			{
			System.out.println("Correct");
			//wordsAudioLoader.setNewMedia(wordsCanvas.getCorrectWord().getKana());
			if(wordsAudioLoader != null)
				wordsAudioLoader.play();
			
			correct = true;
				
			numberOfCorrectAnswer++;
			}
		else
			{
			correct = false;
			System.out.println("Incorrect");
			wordsCanvas.resetWord();
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
		
		if(resizeCanvas)
			{
			context = g2d.getFontRenderContext();
			resizeCanvas = false;
			//determineWidth();
			}
		
		draw();
		
		g2d.dispose();
		
		bufferStrategy.show();
		}
		
	public void draw()
		{
		g2d.setColor(Color.black);
		g2d.fillRect(0,0,PWIDTH,PHEIGHT);
		g2d.setColor(Color.white);
		
		for(int i=0; i < characterIndexes.size(); i++)
			{
			drawCenteredString(wordsCanvas.getWordsLoader().getChoices().get(i).getEnglish(), i * 60);
			/*g2d.setColor(Color.red);
			g2d.drawRect(wordsCanvas.getWordsLoader().getChoices().get(i).getRectangle().x,
				wordsCanvas.getWordsLoader().getChoices().get(i).getRectangle().y,
				wordsCanvas.getWordsLoader().getChoices().get(i).getRectangle().width,
				wordsCanvas.getWordsLoader().getChoices().get(i).getRectangle().height);*/
			}
			
		
		
		//g2d.fillRect(mouse.getX() - 2, mouse.getY() - 2, 4, 4);
		//g2d.drawString("Button: " + mouse.getButton(), PWIDTH / 60, (PHEIGHT / 20));

		g2d.setColor(Color.black);
		}
		
	private void drawCenteredString(String text, int heightOffset)
		{
	   FontRenderContext context = g2d.getFontRenderContext();

		TextLayout txt = new TextLayout(text, font, context);
		
		Rectangle2D bounds = txt.getBounds();

		int xString = getWidth() / 2 - (int)bounds.getWidth() / 2;
		int yString = subFrame.getInsets().top + (int)bounds.getHeight() / 2;
		//(int)((getHeight() - (int)bounds.getHeight()) / WordsLoader.NUMBER_OF_CHOICES/* / (WordsLoader.NUMBER_OF_CHOICES + 2)*/);
		
		g2d.setFont(font);
		g2d.drawString(text, xString, yString + heightOffset);
		}
		
	public int determineWidth()
		{
		int length = wordsCanvas.getWordsLoader().getWords().get(0).getEnglish().length();
		String text = wordsCanvas.getWordsLoader().getWords().get(0).getEnglish();
		
		for(int i=1; i<wordsCanvas.getWordsLoader().getWords().size(); i++)
			{
			if(wordsCanvas.getWordsLoader().getWords().get(i).getEnglish().length() > length)
				{
				length = wordsCanvas.getWordsLoader().getWords().get(i).getEnglish().length();
				text = wordsCanvas.getWordsLoader().getWords().get(i).getEnglish();
				}
			}
			
		System.out.println("text: " + text);

		TextLayout txt = new TextLayout(text, font, context);
		
		Rectangle2D bounds = txt.getBounds();
		PWIDTH = (int)bounds.getWidth() + 60;
		
		if(wordsCanvas.getWordsLoader().getChoices() == null)
			System.out.println("choices is null");
		
		for(int i=0; i<wordsCanvas.getWordsLoader().getChoices().size(); i++)
			wordsCanvas.getWordsLoader().getChoices().get(i).setRectangle(0,i*60,PWIDTH,60);
		
		return PWIDTH;
		
		
		
		/*PWIDTH = xString + 60;
		
		for(int i=0; i<wordsCanvas.getWordsLoader().getChoices().size(); i++)
			wordsCanvas.getWordsLoader().getChoices().get(i).setRectangle(0,i*60,PWIDTH,60);
		
		wordsCanvas.getKana().resetLocation();
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		//wordsCanvas.resizeSubFrame(PWIDTH, PHEIGHT,wordsCanvas.getKana().getFrame());
		//wordsCanvas.getKana().setVisible(true);
		*/
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
		
	public void setGraphics2d(Graphics2D g2d)
		{
		this.g2d = g2d;
		}
		
	public boolean getCorrect()
		{
		return correct;
		}
		
	/*public void resetResizeCanvas()
		{
		determineWidth();
		}*/
		
	public WordsAudioLoader getWordsAudioLoader()
		{
		return wordsAudioLoader;
		}
		
	public int getNumberOfClicks()
		{
		return numberOfClicks;
		}
		
	public int getNumberOfCorrectAnswer()
		{
		return numberOfCorrectAnswer;
		}
		
	public Mouse getMouse()
		{
		return mouse;
		}
	}