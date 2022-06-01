package com.kuiprux.animalcrossingbgmbot.music;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.kuiprux.animalcrossingbgmbot.GoogleCloudHandler;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandPlay;

public class ACBBMusicDataContainer {
	
	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(48000f, 16, 2, true, true);
	public static final String EXTENSION_REGEX = "\\.[^.]*$";

	public static Map<String, ACBBMusicData> musicDataMap = new HashMap<>();
	public static Map<String, Integer> musicRequestCounter = new HashMap<>();
	
	public static void loadBgmMusic(String titleName, String weatherName, int currentHour) {
		loadMusic(GoogleCloudHandler.getBgmFileName(titleName, weatherName, currentHour));
	}

	public static boolean isMusicLoaded(String name) {
		System.out.println("ismusicloaded: " + name);
		return musicDataMap.get(name) != null;
	}

	public static void loadMusic(String name) {
		String actualName = GoogleCloudHandler.getActualName(name);
		loadMusic(actualName, GoogleCloudHandler.getFileWithName(actualName));
	}


	public static void loadMusic(String name, byte[] rawData) {
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(new ByteArrayInputStream(rawData));
			AudioInputStream din = AudioSystem.getAudioInputStream(AUDIO_FORMAT, in);
			byte[] convData = din.readAllBytes();

			String[] names = name.replaceAll(EXTENSION_REGEX, "").split("_");
			System.out.println(names[0]);
			if(names.length > 2) {
				try {
					musicDataMap.put(names[0], new ACBBMusicData(convData, Integer.parseInt(names[1]), Integer.parseInt(names[2])));
					//System.out.println(name[0] + ": " + name[1] + "/" + name[2]);
				} catch(NumberFormatException e) {
					System.err.println("Error reading music loop data. Loading only music data...");
					musicDataMap.put(names[0], new ACBBMusicData(convData));
				}
			} else {
				musicDataMap.put(names[0], new ACBBMusicData(convData));
			}
		} catch(UnsupportedAudioFileException e1) {
			System.err.println(name + " could not be read as a music file.");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadDefaultMusics(String prefix) {
		List<String> names = GoogleCloudHandler.getActualNames(prefix);
		
		for(String name : names) {
			loadMusic(name, GoogleCloudHandler.getFileWithName(name));
			String[] name1 = name.split("\\.");
			if(name1.length > 1) {
				musicRequestCounter.put(name1[0], -1);
				System.out.println("D " + name1[0] + " counter: " + -1);
			}
		}
	}

	public static void requestMusic(String name) {
		if(!isMusicLoaded(name)) {
			loadMusic(name);
		}
		
		Integer counter = musicRequestCounter.get(name);
		System.out.println("R " + name + " counter: " + counter);
		
		if(counter == null)
			musicRequestCounter.put(name, counter = 0);
		
		if(counter < 0)
			return;
		
		musicRequestCounter.put(name, ++counter);
		
		System.out.println("Incremented " + name + " counter: " + counter);
	}

	public static void cancelRequest(String name) {
		Integer counter = musicRequestCounter.get(name);
		System.out.println("C " + name + " counter: " + counter);
		
		if(counter == null)
			musicRequestCounter.put(name, counter = 0);
		
		if(counter < 0)
			return;
		
		musicRequestCounter.put(name, --counter);
		
		System.out.println("Decremented " + name + " counter: " + counter);
		if(counter == 0) {
			System.out.println(name + " is removed");
			musicDataMap.remove(name);
		}
	}
	
	
	/*
	public static void loadMusic(String name) {
		loadMusic(new File(name));
	}
	
	public static void unloadMusic(String name) {
		musicDataMap.remove(name);
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
	}*/

}
