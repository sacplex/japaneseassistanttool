package com.tgd.kana.words;

import java.awt.Rectangle;
import java.util.ArrayList;

public class MultiChoice
	{
	private ArrayList<String> labels;
	private Rectangle rect;
	private int drawHeight;
	private int index;
	
	public MultiChoice(int drawHeight, int x, int y, int width, int height)
		{
		setChoice(drawHeight, x, y, width, height);
		}
		
	public void addMultiLabel(String label)
		{
		if(labels == null)
			labels = new ArrayList<String>();
			
		labels.add(label);
		}
		
	public void incrementLabel()
		{
		if(index == labels.size()-1)
			index = 0;
		else
			index++;
		}
		
	public String getLabel()
		{		
		return labels.get(index);
		}
	
	public void setRectangle(int x, int y, int width, int height)
		{
		rect = new Rectangle(x,y,width,height);
		}
		
	public void setDrawHeight(int drawHeight)
		{
		this.drawHeight = drawHeight;
		}
		
	public Rectangle getRectangle()
		{
		return rect;
		}
		
	public int getDrawHeight()
		{
		return drawHeight;
		}
		
	public void setChoice(int drawHeight, int x, int y, int width, int height)
		{
		setRectangle(x,y,width,height);
		setDrawHeight(drawHeight);
		}
	}