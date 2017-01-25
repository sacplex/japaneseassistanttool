package com.tgd.kana.words;

public class ProbablyStringMatchesInterface
	{
	public native float stringMatch(String left, String right);
	
	public static void loadLibrary()
		{
		System.loadLibrary("lib//ProbablyStringMatchesLibrary");
		}
	}