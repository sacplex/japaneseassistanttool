package com.tgd.kana.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileIO extends IO
	{
	protected String fileName;
	
	protected ArrayList<String> lines;
	protected int size;
	
	public FileIO()
		{
		}
	
	public FileIO(String fileName)
		{
		this.fileName = fileName;
		
		lines = new ArrayList<String>();
		}
	
	public String [] getDirectoryList(String directory)
		{
		directory = currentDirectory + directory;
		
		File dataDirectory = new File(directory);
		
		String [] fileNames = dataDirectory.list();
		
		return fileNames;
		}
	
	public String [] getDirectoryList(String directory, final String filter)
		{
		directory = currentDirectory + directory;
		
		System.out.println(directory);
		
		File dataDirectory = new File(directory);
		
		FilenameFilter filenameFilter = new FilenameFilter() 
			{
			public boolean accept(File dir, String name) 
				{
				return name.endsWith(filter);
				}
			}; 
		
		String [] fileNames = dataDirectory.list(filenameFilter);
		
		return fileNames;
		}
		
	public void start()
		{
		if(loader == null)
			{
			loader = new Thread(this);
			loader.start();
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
		lines = readLines();
		}
		
	public ArrayList<String> readLines()
		{
		System.out.println("Read Line");
		
		String line;
				
		try
			{
			FileInputStream fileInputStream = new FileInputStream(currentDirectory + fileName);
			
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
			
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			line = bufferedReader.readLine();
			
			while(line != null)
				{
				lines.add(line);
				line = bufferedReader.readLine();
				size++;
				}				
				
			line = null;
			
			if(bufferedReader != null)
				bufferedReader.close();
			
			if(inputStreamReader != null)
				inputStreamReader.close();			
				
			if(fileInputStream != null)
				fileInputStream.close();	
			}
		catch(IOException e)
			{
			System.err.println("Error, problem in reading the lines of this data file: " + currentDirectory + fileName);
			}

		return lines;		
		}
		
	public void addToCurrentDirectory(String additionalDirectories)
		{
		currentDirectory = currentDirectory + additionalDirectories;
		}
		
	public ArrayList<String> getLines()
		{
		return lines;
		}
		
	public String getFileName()
		{
		return fileName;
		}
		
	public int size()
		{
		return size;
		}
	}