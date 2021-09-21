package com.kuiprux.animalcrossingbgmbot.music.audiocommand;

public class CommandAudioVolume extends AudioCommand {
	
	String targetChannelName;
	CommandVolume cmdVolume;

	public CommandAudioVolume(String targetChannelName, double volume) {
		this(targetChannelName, volume, 0);
	}
	public CommandAudioVolume(String targetChannelName, double volume, int durationMillis) {
		cmdVolume = new CommandVolume(volume, durationMillis);
		this.targetChannelName = targetChannelName;
	}
	
	public String getTargetChannelName() {
		return targetChannelName;
	}
	
	public CommandVolume getCmdVolume() {
		return cmdVolume;
	}

}
