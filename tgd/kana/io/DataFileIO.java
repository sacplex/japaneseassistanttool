package com.tgd.mgfm.io;

import java.io.*;
import java.util.ArrayList;

public class DataFileIO extends FileIO
	{
	private static final String COMMENT_FILTER = "//";
	private static final String NEWLINE_FILTER = "";
	
	private static final String DATA_PATH = "Data";
	
	private String [] dataRecords;
	private String [] fileNames;
	private String [] otherFields;
	private String [] field;
	
	private String type;
	
	private int size;	
	private int columnNumber;
	
	public DataFileIO(String fileName)
		{
		super(DATA_PATH + slash + fileName);
		
		lines = readLines();
		
		toArray(lines);
		}
		
	public DataFileIO(String customisePath, String fileName)
		{
		super(DATA_PATH + slash + customisePath + slash + fileName);
		
		System.out.println(DATA_PATH + slash + customisePath + slash + fileName);
		
		lines = readLines();
		
		toArray(lines);
		}
		
	public DataFileIO(String fileName, int columnNumber)
		{
		super(DATA_PATH + slash + fileName);
		
		this.columnNumber = columnNumber;
		
		lines = readLines();
		
		toArray(lines);
		}
		
	public DataFileIO(String customisePath, String fileName, int columnNumber)
		{
		super(DATA_PATH + slash + customisePath + slash + fileName);
		
		this.columnNumber = columnNumber;
		
		lines = readLines();
		
		toArray(lines);
		}	
		
	public ArrayList<String> readLines()
		{
		System.out.println("Read Line");
		
		String line;
				
		try
			{
			FileReader fileReader = new FileReader(currentDirectory + fileName);
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			line = bufferedReader.readLine();
			
			while(line != null)
				{
				if(!(line.startsWith(COMMENT_FILTER) || line.equals(NEWLINE_FILTER)))
					lines.add(line);
				line = bufferedReader.readLine();
				}				
				
			line = null;			
				
			if(fileReader != null)
				fileReader.close();	
			}
		catch(IOException e)
			{
			System.err.println("Error, problem in reading the lines of this data file: " + currentDirectory + fileName);
			}

		return lines;		
		}
		
	private void toArray(ArrayList <String>tempRecords)
		{
		String [] fields;
		
		size = tempRecords.size();
		
		dataRecords = new String[size];
		fileNames = new String[size];
		
		if(columnNumber != 0)
			field = new String[size];
		
		dataRecords = tempRecords.toArray(dataRecords);
		
		tempRecords = null;
		
		for(int i = 0; i < size; i++)
			{
			fields = dataRecords[i].split(":");
			fileNames[i] = fields[0];
			
			if(columnNumber != 0 && columnNumber < fields.length)
				field[i] = fields[columnNumber];

			}
		}
		
	private void findRecords(ArrayList <String> dataRecords, String type)
		{
		ArrayList <String>tempRecords = new ArrayList<String>();
		
		int index = 0;
		
		try
			{		
			while(!dataRecords.get(index++).equals(type));
		
			while(!dataRecords.get(index).equals("# End"))
				tempRecords.add(dataRecords.get(index++));
				
			dataRecords = null;
			
			toArray(tempRecords);
			}
		catch(IndexOutOfBoundsException e)
			{
			System.err.println("Caution: Tag " + type + " not found ");
			size = 0;
			}
			
		}
		
	public void setFileName(String fileName)
		{
		this.fileName = DATA_PATH + slash + fileName;
		
		readLines();
		
		toArray(lines);
		}
		
	public void setType(String type)
		{
		this.type = type;
		
		findRecords(lines, type);
		}	
		
	public String [] getDataRecords()
		{
		return dataRecords;
		}
		
	public String getDataRecord(int index)
		{
		return dataRecords[index];
		}
		
	public String [] getFileNames()
		{
		return fileNames;
		}
		
	public String [] getAllFileNames()
		{
		String [] allFileNames = null;
		String tempList = "";
		
		for(int i = 0; i < dataRecords.length; i++)
			{
			String [] fields = dataRecords[i].split(":");
			
			for(int j = 0; j < fields.length; j++)
				{
				if(fields[j].contains("."))
					tempList = tempList + fields[j] + ":";
				}
			}
		
		tempList = tempList.substring(0, tempList.length()-1);
		allFileNames = tempList.split(":");
			
		return allFileNames;
		}
		
	public String getFileName(int index)
		{
		return fileNames[index];
		}
		
	public String [] getDataField()
		{
		return field;
		}
		
	public int getSize()
		{
		return size;
		}
		
	public String getPath()
		{
		return currentDirectory + DATA_PATH + slash;
		}	
	}