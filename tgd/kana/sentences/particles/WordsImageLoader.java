package com.tgd.kana.words;

import com.tgd.kana.hiragana.HiraganaCharacter;
import com.tgd.kana.io.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WordsImageLoader
	{
	private static String [] FILENAMES = {"a","i","u","e","o","ka","ki","ku","ke","ko","sa","shi","su","se","so","ta","chi","tsu","te","to",
											"na","ni","nu","ne","no","ha","hi","fu","he","ho","ma","mi","mu","me","mo","ya","yu","yo",
											"ra","ri","ru","re","ro","wa","wo", "n","ga","gi","gu","ge","go","za","ji","zu","ze","zo",
											"da","dji","tdu","de","do","ba","bi","bu","be","bo","pa","pi","pu","pe","po",
											"kya","kyu","kyo","sha","shu","sho","cha","chu","cho",
									  		"nya","nyu","nyo","hya","hyu","hyo","mya","myu","myo",
									  		"rya","ryu","ryo","gya","gyu","gyo","ja","ju","jo",
									  		"dja","dju","djo","bya","byu","byo","pya","pyu","pyo"};
                                               
	private static final String EXTENSION = ".png";
	private String directory;
	private String imageDirectory;
	
	private ImageLoader imageLoader;
	
	private ArrayList<HiraganaCharacter> hiraganaCharacters;
	
	public WordsImageLoader()
		{
		imageLoader = new ImageLoader();
		
		hiraganaCharacters = new ArrayList<>();
		
		buildCharacters();
		}
		
	public WordsImageLoader(String directory)
		{
		imageLoader = new ImageLoader();
		
		this.directory = directory;
		
		hiraganaCharacters = new ArrayList<>();
		
		buildCharacters();
		}
		
	public WordsImageLoader(String directory, String imageDirectory)
		{
		imageLoader = new ImageLoader();
		
		this.directory = directory;
		this.imageDirectory = imageDirectory;
		
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
		imageLoader.addToEndOfCurrentImageDirectory(imageDirectory);
			
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
		
	public HiraganaCharacter getCharacter(String character)
		{
		HiraganaCharacter hiraganaCharacter = null;
		
		for(int i = 0; i < hiraganaCharacters.size(); i++)
			{
			if(hiraganaCharacters.get(i).getCharacter().equals(character))
				hiraganaCharacter = hiraganaCharacters.get(i);
			}
			
		return hiraganaCharacter;
		}
		
	public int getNumberOfCharacters()
		{
		return FILENAMES.length;
		}
	}