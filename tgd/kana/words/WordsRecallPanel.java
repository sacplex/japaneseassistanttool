package com.tgd.kana.words;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class WordsRecallPanel extends JPanel implements Runnable
	{
	private WordsCanvas wordsCanvas;
	
	private WordsAudioLoader wordsAudioLoader;
	
	private ProbablyStringMatchesInterface probablyStringMatchesInterface;
	
	private Thread updator;
	
	private int counter;
	private int numberOfCorrectAnswer;
	
	private float matchValueScore;
	
	final JTextField userText = new JTextField(46);
	
	private volatile boolean running; 
	
	public WordsRecallPanel(WordsCanvas wordsCanvas)
		{
		this.wordsCanvas = wordsCanvas;
		
		probablyStringMatchesInterface = new ProbablyStringMatchesInterface();
		probablyStringMatchesInterface.loadLibrary();
		
		setPreferredSize(new Dimension(1200, 50));
		
		userText.setHorizontalAlignment(JTextField.CENTER);
		userText.setFont(new Font("SansSerif", 4, 30));		
		
		userText.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				counter++;
				
				System.out.println(userText.getText());
				checkAnswer(userText.getText().toLowerCase());
				
				userText.setText("");
				}
			});
		
		DocumentFilter filter = new UppercaseDocumentFilter();	
		((AbstractDocument)userText.getDocument()).setDocumentFilter(filter);
				
		add(userText);
		}
		
	public void add(WordsAudioLoader wordsAudioLoader)
		{
		this.wordsAudioLoader = wordsAudioLoader;
		}
		
	public WordsAudioLoader getWordsAudioLoader()
		{
		return wordsAudioLoader;
		}
		
	private void checkAnswer(String inputText)
		{
		System.out.println("Input Text: " + inputText);
		System.out.println("wordsCanvas.getCorrectWord().getEnglish(): " + wordsCanvas.getCorrectWord().getEnglish());
		
		float matchValueEng = probablyStringMatchesInterface.stringMatch(wordsCanvas.getCorrectWord().getEnglish().toLowerCase(), inputText.toLowerCase());
		
		float matchValueJap = probablyStringMatchesInterface.stringMatch(wordsCanvas.getCorrectWord().getKana().toLowerCase(), inputText.toLowerCase());
		
		float matchValue;
		
		System.out.println("Match Value English: " + matchValueEng + ", Match Value Japanese: " + matchValueJap);
		
		if(matchValueEng > matchValueJap)
			matchValue = matchValueEng;
		else
			matchValue = matchValueJap;		
		
		matchValueScore = matchValueScore + matchValue;
		
		System.out.println("Match Value: " + matchValue);
		System.out.println("Match Value Score: " + matchValueScore);
		
		if(wordsAudioLoader != null)
			{
			wordsAudioLoader.play();
			wordsAudioLoader.setResetWord(true);
			}
			
		numberOfCorrectAnswer++;
		
		//matchValueScore = matchValueScore / numberOfCorrectAnswer;
		
		/*if(wordsCanvas.getCorrectWord().getEnglish().equalsIgnoreCase(inputText))
			{
			System.out.println("Correct");
			wordsAudioLoader.play();
			numberOfCorrectAnswer++;
			}
		else
			{
			System.out.println("Incorrect");
			wordsCanvas.resetWord();
			}*/
		
		//hiraganaCanvas.setNumberOfCorrectAnswer(numberOfCorrectAnswer);
		//hiraganaCanvas.setCounter(counter);		
		//hiraganaCanvas.nextCharacter();	
		}
		
	public void startThread()
		{
		if(updator == null && !running)
			{
			updator = new Thread(this, "Update Thread");
			updator.start();
			}
		}
		
	public void run()
		{
		running = true;
		
		while(running)
			{
			update();

			sleep();
			}
		}
		
	private void update()
		{
		if(wordsAudioLoader != null)				
			wordsAudioLoader.update();
		}
		
	private void sleep()
		{
		try
			{
			Thread.sleep(20);
			}
		catch(InterruptedException ex)
			{
			System.err.println("An Interrupt Exception has occured for this event");
			}
		}
		
	public float getMatchValueScore()
		{
		return matchValueScore;
		}
		
	public native float stringMatch(String left, String right);
		
	class UppercaseDocumentFilter extends DocumentFilter
		{
    	public void insertString(DocumentFilter.FilterBypass fb, int offset,
            String text, AttributeSet attr) throws BadLocationException
			{
			fb.insertString(offset, text.toUpperCase(), attr);
    		}

    	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs) throws BadLocationException
			{
	      fb.replace(offset, length, text.toUpperCase(), attrs);
    		}
		}
	}	