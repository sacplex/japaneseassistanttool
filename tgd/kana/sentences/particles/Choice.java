package com.tgd.kana.words;

import java.awt.Rectangle;

public class Choice
	{
	private String english;
	private Rectangle rect;
	private int drawHeight;
	
	public Choice()
		{
		}
	
	public Choice(String english, int x, int y, int width, int height)
		{
		setChoice(english, x, y, width, height);
		}
		
	public Choice(String english, int drawHeight, int x, int y, int width, int height)
		{
		setChoice(english, drawHeight, x, y, width, height);
		}
	
	public void setEnglish(String english)
		{
		this.english = english;
		}
		
	public void setRectangle(int x, int y, int width, int height)
		{
		rect = new Rectangle(x,y,width,height);
		}
		
	public void setDrawHeight(int drawHeight)
		{
		this.drawHeight = drawHeight;
		}
		
	public String getEnglish()
		{
		return english;
		}
		
	public Rectangle getRectangle()
		{
		return rect;
		}
		
	public int getDrawHeight()
		{
		return drawHeight;
		}
		
	public void setChoice(String english, int x, int y, int width, int height)
		{
		setEnglish(english);
		setRectangle(x,y,width,height);
		}
		
	public void setChoice(String english, int drawHeight, int x, int y, int width, int height)
		{
		setEnglish(english);
		setRectangle(x,y,width,height);
		setDrawHeight(drawHeight);
		}
	}