package com.tgd.kana.katana;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Text
	{
	int frontSize;
	ArrayList<StringBuilder> lines;
	StringBuilder line;
	
	public Text()
		{
		lines = new ArrayList<>();
		
		line = new StringBuilder();
		
		lines.add(line);
		}
		
	public void add(char character)
		{
		if(lines.size() == 0)
			lines.add(line);
		
		line.append(character);
		}
	
	public void remove()
		{
		if(lines.size() > 0)
			{
			line = lines.get(lines.size()-1);
			
			if(line.length() > 0)
				line = line.deleteCharAt(line.length()-1);
			else if(line.length() == 0)
				lines.remove(lines.size()-1);
			}
		else if(lines.size() == 0)
			{
			System.out.println("no characters");
			
			line = new StringBuilder();
			}
		}
		
	public void newLineOrNewCommand()
		{
		if(lines.get(lines.size()-1).toString().endsWith(";"))
			{
			// TO DO
			}
			
		line = new StringBuilder();
		lines.add(line);
		}
		
	public void draw(Graphics2D g2d, int x, int y)
		{
		if(lines.size() > 0)
			{
			for(int i = 0; i < lines.size(); i++)
				{
				g2d.drawString(lines.get(i).toString(), x, y + i * 20);
				}
			}
		}
	}