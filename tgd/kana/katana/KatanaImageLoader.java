package com.tgd.kana.katana;

import com.tgd.kana.katana.KatanaCharacter;
import com.tgd.kana.io.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class KatanaImageLoader
	{
	private static final String [] FILENAMES = {"a", "chi", "e", "fu", "ha", "hi", "he", "ho", "i",
															  "ka", "ki", "ku", "ke", "ko", "ma", "mi", "mu", "me",
															  "mo", "n", "na", "ni", "nu", "ne", "no", "o", "ra",
                                               "ri", "ru", "re", "ro", "sa", "shi", "su", "se", "so", "ta",
                                               "tsu", "te", "to", "u", "wa", "wo", "ya", "yo", "yu"  
                                               };
                                               
	private static final String EXTENSION = ".png";
	
	private ImageLoader imageLoader;
	
	private ArrayList<KatanaCharacter> katanaCharacters;
	
	public KatanaImageLoader()
		{
		imageLoader = new ImageLoader();
		
		katanaCharacters = new ArrayList<>();
		
		buildCharacters();
		}
		
	private void buildCharacters()
		{
		loadCharacterImages();
		}
		
	private void loadCharacterImages()
		{
		for(int i=0; i < FILENAMES.length; i++)
			{
			imageLoader.add(FILENAMES[i] + EXTENSION);
			}
			
		imageLoader.load();
		imageLoader.addToEndOfCurrentDirectory("katana");
			
		BufferedImage [] characterImages = imageLoader.getAllImages();
		
		KatanaCharacter katanaCharacter = new KatanaCharacter();
		
		for(int i=0; i < FILENAMES.length; i++)
			{
			katanaCharacter.setImage(characterImages[i]);
			katanaCharacter.setCharacter(FILENAMES[i]);
			katanaCharacter.setIndex(i);
			katanaCharacters.add(katanaCharacter);
			katanaCharacter = new KatanaCharacter();
			}
		}
		
	public ArrayList<KatanaCharacter> getCharacters()
		{
		return katanaCharacters;
		}
		
	public int getNumberOfCharacters()
		{
		return FILENAMES.length;
		}
	}