package com.kuiprux.animalcrossingbgmbot.music.audiocommand;

import com.kuiprux.animalcrossingbgmbot.music.ACBBAudio;

public class CommandPlay extends AudioCommand {
	
	String name;
	String channelName;
	boolean shouldLoop;
	ACBBAudio audioToSync;
	double volume;
	
	public CommandPlay(String name, String channelName) {
		this(name, channelName, 1);
	}

	public CommandPlay(String name, String channelName, boolean shouldLoop) {
		this(name, channelName, shouldLoop, 1);
	}
	
	public CommandPlay(String name, String channelName, double volume) {
		this(name, channelName, false, volume);
	}
	
	public CommandPlay(String name, String channelName, boolean shouldLoop, double volume) {
		this(name, channelName, shouldLoop, null, volume);
	}
	
	public CommandPlay(String name, String channelName, boolean shouldLoop, ACBBAudio audioToSync, double volume) {
		this.name = name;
		this.channelName = channelName;
		this.shouldLoop = shouldLoop;
		this.audioToSync = audioToSync;
		this.volume = volume;
	}
	
	public String getName() {
		return name;
	}
	
	public String getChannelName() {
		return channelName;
	}
	
	public boolean shouldLoop() {
		return shouldLoop;
	}
	
	public ACBBAudio getAudioToSync() {
		return audioToSync;
	}
	
	public double getVolume() {
		return volume;
	}

}
