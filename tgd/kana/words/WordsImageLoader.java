package com.tgd.kana.words;

import com.tgd.kana.hiragana.HiraganaCharacter;
import com.tgd.kana.io.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WordsImageLoader
	{
	private static String [] FILENAMES = {"volume_icon"};
	private BufferedImage [] images;
                                               
	private static final String EXTENSION = ".png";
	private String directory;
	private String imageDirectory;
	
	private ImageLoader imageLoader;
	
	public WordsImageLoader()
		{
		imageLoader = new ImageLoader();
		
		loadImages();
		}
		
	public WordsImageLoader(String directory)
		{
		imageLoader = new ImageLoader();
		
		this.directory = directory;
		
		loadImages();
		}
		
	public WordsImageLoader(String directory, String imageDirectory)
		{
		imageLoader = new ImageLoader();
		
		this.directory = directory;
		this.imageDirectory = imageDirectory;
		
		loadImages();
		}
		
	private void loadImages()
		{
		for(int i=0; i < FILENAMES.length; i++)
			{
			imageLoader.add(FILENAMES[i] + EXTENSION);
			}
			
		imageLoader.load();
		
		if(directory != null)
			imageLoader.addToEndOfCurrentDirectory(directory);
			
		if(imageDirectory != null)	
			imageLoader.addToEndOfCurrentImageDirectory(imageDirectory);
		}
		
	public int getNumberOfCharacters()
		{
		return FILENAMES.length;
		}
		
	public BufferedImage getImage(String fileName)
		{
		return imageLoader.getImage(fileName);
		}
	}