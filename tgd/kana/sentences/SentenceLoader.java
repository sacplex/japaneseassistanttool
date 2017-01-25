package com.tgd.kana.sentences;

import com.tgd.kana.io.FileIO;
import com.tgd.kana.hiragana.HiraganaImageLoader;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Random;

public class SentenceLoader
	{
	private HiraganaImageLoader hiraganaImageLoader;
	private FileIO fileIO;

	private ArrayList<Sentence> sentences;
	
	public SentenceLoader(String lesson, String choices, String mode)
		{
		System.out.println(lesson);
		fileIO = new FileIO("lesson" + lesson.substring(7,8) + ".txt");
		fileIO.addToCurrentDirectory("sentences\\Lessons\\");
		fileIO.run();
	
		sentences = new ArrayList<>();
		
		for(int i = 0; i < fileIO.size(); i++)
			{
			Sentence sentence = new Sentence(fileIO.getLines().get(i));
			
			sentences.add(sentence);
			}			
		}
		
	public ArrayList<Sentence> getSentence()
		{
		return sentences;
		}
		
	/*public void produceChoices(Word word)
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
		}*/
	}