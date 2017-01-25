package com.tgd.kana.image;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class InteractImage
	{
	private BufferedImage image;
	
	private Rectangle rect;
	
	private int x;
	private int y;
	
	private boolean interact;
	
	public InteractImage(BufferedImage image, int x, int y)
		{
		this.image = image;
		this.x = x;
		this.y = y;
		
		rect = new Rectangle(x,y,image.getWidth(),image.getHeight());
		}
		
	public void draw(Graphics2D g2d)
		{
		g2d.drawImage(image, x, y, null);
		}
		
	public void update(int x, int y)
		{
		if(rect.contains(x,y))
			interact = true;
		}
		
	public void setInteracted(boolean interact)
		{
		this.interact = interact;
		}
		
	public boolean hasInteracted()
		{
		return interact;
		}
	}