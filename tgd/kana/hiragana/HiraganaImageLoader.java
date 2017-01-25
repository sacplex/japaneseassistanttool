package com.tgd.kana.hiragana;

import com.tgd.kana.hiragana.HiraganaCharacter;
import com.tgd.kana.io.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HiraganaImageLoader
	{
	private static final String [] FILENAMES = {"a", "ba", "bi", "bu", "be", "bo", "chi", "da", "di(ji)", 
                                               "du(zu)", "de", "do", "e", "fu", "ga", "gi", "gu", "ge",
                                               "go", "ha", "hi", "he", "ho", "i", "ka", "ki", "ku",
                                               "ke", "ko", "ma", "mi", "mu", "me", "mo", "n", "na", "ni",
                                               "nu", "ne", "no", "o", "pa", "pi", "pu", "pe", "po", "ra",
                                               "ri", "ru", "re", "ro", "sa", "shi", "su", "se", "so", "ta",
                                               "tsu", "te", "to", "u", "wa", "wo", "ya", "yo", "yu", "za",
                                               "zi(ji)", "zu", "ze", "zo" 
                                               };
                                               
	private static final String EXTENSION = ".png";
	private String directory;
	
	private ImageLoader imageLoader;
	
	private ArrayList<HiraganaCharacter> hiraganaCharacters;
	
	public HiraganaImageLoader()
		{
		imageLoader = new ImageLoader();
		
		hiraganaCharacters = new ArrayList<>();
		
		buildCharacters();
		}
		
	public HiraganaImageLoader(String directory)
		{
		imageLoader = new ImageLoader();
		
		this.directory = directory;
		
		hiraganaCharacters = new ArrayList<>();
		
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
		imageLoader.addToEndOfCurrentDirectory(directory);
			
		BufferedImage [] characterImages = imageLoader.getAllImages();
		
		HiraganaCharacter hiraganaCharacter = new HiraganaCharacter();
		
		for(int i=0; i < FILENAMES.length; i++)
			{
			hiraganaCharacter.setImage(characterImages[i]);
			hiraganaCharacter.setCharacter(FILENAMES[i]);
			hiraganaCharacter.setIndex(i);
			hiraganaCharacters.add(hiraganaCharacter);
			hiraganaCharacter = new HiraganaCharacter();
			}
		}
		
	public ArrayList<HiraganaCharacter> getCharacters()
		{
		return hiraganaCharacters;
		}
		
	public int getNumberOfCharacters()
		{
		return FILENAMES.length;
		}
	}