package com.kuiprux.animalcrossingbgmbot.music.audiocommand;

public class CommandWait extends AudioCommand {
	
	boolean isWaiting = false;
	
	long startTime;
	long endTime;
	
	int waitMillis;
	
	public CommandWait(int waitMillis) {
		this.waitMillis = waitMillis;
	}
	
	public void startWaiting() {
		isWaiting = true;
		startTime = System.currentTimeMillis();
		endTime = startTime + waitMillis;
	}
	
	public long getLeftTime() {
		return endTime - System.currentTimeMillis();
	}
	
	public boolean isWaiting() {
		return isWaiting;
	}

}
