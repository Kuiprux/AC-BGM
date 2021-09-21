package com.kuiprux.animalcrossingbgmbot.music.audiocommand;

public class CommandMoveAudio extends AudioCommand {
	
	String fromChannelName;
	String toChannelName;

	public CommandMoveAudio(String fromChannelName, String toChannelName) {
		this.fromChannelName = fromChannelName;
		this.toChannelName = toChannelName;
	}
	
	public String getFromChannelName() {
		return fromChannelName;
	}
	
	public String getToChannelName() {
		return toChannelName;
	}

}
