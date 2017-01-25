package com.tgd.kana.sentences;

import com.tgd.kana.Kana;
import com.tgd.kana.hiragana.Text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;

import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import java.awt.geom.Rectangle2D;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class SentenceCanvas extends Canvas implements Runnable
	{
	private Kana kana;
	private Text text;
	
	private Random random;
	
	private Thread animator;
	private Graphics2D g2d;
	private Image image;
	
	//private WordsSubCanvas wordsSubCanvas;
	//private WordsImageLoader wordsImageLoader;	
	private Sentence sentence;	
	//private WordsAudioLoader wordsAudioLoader;
	private SentenceLoader sentenceLoader;
	
	private int counter;
	private int numberOfCorrectAnswer;
	private long timeCounter;
	
	private BufferedImage characterImage;
	private String character;
	private int characterIndex;
	private ArrayList<Integer> characterIndexes;
	
	private String mode;
	private String lesson;
	//private int numberOfWords;
		
	public static final int PWIDTH = 1250;
	public static final int PHEIGHT = 220;
	
	private Font resultsFont;
	private Font finalResultsFont;
	private FontRenderContext context;
	
	private JFrame mainFrame;
	private JFrame subFrame;
	
	protected long period;
	
	private static Integer lastKey = null;
	private static boolean repressKey = true;
	private boolean shiftDown;
	
	private static final int NO_DELAYS_PER_YIELD = 16;
	private static final int NUM_FPS = 10;
	private static final int MAX_FRAME_SKIPS = 5;
	private static final long MAX_STATS_INTERVAL = 1000000000L;
	
	private static long hiraganaStartTime;
	
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
	
	private boolean order;
	
	private volatile boolean running;
	private volatile boolean displayResults;
	private volatile boolean displayChoices;
	private volatile boolean resetWord;
	private volatile boolean resizeCanvas = true;
	private volatile boolean skipFrame;
	
	public SentenceCanvas(Kana kana, long period, String recognitionOrRecall, String lesson, String order, String choices, String mode)
		{
		this.period = period;
		this.lesson = lesson;
		this.mode = mode;
		
		displayChoices = true;
		resultsFont = new Font("TimesRoman", Font.PLAIN, 18);
		finalResultsFont = new Font("SansSerif", 9, 20);
		
		if(order.startsWith("ORDER"))
			this.order = true;
		
		if(kana != null)
			{
			initHiraganaPanel(kana, choices);
			}
			
		text = new Text();
		
		addInputs();
		}
		
	public void addFrame(JFrame frame)	
		{
		this.mainFrame = frame;
		}
	
	private void initHiraganaPanel(Kana kana, String choices)
		{
		//setDoubleBuffered(true);
		setBackground(Color.black);
				
		this.kana = kana;
		
		//wordsImageLoader = new WordsImageLoader("words","Hiragana");
		
		sentenceLoader = new SentenceLoader(lesson, choices, mode);
		
		extractWords();
		
		//numberOfWords = wordsLoader.getWords().size();
		
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		
		random = new Random();
		
		//wordsAudioLoader = new WordsAudioLoader(lesson, correctWord.getEnglish(),this);
		
		setFocusable(true);
		requestFocus();
		
		addInputs();
		}
		
	public void extractWords()
		{
		/*random = new Random();
		
		if(wordsLoader.getWords().size() == 0)
			{
			System.out.println("No more characters");
			displayResults = true;
			}
		else
			{
			int index = 0;
			
			if(!order)
				index = random.nextInt(wordsLoader.getWords().size());
				
			correctWord = wordsLoader.getWords().get(index);
			
			System.out.println("Correct Word English: " + correctWord.getEnglish());
			System.out.println("Correct Word Kanji Length: " + correctWord.getKanji());
			
			wordsLoader.getWords().remove(index);
				
			wordsLoader.produceChoices(correctWord);	
			}*/
			
		sentence = sentenceLoader.getSentence().get(19);
		}
		
	public void initWordsSubPanel(JFrame frame)
		{
		/*this.mainFrame = frame;
		this.subFrame = new JFrame();
		this.subFrame.setResizable(false);
		this.subFrame.setTitle("Words Input");
		this.subFrame.add(wordsSubCanvas);
		this.subFrame.pack();
		
		this.subFrame.setLocation(mainFrame.getX() + mainFrame.getWidth() / 2 - this.subFrame.getWidth() / 2, mainFrame.getY() + mainFrame.getHeight());
		
		this.subFrame.setVisible(true);*/
		}
		
	public void resizeSubFrame(int width, int height, JFrame frame)
		{
		/*this.subFrame.setResizable(true);
		this.subFrame.setSize(width, height);
		this.subFrame.pack();
		
		this.subFrame.setLocation(mainFrame.getX() + mainFrame.getWidth() / 2 - this.subFrame.getWidth() / 2, mainFrame.getY() + mainFrame.getHeight());*/
		}
		
	public void resetWord()
		{
		resetWord = true;
		skipFrame = true;
		}
	
	private void nextWord()
		{
		extractWords();
		
				//wordsSubCanvas.getWordsAudioLoader().setNewMedia(lesson, correctWord.getEnglish());
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

			sleep();
			}
		}
		
	private void update()
		{
		if(sentenceLoader.getSentence().size() != 0)
			timeCounter++;
			
		if(finished())
			subFrame.setVisible(false);
		
		if(resetWord)
			{
			resetWord = false;
			nextWord();
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
			//wordsSubCanvas = new WordsSubCanvas(this, period, context);
			
			JFrame subFrame = new JFrame();
			subFrame.setUndecorated(false);
			subFrame.setResizable(false);
			subFrame.setTitle("Kana");
			//subFrame.add(wordsSubCanvas);
			subFrame.pack();
			
			//wordsSubCanvas.initWordsSubPanel(subFrame);			
		
			
			resizeCanvas = false;
			//PWIDTH = correctWord.getKanaLength(context);
			
			System.out.println("bottom: " + mainFrame.getInsets().top);
			
			subFrame.setLocationRelativeTo(null);
			subFrame.setLocation(subFrame.getX(), mainFrame.getY() + mainFrame.getContentPane().getSize().height + mainFrame.getInsets().top);
			subFrame.setVisible(true);
			
			//wordsSubCanvas.addFrame(subFrame);
			//wordsSubCanvas.add(wordsAudioLoader);
			//wordsSubCanvas.startThread();
			}
		
		draw();
		
		g2d.dispose();
		
		bufferStrategy.show();
		}
		
	public void draw()
		{
		if(displayResults)
			{
			drawFinalResults();
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
		
		g2d.setColor(Color.white);
				
		/*if(mode.equals("BOTH") || mode.equals("KANJI"))
			if(correctWord.getKanji() != null)
				drawCenteredString(correctWord.getKanji(),-30);
				
		if(mode.equals("BOTH") || mode.equals("KANA"))
			drawCenteredString(correctWord.getKana(),60);*/
		
		/*if(wordsSubCanvas != null)
			drawResults("Results: " + wordsSubCanvas.getNumberOfCorrectAnswer() + "/" + wordsSubCanvas.getNumberOfClicks() + "/" + numberOfWords,
				(int)(PWIDTH * .70), (int)(PHEIGHT * .95));*/
				
		drawCenteredString(sentence.getDisplaySentence(),-30);
		
		drawCenteredString(sentence.getCompleteSentence(),60);

		g2d.setColor(Color.black);
		}
		
	private void drawResults(String text, int x, int y)
		{
		g2d.setFont(resultsFont);
		g2d.drawString(text, x, y);
		}
		
	private void drawFinalResults()
		{
		g2d.setColor(Color.black);
		g2d.fillRect(0,0,PWIDTH,PHEIGHT);
		
		g2d.setFont(finalResultsFont);
		g2d.setColor(Color.white);
		g2d.drawString("Final Results", 50, 50);
		
		/*if(wordsSubCanvas.getCorrect())
			g2d.drawString("Number of Correct Answers: " + (wordsSubCanvas.getNumberOfCorrectAnswer() - 1) + "/" + numberOfWords, 50, 100);
		else
			g2d.drawString("Number of Correct Answers: " + (wordsSubCanvas.getNumberOfCorrectAnswer()) + "/" + numberOfWords, 50, 100);
		
		if(((timeCounter%7200)/120) < 10)
			g2d.drawString("Time elapsed: " + (timeCounter/7200) + ":0" + ((timeCounter%7200)/120) , 50, 150);
		else
			g2d.drawString("Time elapsed: " + (timeCounter/7200) + ":" + ((timeCounter%7200)/120) , 50, 150);*/
		
		g2d.setColor(Color.black);
		}
		
	private void drawCenteredString(String text, int heightOffset)
		{
	   //FontRenderContext context = g2d.getFontRenderContext();
		
		TextLayout txt = new TextLayout(text, sentence.getFont(), context);
		
		Rectangle2D bounds = txt.getBounds();
		int xString = (int)((getWidth() / 2 - (int)bounds.getWidth() / 2));
		int yString = (int)((getHeight() / 2));
		
		g2d.setFont(sentence.getFont());
		g2d.drawString(new String(text), xString, yString + heightOffset);
		}
		
	private void drawCenteredResults(String text, int heightOffset)
		{
		TextLayout txt = new TextLayout(text, sentence.getFont(), context);
		
		Rectangle2D bounds = txt.getBounds();
		int xString = (int)((getWidth() - (int)bounds.getWidth()) / 2 );
		//int yString = (int)((getHeight() - (int)bounds.getHeight()) / (WordsLoader.NUMBER_OF_CHOICES + 1));
		
		//g2d.setFont(font);
		
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
			
		/*mouse = Mouse.getInstance();
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
		
	public SentenceLoader getSentenceLoader()
		{
		return sentenceLoader;
		}
		
	/*public WordsImageLoader getWordsImageLoader()
		{
		return wordsImageLoader;
		}*/
		
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
		
	public Kana getKana()
		{
		return kana;
		}
		
	public Sentence getSentence()
		{
		return sentence;
		}
		
	public boolean getDisplayChoices()
		{
		return displayChoices;
		}
		
	public boolean finished()
		{
		return sentenceLoader.getSentence().size() < 0 ? true : false;
		}
	}