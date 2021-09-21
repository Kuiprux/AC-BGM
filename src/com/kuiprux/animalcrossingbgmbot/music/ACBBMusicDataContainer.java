package com.kuiprux.animalcrossingbgmbot.music;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ACBBMusicDataContainer {
	
	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(48000f, 16, 2, true, true);
	public static final String EXTENSION_REGEX = "\\.[^.]*$";

	public static Map<String, ACBBMusicData> musicDataMap = new HashMap<>();
	
	private static void loadMusic(String name) {
		loadMusic(new File(name));
	}

	private static void loadMusic(File file) {
		if(file.isDirectory()) {
			for(File aFile : file.listFiles()) {
				loadMusic(aFile);
			}
		} else {
			try {
				AudioInputStream in = AudioSystem.getAudioInputStream(file);
				AudioInputStream din = AudioSystem.getAudioInputStream(AUDIO_FORMAT, in);
				byte[] bytes = din.readAllBytes();
				String[] name = file.getName().replaceAll(EXTENSION_REGEX, "").split("_");
				System.out.println(file.getName());
				if(name.length > 2) {
					try {
						musicDataMap.put(name[0], new ACBBMusicData(bytes, Integer.parseInt(name[1]), Integer.parseInt(name[2])));
						//System.out.println(name[0] + ": " + name[1] + "/" + name[2]);
					} catch(NumberFormatException e) {
						System.err.println("Error reading music loop data. Loading only music data...");
						musicDataMap.put(name[0], new ACBBMusicData(bytes));
					}
				} else {
					musicDataMap.put(name[0], new ACBBMusicData(bytes));
				}
			} catch(UnsupportedAudioFileException e1) {
				System.err.println(file.getAbsolutePath() + " could not be read as a music file.");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadMusics() {
		//System.out.println("ee");
		loadMusic("C:\\Users\\user\\Desktop\\testmusic.wav");
		loadMusic("musics");
		//loadMusic("musics\\backgrounds\\temp");
	}

}
