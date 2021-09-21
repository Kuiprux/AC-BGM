package com.kuiprux.animalcrossingbgmbot.music.audiocommand;

public class CommandVolume extends AudioCommand {

	boolean isVolumeChanging = false;
	double volumeFrom;
	double volumeTo;
	long volumeStartTime;
	long volumeDuration;

	public CommandVolume(double volume) {
		this(volume, 0);
	}
	public CommandVolume(double volume, int durationMillis) {
		volumeTo = volume;
		volumeDuration = durationMillis;
	}
	
	public void startVolume(double currentVolume) {
		volumeStartTime = System.currentTimeMillis();
		volumeFrom = currentVolume;
		isVolumeChanging = true;
	}

	public double getVolume() {
		return getVolume(0);
	}

	public double getVolume(double offset) {
		double volume = volumeTo;
		if(isVolumeChanging) {
		long timeElapsed = System.currentTimeMillis()-volumeStartTime;
			if(timeElapsed >= volumeDuration) {
				volume = volumeTo;
				isVolumeChanging = false;
			} else {
				volume = volumeFrom + (volumeTo-volumeFrom)*((double) timeElapsed/volumeDuration);
			}
		}
		return volume;
	}
	
	public boolean isVolumeEnded() {
		return System.currentTimeMillis() >= volumeStartTime + volumeDuration;
	}
	
	public boolean isVolumeChanging() {
		return isVolumeChanging;
	}
}
