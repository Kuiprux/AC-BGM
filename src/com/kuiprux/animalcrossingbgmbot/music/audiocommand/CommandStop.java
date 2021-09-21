package com.kuiprux.animalcrossingbgmbot.music.audiocommand;

public class CommandStop extends AudioCommand {
	
	String targetChannelName;
	
	public CommandStop() {}
	
	public CommandStop(String targetChannelName) {
		this.targetChannelName = targetChannelName;
	}
	
	public String getTargetChannelName() {
		return targetChannelName;
	}

}
