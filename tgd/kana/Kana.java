package com.tgd.kana;

import com.tgd.kana.hiragana.HiraganaCanvas;
import com.tgd.kana.katana.KatanaCanvas;
import com.tgd.kana.words.WordsCanvas;
import com.tgd.kana.sentences.SentenceCanvas;
import com.tgd.kana.words.WordsInitialCanvas;

import java.awt.Canvas;

import javax.swing.JFrame;

public class Kana
	{
	public final static int DEFAULT_FPS = 120;
	private JFrame frame;
	private Canvas canvas = null;
	private long period;
	
	/*public Kana(String kana, String recognitionOrRecall, String totalCharacters)
		{
		int fps = DEFAULT_FPS;
		long period = (long)1000.0/fps;
		
		if(kana.equals("h"))
			canvas = new HiraganaCanvas(this,period * 1000000L, Integer.parseInt(totalCharacters));
		else if(kana.equals("k"))
			canvas = new KatanaCanvas(this,period * 1000000L, Integer.parseInt(totalCharacters));
		
		init(kana, recognitionOrRecall);
		}*/
		
	public Kana(String kana, String recognitionOrRecall, String totalCharacters, String order, String choices, String mode)
		{
		if(kana.equals("words"))
			preInit();
		else
			init(kana,null,"lesson 1","ORDER",null,null);
		//init(kana, recognitionOrRecall, totalCharacters, order, choices, mode);
		}
		
	private void preInit()	
		{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Japanese Assistant Tool");
		
		int fps = DEFAULT_FPS;
		period = (long)1000.0/fps;
		
		canvas = new WordsInitialCanvas(this, period);
		
		((WordsInitialCanvas)canvas).addFrame(frame);
		frame.add(canvas);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);	
		frame.setVisible(true);
		}
		
	public void init(String kana, String recognitionOrRecall, String lesson, String order, String choices, String mode)
		{
		if(kana.equals("h") || kana.equals("k"))
			{
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			}
		
		if(kana.equals("h"))
			{
			if(recognitionOrRecall.equals("cog"))
				((HiraganaCanvas)canvas).initHiraganaSubPanel(this, frame);
			else if(recognitionOrRecall.equals("call"))
				((HiraganaCanvas)canvas).initHiraganaRecallPanel(this, frame);
			}
		else if(kana.equals("k"))
			{
			if(recognitionOrRecall.equals("cog"))
				((KatanaCanvas)canvas).initKatanaSubPanel(this, frame);
			}
		else if(kana.equals("words"))
			{
			Canvas canvas = new WordsCanvas(this,period * 1000000L, recognitionOrRecall, lesson, order, choices, mode);
			((WordsCanvas)canvas).addFrame(frame);
			frame.remove(this.canvas);
			frame.add(canvas);
			this.canvas = canvas;
						
			frame.pack();
			}
		else if(kana.equals("sentences"))
			{
			frame = new JFrame();
			frame.setResizable(false);
			frame.setTitle("Japanese Assistant Tool");
			
			int fps = DEFAULT_FPS;
			period = (long)1000.0/fps;
			
			Canvas canvas = new SentenceCanvas(this,period * 1000000L, recognitionOrRecall, lesson, order, choices, mode);
			((SentenceCanvas)canvas).addFrame(frame);
			//frame.remove(this.canvas);
			frame.add(canvas);
			this.canvas = canvas;
			
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);	
			frame.setVisible(true);
			}
		}
		
	private boolean isNumeric(String str)
		{
		try
			{
			Integer.parseInt(str); 
			}
		catch(NumberFormatException e)
			{
			return false;
			}
			
		return true;
		}
		
	public void resetLocation()
		{
		frame.setLocationRelativeTo(null);
		}
		
	public void setVisible(boolean visible)
		{
		frame.setVisible(true);
		}
		
	public JFrame getFrame()
		{
		return frame;
		}
		
	public static void main(String [] args)
		{
		//System.out.println("args[4]: " + args[4]);
		
		new Kana(args[0],null,null,null,null,null);
		
		//if(args[0].equals("words"))
		//	new Kana(args[0],args[1],args[2],args[3],args[4],args[5]);
		/*else
			new Kana(args[0],args[1],args[2]);*/
		}
	}