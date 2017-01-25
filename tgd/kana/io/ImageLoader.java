package com.tgd.kana.io;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageLoader
	{
	private String fileName;
	
	private ArrayList <String> listOfFileNames;
	
	private BufferedImageIO bio;
	
	public ImageLoader()
		{
		listOfFileNames = new ArrayList<String>();
		}
	
	public ImageLoader(String fileName)
		{
		this.fileName = fileName;
		
		bio = new BufferedImageIO(this.fileName);
		bio.start();
		}
		
	public ImageLoader(String [] fileNames)
		{
		bio = new BufferedImageIO(fileNames);
		bio.start();
		}
		
	public void add(String fileName)
		{
		listOfFileNames.add(fileName);
		}
		
	public void	add(String [] fileNames)
		{
		for(int i = 0; i < fileNames.length; i++)
			{
			listOfFileNames.add(fileNames[i]);
			System.out.println(fileNames[i]);
			}
		}
		
	public void load()
		{
		String [] fileNames = new String[listOfFileNames.size()];
		
		fileNames = listOfFileNames.toArray(fileNames);
		
		bio = new BufferedImageIO(fileNames);
		bio.start();
		}	
		
	public BufferedImage getImage(String fileName)
		{
		BufferedImage image;
		
		bio.waitForThread();
		
		image = bio.getImage(fileName);
		
		return image;
		}
		
	public BufferedImage [] getAllImages()
		{
		BufferedImage [] images;
		
		bio.waitForThread();
		
		images = bio.getAllImages();
		
		return images;
		}
		
	public BufferedImage [] getSpriteImage(String fileName, int numberOfRows, int numberOfColumns)
		{
		BufferedImage image;
		BufferedImage [] images = new BufferedImage[numberOfRows * numberOfColumns];
		int width, height;
		
		bio.waitForThread();
		
		image = bio.getImage(fileName);
		
		if(image != null)
			{
			width = image.getWidth()/numberOfRows;
			height = image.getHeight()/numberOfColumns;
			
			int index = 0;
		
			for(int j = 0; j < numberOfColumns; j++)
				for(int i = 0; i < numberOfRows; i++)
					images[index++] = image.getSubimage(i * width, j * height, width, height);
			}
			
		return images;	
		}
		
	public void addToEndOfCurrentDirectory(String addDirectory)
		{
		bio.addToEndOfCurrentDirectory(addDirectory);
		}
		
	public void addToEndOfCurrentImageDirectory(String addDirectory)
		{
		bio.addToEndOfCurrentImageDirectory(addDirectory);
		}
		
	public ArrayList<String> getFilesNames()
		{
		return listOfFileNames;
		}
	}