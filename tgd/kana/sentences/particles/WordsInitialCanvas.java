package com.tgd.kana.words;

import com.tgd.kana.Kana;
import com.tgd.kana.input.Mouse;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;

import javax.swing.JFrame;

public class WordsInitialCanvas extends Canvas implements Runnable
	{
	public static final int PWIDTH = WordsCanvas.PWIDTH;
	public static final int PHEIGHT = 220;
	
	private JFrame frame;
		
	private Font font;
	private Font choiceFont;
	
	private Kana kana;
	
	private Mouse mouse;
	
	private Thread animator;
	private Graphics2D g2d;
	private BufferStrategy bufferStrategy;
	private FontRenderContext context;
	private Image image;
	
	private ArrayList<Choice> choices;
	private ArrayList<MultiChoice> multiChoices;
	
	private static final int NO_DELAYS_PER_YIELD = 16;
	private static final int NUM_FPS = 10;
	private static final int MAX_FRAME_SKIPS = 5;
	private static final long MAX_STATS_INTERVAL = 1000000000L;
	
	private static long hiraganaStartTime;
	
	private long eventStartTime;
	private int timeSpentInGame = 0; // in seconds
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
	
	private long period;
	
	private volatile boolean running;
	
	public WordsInitialCanvas(Kana kana, long period)
		{
		font = new Font("TimesRoman", Font.BOLD, 36);
		choiceFont = new Font("TimesRoman", Font.PLAIN, 20);
		
		this.period = period;
		
		if(kana != null)
			{
			this.kana = kana;
			
			setBackground(Color.black);
			setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
			}
		
		addInputs();
		}
		
	private void addChoices()
		{
		multiChoices = new ArrayList<>();
		
		MultiChoice mc = new MultiChoice(127,0,frame.getInsets().top + 127-12,PWIDTH-1,24);
		mc.addMultiLabel("BOTH");
		mc.addMultiLabel("KANA");
		mc.addMultiLabel("KANJI");
		
		multiChoices.add(mc);
		
		mc = new MultiChoice(103,0,frame.getInsets().top + 103-12,PWIDTH-1,24);
		mc.addMultiLabel("3 CHOICES");
		mc.addMultiLabel("4 CHOICES");
		mc.addMultiLabel("5 CHOICES");
		mc.addMultiLabel("6 CHOICES");
		mc.addMultiLabel("7 CHOICES");
		
		multiChoices.add(mc);
		
		mc = new MultiChoice(79,0,frame.getInsets().top + 79-12,PWIDTH-1,24);
		mc.addMultiLabel("UNORDER");
		mc.addMultiLabel("ORDER");
		
		multiChoices.add(mc);
		
		mc = new MultiChoice(55,0,frame.getInsets().top + 55-12,PWIDTH-1,24);
		mc.addMultiLabel("LESSON 1");
		mc.addMultiLabel("LESSON 2");
		mc.addMultiLabel("LESSON 3");
		mc.addMultiLabel("LESSON 5");
		mc.addMultiLabel("LESSON 6");
		mc.addMultiLabel("LESSON 7");
		mc.addMultiLabel("LESSON 8");
		mc.addMultiLabel("LESSON 9");
		mc.addMultiLabel("LESSON 10");
		mc.addMultiLabel("LESSON 11");
		mc.addMultiLabel("LESSON 12");
		
		multiChoices.add(mc);
		
		mc = new MultiChoice(31,0,frame.getInsets().top + 31-12,PWIDTH-1,24);
		mc.addMultiLabel("COG");
		
		multiChoices.add(mc);
		
		choices = new ArrayList<>();
		
		choices.add(new Choice("START",156,0,frame.getInsets().top + 156-12,PWIDTH-1,24));
		choices.add(new Choice("EXIT",180,0,frame.getInsets().top + 180-12,PWIDTH-1,24));
		}
		
	protected void addInputs()
		{
		mouse = Mouse.getInstance();
		addMouseListener(mouse);
		/*addMouseMotionListener(mouse);*/
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
		eventStartTime = System.nanoTime(); // this game event is starting now
		preFrameTime = eventStartTime;
		
		while(running)
			{
			update();
			render();
			//repaint();
			sleep();
			}
			
		System.out.println("LAST COMMAND OF THE INITIAL WINDOW");
		
		kana.init("words",
			multiChoices.get(4).getLabel(),
			multiChoices.get(3).getLabel(),
			multiChoices.get(2).getLabel(),
			multiChoices.get(1).getLabel(),
			multiChoices.get(0).getLabel());
		}
		
	private void update()
		{
		if(mouse.getState() == Mouse.MOUSE_STATE.PRESSED)
			{
			for(int i =0; i < multiChoices.size(); i++)
					{
					if(multiChoices.get(i).getRectangle().contains(mouse.getX(), mouse.getY()))
						{
						multiChoices.get(i).incrementLabel();
						}
					}
			
			for(int i =0; i < choices.size(); i++)
				{
				if(choices.get(i).getRectangle().contains(mouse.getX(), mouse.getY()))
					{
					if(choices.get(i).getEnglish().equals("START"))
						{
						System.out.println("Start Click");
						
						running = false;
						
						break;
						}
					else if(choices.get(i).getEnglish().equals("EXIT"))
						{
						System.exit(0);
						}
					}
				}				

			mouse.reset();
			}
		}
		
	public void render()
		{
		BufferStrategy bufferStrategy = getBufferStrategy();
		
		if(bufferStrategy == null)
			{
			addChoices();
			createBufferStrategy(3);
			return;
			}
			
		g2d = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		context = g2d.getFontRenderContext();
		
		draw();
		
		g2d.dispose();
		
		bufferStrategy.show();
		}
		
	private void draw()
		{
		g2d.setColor(Color.black);
		g2d.fillRect(0,0,PWIDTH,PHEIGHT);
		g2d.setColor(Color.white);
		drawCenteredString("Japanese Assistant Tool", -8, font);
		
		for(int i=0; i < choices.size(); i++)
			{
			/*g2d.drawRect(choices.get(i).getRectangle().x,
							 choices.get(i).getRectangle().y,
							 choices.get(i).getRectangle().width,
							 choices.get(i).getRectangle().height);*/
							 
			drawCenteredString(choices.get(i).getEnglish(), choices.get(i).getDrawHeight(), choiceFont);
			}
			
		g2d.setColor(Color.red);
			
		for(int i=0; i < multiChoices.size(); i++)
			{
			/*g2d.drawRect(multiChoices.get(i).getRectangle().x,
							 multiChoices.get(i).getRectangle().y,
							 multiChoices.get(i).getRectangle().width,
							 multiChoices.get(i).getRectangle().height);*/
							 
			drawCenteredString(multiChoices.get(i).getLabel(), multiChoices.get(i).getDrawHeight(), choiceFont);
			}
		}
		
	private void drawCenteredString(String text, int heightOffset, Font font)
		{
	   FontRenderContext context = g2d.getFontRenderContext();

		TextLayout txt = new TextLayout(text, font, context);
		
		Rectangle2D bounds = txt.getBounds();

		int xString = getWidth() / 2 - (int)bounds.getWidth() / 2;
		int yString = frame.getInsets().top + (int)bounds.getHeight() / 2;
		//(int)((getHeight() - (int)bounds.getHeight()) / WordsLoader.NUMBER_OF_CHOICES/* / (WordsLoader.NUMBER_OF_CHOICES + 2)*/);
		
		g2d.setFont(font);
		g2d.drawString(text, xString, yString + heightOffset);
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
		
	public void addFrame(JFrame frame)	
		{
		this.frame = frame;
		}
		
	private void processKeys(KeyEvent e)
		{
		}
		
	private void processKeyReleased(KeyEvent e)
		{
		}
	}