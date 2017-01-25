package com.tgd.kana.words;

import com.tgd.kana.io.FileIO;
import com.tgd.kana.hiragana.HiraganaImageLoader;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Random;

public class WordsLoader
	{
	private HiraganaImageLoader hiraganaImageLoader;
	private FileIO fileIO;

	private ArrayList<Word> words;
	private ArrayList<String> englishWords;
	private ArrayList<Choice> choices;
	private ArrayList<String> permanentWords;
	private ArrayList<String> tempChoices;
	private Random random;
	
	public static int NUMBER_OF_CHOICES;
	
	public WordsLoader(/*WordsImageLoader wordsImageLoader, */String lesson, String choices, String mode)
		{
		fileIO = new FileIO("lesson" + lesson.substring(7,8) + ".txt");
		fileIO.addToCurrentDirectory("words\\Lessons\\");
		fileIO.run();
		
		NUMBER_OF_CHOICES = new Integer(choices.substring(0,1));
	
		words = new ArrayList<Word>();
		englishWords = new ArrayList<String>();
		
		for(int i = 0; i < fileIO.size(); i++)
			{
			if(mode.equals("KANJI"))
				{
				Word word = new Word(fileIO.getLines().get(i));
				
				if(word.getKanji() != null)
					words.add(word);
				}
			else
				{
				words.add(new Word(fileIO.getLines().get(i)));				
				englishWords.add(words.get(i).getEnglish());
				}
			}
			
		System.out.println(words.size());
		}
		
	public ArrayList<Word> getWords()
		{
		return words;
		}
		
	public void produceChoices(Word word)
		{
		if(permanentWords == null)
			{
			permanentWords = new ArrayList<>(englishWords.size());
			
			for(int i=0; i<words.size(); i++)
				permanentWords.add(words.get(i).getEnglish());
			}
			
		if(tempChoices == null)
			tempChoices = new ArrayList<>(englishWords.size());
		else
			tempChoices.clear();
		
		if(choices == null)
			{
			choices = new ArrayList<>(NUMBER_OF_CHOICES);
			
			for(int i=0; i<NUMBER_OF_CHOICES; i++)
				choices.add(new Choice());
			}
		
		random = new Random();
		boolean correctAnswseredExcluded = true;
		Choice choice = null;
		
		for(int i=0; i<permanentWords.size(); i++)
			tempChoices.add(permanentWords.get(i));
			
		for(int i=0; i<NUMBER_OF_CHOICES; i++)
			{
			int randomIndex = random.nextInt(tempChoices.size());
			
			choices.get(i).setEnglish(tempChoices.get(randomIndex));
			tempChoices.remove(randomIndex);
			} 
			
		for(int i=0; i<choices.size(); i++)
			{
			if(word.getEnglish().equals(choices.get(i).getEnglish()))
				{
				correctAnswseredExcluded = false;
				break;
				}
			}
			
		if(correctAnswseredExcluded)
			{
			choices.get(random.nextInt(NUMBER_OF_CHOICES)).setEnglish(word.getEnglish());
			}
		}
		
	public ArrayList<Choice> getChoices()
		{
		return choices;
		}
		
	public ArrayList<String> getAllEnglishWords()
		{
		return englishWords;
		}
		
	public int numberOfWordsLeft()
		{
		return words.size();
		}
	}