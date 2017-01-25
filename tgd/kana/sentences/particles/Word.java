package com.tgd.kana.words;

import com.tgd.kana.hiragana.HiraganaCharacter;

import java.awt.Font;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import java.awt.geom.Rectangle2D;

import java.util.ArrayList;

public class Word
	{
	private Font font;
	
	private String [] standard = {"a","i","u","e","o","ka","ki","ku","ke","ko","sa","shi","su","se","so","ta","chi","tsu","te","to",
											"na","ni","nu","ne","no","ha","hi","fu","he","ho","ma","mi","mu","me","mo","ya","yu","yo",
											"ra","ri","ru","re","ro","wa","wo", "n"};
											
	private String [] dakuon = {"ga","gi","gu","ge","go","za","ji","zu","ze","zo","da","dji","tdu","de","do","ba","bi","bu","be","bo",
										 "pa","pi","pu","pe","po"};
										 
	private String [] yoon = {"kya","kyu","kye","kyo","sha","shu","she","sho","cha","chu","che","cho",
									  "nya","nyu","nye","nyo","hya","hyu","hye","hyo","mya","myu","mye","myo",
									  "rya","ryu","rye","ryo","gya","gyu","gye","gyo","ja","ju","je","jo",
									  "dja","dju","dje","djo","bya","byu","bye","byo","pya","pyu","pye","pyo"};
	
	private String kana;
	private String kanji;
	private String english;
	
	private String type;
	private int length;
	
	private boolean dPrintCharacter = false;
	
	public Word(String line)
		{
		font = new Font("SansSerif", 9, 48);
		
		byte [] lineArray = line.getBytes();
		int numberOfColons = 0;
		
		for(int i=0; i <lineArray.length; i++)
			{
			if(lineArray[i] == ':')
				numberOfColons++;
			}
		
		if(numberOfColons == 2) // Hiragana and Kanji
			{
			kana = line.split(":")[0];
			kanji = line.split(":")[1];
			english = line.split(":")[2];
			}
		else if(numberOfColons == 1) // Katana
			{
			kana = line.split(":")[0];
			english = line.split(":")[1];
			}
			
		System.out.println("English Word: " + english);
		}
		
	private int determineWidth(String word, FontRenderContext context)
		{
		length = word.length();
		String text = word;
		
		TextLayout txt = new TextLayout(text, font, context);
		
		Rectangle2D bounds = txt.getBounds();
			
		return (int)bounds.getWidth();
		}
		
	public int getKanaLength(FontRenderContext context)
		{
		return determineWidth(kana, context);
		}
		
	public int getKanjiLength(FontRenderContext context)
		{
		return determineWidth(kanji, context);
		}
		
	public void setKana(String kana)
		{
		this.kana = kana;
		}
		
	public String getKana()
		{
		return kana;
		}
		
	public void setKanji(String kanji)
		{
		this.kanji = kanji;
		}
		
	public String getKanji()
		{
		return kanji;
		}
		
	public void setEnglish(String english)
		{
		this.english = english;
		}
		
	public String getEnglish()
		{
		return english;
		}
		
	public void setFont(Font font)
		{
		this.font = font;
		}
		
	public Font getFont()
		{
		return font;
		}
		
	private void printCharacters(String type, String character, int length)
		{
		System.out.println(type + " kana: " + character + ", length: " + length);
		}
		
	public void clear()
		{
		kana = null;
		kanji = null;
		//english = null;
		}
	}