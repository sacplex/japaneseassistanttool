package com.tgd.kana.io;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.imageio.*;

public abstract class ImageIO extends IO
	{
	private static String imageDirectory = "Images";
	protected HashMap imagesMap;
		
	protected Image read(String fileName)
		{
		Image image = null;
		
		try
			{
			image = javax.imageio.ImageIO.read(new File(currentDirectory + imageDirectory + slash + fileName));
			}
		catch(IOException e)
			{
			System.out.println(e);
			System.out.println("File: " + currentDirectory + imageDirectory + slash + fileName);
			}
		return image;	
		}
		
	protected Image read(String customDirectory, String fileName)
		{
		Image image = null;
		
		try
			{
			image = javax.imageio.ImageIO.read(new File(currentDirectory + customDirectory + slash + fileName));
			}
		catch(IOException e)
			{
			System.out.println(e);
			System.out.println("File: " + currentDirectory + imageDirectory + slash + fileName);
			}
		return image;	
		}
		
	protected Image read(File file)
		{
		Image image = null;
		
		try
			{
			image = javax.imageio.ImageIO.read(file);
			}
		catch(IOException e)
			{
			System.out.println(e);
			System.out.println("File: " + file.getName());
			}
		return image;	
		}
		
	protected void write(String fileName, Image image)
		{
		String [] extension = fileName.split("\\.");
		
		if(image != null)
			{
			try
				{
				javax.imageio.ImageIO.write((java.awt.image.BufferedImage)image, extension[extension.length-1], 
													  new File(currentDirectory + fileName));
				}
			catch(IOException e)
				{
				System.err.println("Error, this image file: " + fileName + " can't be written to disk");
				}
			}
		}	
		
	private void checkMemory()
		{
		Runtime runTime = Runtime.getRuntime();
		
		long totalMemory = (runTime.maxMemory()/(1024L*1024L));
		long memoryLimit = (long)((double)totalMemory * 0.90);
		
		if(((runTime.totalMemory()/(1024L*1024L))	-(runTime.freeMemory()/(1024L*1024L)) >= memoryLimit))
			{
			System.out.println("GC");
			System.gc();
			}
		}
		
	public void addToEndOfCurrentImageDirectory(String addDirectory)
		{
		this.imageDirectory = this.imageDirectory + slash + addDirectory + slash;
		}	
	}