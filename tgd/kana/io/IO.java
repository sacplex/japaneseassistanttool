package com.tgd.kana.io;

import java.io.*;

import java.util.*;

public abstract class IO implements Runnable
	{
	protected ArrayList <String>fileNames;
	protected volatile boolean running;
	protected Thread loader;
	
	protected String currentDirectory;
	
	protected static String slash = File.separator;
	
	public IO()
		{
		fileNames = new ArrayList<String>();
		
		try
			{
			currentDirectory = new File(".").getCanonicalPath();
			this.currentDirectory = currentDirectory + slash;
			
			String packageName = this.getClass().getPackage().getName();
			
			if(packageName != null)
				{
				String [] packageNames = packageName.split("\\.");
				
				if(packageNames.length > 1)
					{				
					this.currentDirectory = this.currentDirectory + packageNames[0]+ slash + packageNames[1]+ slash + packageNames[2] + slash;
					}
				
				packageNames = null;
				packageName = null;
				}
			}
		catch(IOException e)
			{
			System.out.println("Can not found parent directory");
			}
		}
		
	public void add(String imageName)
		{
		fileNames.add(imageName);
		}
		
	public void add(String [] imageNames)
		{
		for(int i=0; i<imageNames.length; i++)
		fileNames.add(imageNames[i]);
		}
		
	public void addToEndOfCurrentDirectory(String addDirectory)
		{
		this.currentDirectory = this.currentDirectory + addDirectory + slash;
		}
		
	public abstract void start();
	
	public abstract void startAndWait();
	
	public abstract void waitForThread();
	}