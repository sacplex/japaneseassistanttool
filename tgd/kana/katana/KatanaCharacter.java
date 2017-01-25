package com.tgd.kana.katana;

import java.awt.image.BufferedImage;

public class KatanaCharacter
	{
	private BufferedImage image;
	private String character;
	private int index;
		
	public void setImage(BufferedImage image)
		{
		this.image = image;
		}
		
	public BufferedImage getImage()
		{
		return image;
		}
		
	public void setCharacter(String character)
		{
		this.character = character;
		}
		
	public String getCharacter()
		{
		return character;
		}
		
	public void setIndex(int index)
		{
		this.index = index;
		}
		
	public int getIndex()
		{
		return index;
		}
	}