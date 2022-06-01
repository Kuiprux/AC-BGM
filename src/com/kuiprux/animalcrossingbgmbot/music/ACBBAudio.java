package com.kuiprux.animalcrossingbgmbot.music;

import com.kuiprux.animalcrossingbgmbot.Util;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandVolume;

public class ACBBAudio {
	
	public String musicName;
	public int nextIndex = 0;
	public ACBBMusicData musicData;
	
	public boolean shouldLoop;
	
	CommandVolume cmdVolume;
	double volume = 1;

	public ACBBAudio(String musicName) {
		this(musicName, false);
	}

	public ACBBAudio(String musicName, boolean shouldLoop) {
		this(musicName, null, shouldLoop);
	}
	
	public ACBBAudio(String musicName, ACBBAudio audioToSync, boolean shouldLoop) {
		this.musicName = musicName;
		//System.out.println(musicName + "::" + shouldLoop);
		musicData = ACBBMusicDataContainer.musicDataMap.get(musicName);
		this.shouldLoop = shouldLoop;
		//System.out.println("AUDIOTOSYNC: " + audioToSync);
		if(audioToSync != null)
			nextIndex = audioToSync.nextIndex;
		if(musicData == null)
			System.err.println(musicName + " could not be played.");
	}
	
	public byte[] next20MsAudio() {
		if(nextIndex == -1 || musicData == null)
			return new byte[Util.BYTE_IN_TWENTY_MILLIS];
		
		byte[] data = musicData.getData();
		byte[] newData = new byte[Util.BYTE_IN_TWENTY_MILLIS];
		
		int formerLength = Math.min(data.length-nextIndex, Util.BYTE_IN_TWENTY_MILLIS);
		
		for(int i = 0; i < formerLength; i++) {
			newData[i] = data[nextIndex+i];
		}
		
		if(shouldLoop) {
			int latterLength = Util.BYTE_IN_TWENTY_MILLIS - formerLength;
			for(int i = 0; i < latterLength; i++) {
				newData[formerLength+i] = data[musicData.getLoopStartIndex()];
			}
		}
		
		nextIndex += Util.BYTE_IN_TWENTY_MILLIS;
		if (nextIndex >= data.length)
			if (shouldLoop)
				nextIndex = nextIndex - data.length + musicData.getLoopStartIndex();
			else
				nextIndex = -1;
		
		if(cmdVolume != null) {
			if(!cmdVolume.isVolumeChanging())
				cmdVolume.startVolume(volume);
			
			volume = cmdVolume.getVolume();
			if(cmdVolume.isVolumeEnded()) {
				cmdVolume = null;
			}
		}
		
		if(cmdVolume == null) {
			Util.processVolume(newData, new CommandVolume(volume));
		} else {
			Util.processVolume(newData, cmdVolume);
		}
		
		return newData;
	}
	
	public void changeVolume(CommandVolume cmdVolume) {
		this.cmdVolume = cmdVolume;
	}
}
