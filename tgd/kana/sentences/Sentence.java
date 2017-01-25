package com.tgd.kana.sentences;

import com.tgd.kana.hiragana.HiraganaCharacter;

import java.awt.Font;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import java.awt.geom.Rectangle2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

public class Sentence
	{
	private Font font;
	
	private String sentence;
	private List<String> particles;
	private String completeSentence;
	private String displaySentence;
	
	private int sentenceLength;
	private int particlesLength;
	
	private boolean dPrintCharacter = false;
	
	public Sentence(String line)
		{
		font = new Font("SansSerif", 9, 48);
		
		this.sentence = line.split(":")[0];
		
		String [] particles = line.split(":")[1].split(",");
		int numberOfParticles = particles.length;

		this.particles = Arrays.asList(particles);
		
		int counter = this.particles.size();
		int indexCounter = 0;
		
		completeSentence = sentence;
		
		System.out.println("Pre: " + completeSentence);
		
		while(counter != 0)
			{
			completeSentence = completeSentence.replaceFirst(" ",this.particles.get(indexCounter++));
			counter--;
			}
			
		System.out.println("Post: " + completeSentence);
		
		displaySentence = sentence.replaceAll(" ","_");
		}
		
	private int determineWidth(String sentence, FontRenderContext context)
		{
		sentenceLength = sentence.length();
		String text = sentence;
		
		TextLayout txt = new TextLayout(text, font, context);
		
		Rectangle2D bounds = txt.getBounds();
			
		return (int)bounds.getWidth();
		}
		
	private int determineWidth(ArrayList<String> particles, FontRenderContext context)
		{
		for(int i = 0; i < particles.size(); i++)
			{
			particlesLength = determineWidth(particles.get(i), context);
			}	
			
		return particlesLength;
		}
		
	public void setFont(Font font)
		{
		this.font = font;
		}
		
	public Font getFont()
		{
		return font;
		}
		
	public String getSentence()
		{
		return sentence;
		}
		
	public String getCompleteSentence()
		{
		return completeSentence;
		}
		
	public String getDisplaySentence()
		{
		return displaySentence;
		}
		
	public void clear()
		{
		sentence = null;
		particles.clear();
		}
		
	public static void main(String [] args)
		{
		//Sentence sentence = new Sentence("sentence:a,b,c");
		
		String a = new String("kore dare kasadesuka");
		ArrayList<String> p = new ArrayList<String>();
		
		p.add("ha");
		p.add("no");
		
		while(p.size() != 0)
			{
			a = a.replaceFirst(" ",p.remove(0));
			}
			
		System.out.println(a);
		
		/*String Str = new String("kore dare kasadesuka");

      System.out.print("Return Value :" );
      System.out.println(Str.replaceFirst("(.*)Tutorials(.*)", "AMROOD"));

      System.out.print("Return Value :" );
      System.out.println(Str.replaceFirst(" ", "ha"));*/
		}
	}