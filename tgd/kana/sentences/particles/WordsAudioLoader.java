package com.tgd.kana.words;

import java.net.URL;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

import javafx.embed.swing.JFXPanel;

import javafx.stage.Stage;

public class WordsAudioLoader extends JFXPanel
	{
	private final String PACKAGEPATH = "com/tgd/kana/words/";
	private final String AUDIOPATH = "Audio//";
	private final String AUDIOEXTENSION = ".mp3";
	
	private MediaPlayer mediaPlayer;
	private WordsCanvas wordsCanvas;
	
	private volatile boolean playing;
	
	public WordsAudioLoader(String lesson, String fileName, WordsCanvas wordsCanvas)
		{
		this.wordsCanvas = wordsCanvas;
		
		init(lesson, fileName);
		}
		
	private void init(String lesson, String fileName)
		{
		try
			{
			java.io.File mediaFile = new java.io.File(PACKAGEPATH + AUDIOPATH + lesson + "//" + fileName + AUDIOEXTENSION);
			System.out.println(PACKAGEPATH + AUDIOPATH + lesson + "//" + fileName + AUDIOEXTENSION);
			URL resource = mediaFile.toURI().toURL();
			Media media = new Media(resource.toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(.5);
			}
		catch(java.net.MalformedURLException e)
			{
			System.out.println("Malformed URL Exception");
			System.out.println("File didn't load");
			//System.out.println(e);
			}
		catch(MediaException e)
			{
			System.out.println("Media Exception");
			System.out.println("File didn't load");
			//System.out.println(e);
			}
		}
		
	public void play()
		{
		Status status = mediaPlayer.getStatus();
				
		if(status == Status.UNKNOWN || status == Status.HALTED)
			return;
			
		if(status == Status.PAUSED || status == Status.READY || status == Status.STOPPED || status == Status.PLAYING)
			{
			System.out.println("Play");
			mediaPlayer.play();
			playing = true; 
			//mediaPlayer.seek(new Duration(15000));
			}
		}
		
	public void update()
		{
		if(mediaPlayer != null)
			{
			if(mediaPlayer.getCurrentCount() == 1 && playing)
				{
				mediaPlayer.stop();
				playing = false;
				wordsCanvas.resetWord();
				System.out.println("done");
				}
			}
		}
		
	public void setNewMedia(String lesson, String fileName)
		{
		if(!playing)
			init(lesson, fileName);
		}
	}