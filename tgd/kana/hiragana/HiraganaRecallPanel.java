package com.tgd.kana.hiragana;

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

public class HiraganaRecallPanel extends JPanel
	{
	private HiraganaCanvas hiraganaCanvas;
	
	private HiraganaAudioLoader hiraganaAudioLoader;
	
	private int counter;
	private int numberOfCorrectAnswer;
	
	final JTextField userText = new JTextField(4);
	
	public HiraganaRecallPanel(HiraganaCanvas hiragana)
		{
		hiraganaCanvas = hiragana;
		
		setPreferredSize(new Dimension(300, 100));
		
		userText.setHorizontalAlignment(JTextField.CENTER);
		userText.setFont(new Font("SansSerif", 9, 64));		
		
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
		
	public void add(HiraganaAudioLoader hiraganaAudioLoader)
		{
		this.hiraganaAudioLoader = hiraganaAudioLoader;
		}
		
	private void checkAnswer(String inputText)
		{
		System.out.println("Input Text: " + inputText);
		System.out.println("hiraganaCanvas.getCharacter(): " + hiraganaCanvas.getCharacter());
		
		if(hiraganaCanvas.getCharacter().contains(inputText))
			{
			System.out.println("Correct");
			hiraganaAudioLoader.play();
			numberOfCorrectAnswer++;
			}
		else
			{
			System.out.println("Incorrect");
			}
		
		hiraganaCanvas.setNumberOfCorrectAnswer(numberOfCorrectAnswer);
		hiraganaCanvas.setCounter(counter);		
		hiraganaCanvas.nextCharacter();	
		}
		
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