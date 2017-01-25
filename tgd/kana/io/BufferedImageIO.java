package com.tgd.kana.io;

import java.io.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class BufferedImageIO extends ImageIO
	{
	protected HashMap <String, BufferedImage>imagesMap;
	private BufferedImage image;
	
	private int count = 0;
	private boolean load = false;
	private boolean waiting = true;
	
	public BufferedImageIO()
		{
		imagesMap = new HashMap<String, BufferedImage>();
		}
	
	public BufferedImageIO(String imageName)
		{
		imagesMap = new HashMap<String, BufferedImage>();
		add(imageName);
//		start();
//		waitForThread();
		}	
		
	public BufferedImageIO(String []imageNames)
		{
		imagesMap = new HashMap<String, BufferedImage>();
		add(imageNames);
		}
		
	public BufferedImageIO(File file)
		{
		image = (BufferedImage)read(file);
		}			
		
	public void start()
		{
		if(loader == null)
			{
			loader = new Thread(this);
			loader.start();
		//	waitForThread();
			}
		}

	public void startAndWait()
		{
		if(loader == null)
			{
			loader = new Thread(this);
			loader.start();
			waitForThread();
			}
		}
		
	public void waitForThread()
		{
		try
			{
			loader.join();
			}
		catch(InterruptedException e)
			{
			System.out.println(e);
			}
		}			
		
	public void run()
		{
		System.out.println("Starting Buffered IO thread");
		loadSingleImages();
		System.out.println("Buffered IO thread Ending");
		}
		
	public void clear()
		{
		imagesMap.clear();
		fileNames.clear();
		}
		
	private void cleanUp()
		{
		System.gc();				
		}	
		
	public void setLoad(boolean load)
		{
		this.load = load;
		}			
		
	public void stopThread()
		{
		running = false;
		}
		
	public BufferedImage	getImage()
		{
		return image;
		}			
		
	public BufferedImage	getImage(String imageName)
		{
		BufferedImage retImage = null;
		
		if(fileNames.isEmpty())
			{
			retImage = loadImage(imageName);
			}
		else
			{
			retImage = (BufferedImage)imagesMap.get(imageName);
			}

		return retImage;
		}
		
	public BufferedImage []	getAllImages()
		{
		BufferedImage [] retImage = new BufferedImage[fileNames.size()];
		
		for(int i = 0; i<retImage.length; i++)
			{
			retImage[i] = (BufferedImage)imagesMap.get(fileNames.get(i));
			}

		return retImage;
		}
		
	public BufferedImage []	getAllImages(String [] fileNames)
		{
		BufferedImage [] retImage = new BufferedImage[fileNames.length];
		
		for(int i = 0; i<retImage.length; i++)
			{
			retImage[i] = (BufferedImage)imagesMap.get(fileNames[i]);
			}

		return retImage;
		}		
		
	public BufferedImage []	getImage(String imageName, int frames)
		{
		BufferedImage retImage [] = new BufferedImage[frames];
		
		if(fileNames.isEmpty())
			{
			retImage[0] = loadImage(imageName);
			}
		else
			{
			retImage[0] = (BufferedImage)imagesMap.get(imageName);			
			}

		return retImage;
		}
		
	public void setImage(String fileName, BufferedImage image)
		{
		write(fileName, image);
		}	
		
	public void loadSingleImages()
		{
		
		BufferedImage image = null;
		
		if(imagesMap == null)
			System.out.println("Images Map is null");

		if(fileNames == null)
			System.out.println("File names is null");		
			
		if(fileNames.isEmpty())
			{
			System.out.println("No fileNames have been added");
			}
		
		else if(imagesMap.containsKey(fileNames.get(count)))
			{
			System.out.println("Image has already been loaded: " + fileNames.get(count));
			}
	
		else
			{
			for(int i=0; i<fileNames.size(); i++)
				{
//				System.out.println(fileNames.get(i));
				image = loadImage(fileNames.get(i));
							
				if(image != null)
					{
					imagesMap.put(fileNames.get(i), image);
					}
				else
					{
					System.out.println("Image is null");
					}
						
				}
			}
		}
		
	private BufferedImage loadImage(String imageName)
		{
		BufferedImage loadedImage = null;
 		loadedImage = (BufferedImage)read(imageName);
		
		if(loadedImage == null)
			{
			System.out.println("image is null");
			}
		
		return loadedImage;
		}
		
	public String toString()
		{
		return fileNames.get(count);
		}		
	}