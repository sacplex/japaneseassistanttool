package com.tgd.kana.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener
	{
	private static Mouse singletonInstance = null;
	
	private int mouseX = -1;
	private int mouseY = -1;
	private int mouseButton = -1;
	
	private boolean singleClick;
	
	public enum MOUSE_STATE { INITIAL, PRESSED, RELEASED };
	
	private MOUSE_STATE state = MOUSE_STATE.INITIAL;
	
	private Mouse()
		{
		}
		
	public static Mouse getInstance()
		{
		if(singletonInstance == null)
			singletonInstance = new Mouse();
			
		return singletonInstance;
		}
		
	public static Mouse getNewInstance()
		{
		return new Mouse();
		}
	
	public int getX()
		{
		return mouseX;
		}
		
	public int getY()
		{
		return mouseY;
		}
		
	public int getButton()
		{
		return mouseButton;
		}
		
	public void mouseEntered(MouseEvent e)
		{
		}
		
	public void mouseExited(MouseEvent e)
		{
		}
		
	public void mouseClicked(MouseEvent e)
		{
		}
		
	public void mousePressed(MouseEvent e)
		{
		mouseButton = e.getButton();
		
		mouseX = e.getX();
		mouseY = e.getY();
		
		state = MOUSE_STATE.PRESSED;
		}
		
	public void mouseReleased(MouseEvent e)
		{
		mouseButton = -1;
		
		state = MOUSE_STATE.RELEASED;
		}
		
	public void mouseMoved(MouseEvent e)
		{
		mouseX = e.getX();
		mouseY = e.getY();
		}
		
	public void mouseDragged(MouseEvent e)
		{
		mouseX = e.getX();
		mouseY = e.getY();
		}
			
	public MOUSE_STATE getState()
		{
		return state;
		}
		
	public void setButton(int e)	
		{
		mouseButton = e;
		}
		
	public void reset()
		{
		mouseX = -1;
		mouseY = -1;
		mouseButton = -1;
		
		state = MOUSE_STATE.INITIAL;
		}
	}