package com.kuiprux.animalcrossingbgmbot.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.kuiprux.animalcrossingbgmbot.Util;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.AudioCommand;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandAudioVolume;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandMoveAudio;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandPlay;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandStop;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandVolume;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandWait;

public class ACBBChannel {

	public LinkedList<AudioCommand> audioCommandList = new LinkedList<>();
	public Map<String, ACBBAudio> currentAudioMap = new HashMap<>();
	
	double volume = 1;

	public byte[] next20MsAudio() {
		CommandVolume cmdVolume = null;
		
		while (audioCommandList.size() > 0) {
			AudioCommand cmd = audioCommandList.getFirst();
			if(cmd instanceof CommandWait) {
				if(!((CommandWait) cmd).isWaiting())
					((CommandWait) cmd).startWaiting();
				
				if(((CommandWait) cmd).getLeftTime() <= 0) {
					audioCommandList.removeFirst();
					continue;
				}
			} else if(cmd instanceof CommandPlay) {
				//System.out.println(((CommandPlay) cmd).getName());
				currentAudioMap.put(((CommandPlay) cmd).getChannelName(), new ACBBAudio(((CommandPlay) cmd).getName(), ((CommandPlay)cmd).getAudioToSync(), ((CommandPlay) cmd).shouldLoop()));
				currentAudioMap.get(((CommandPlay) cmd).getChannelName()).volume = ((CommandPlay) cmd).getVolume();
				audioCommandList.removeFirst();
				continue;
			} else if(cmd instanceof CommandStop) {
				String targetChannelName = ((CommandStop) cmd).getTargetChannelName();
				if(targetChannelName == null)
					currentAudioMap.clear();
				else
					currentAudioMap.remove(targetChannelName);
				audioCommandList.removeFirst();
				continue;
			} else if(cmd instanceof CommandVolume) {
				if(!((CommandVolume) cmd).isVolumeChanging())
					((CommandVolume) cmd).startVolume(volume);
				
				cmdVolume = (CommandVolume) cmd;
				volume = ((CommandVolume) cmd).getVolume();
				if(((CommandVolume) cmd).isVolumeEnded()) {
					audioCommandList.removeFirst();
					continue;
				}
			} else if(cmd instanceof CommandAudioVolume) {
				String targetChannelName = ((CommandAudioVolume) cmd).getTargetChannelName();
				CommandVolume cv = ((CommandAudioVolume) cmd).getCmdVolume();
				if(targetChannelName == null) {
					for (ACBBAudio audio : currentAudioMap.values()) {
						audio.changeVolume(cv);
					}
				} else {
					System.out.println("channel: " + targetChannelName);
					currentAudioMap.get(targetChannelName).changeVolume(cv);
				}
				audioCommandList.removeFirst();
				continue;
			} else if(cmd instanceof CommandMoveAudio) {
				String fromChannelName = ((CommandMoveAudio) cmd).getFromChannelName();
				String toChannelName = ((CommandMoveAudio) cmd).getToChannelName();
				currentAudioMap.put(toChannelName, currentAudioMap.get(fromChannelName));
				currentAudioMap.remove(fromChannelName);
				
				audioCommandList.removeFirst();
				continue;
			}
			break;
		}
		
		byte[] data = new byte[Util.BYTE_IN_TWENTY_MILLIS];
		List<String> removeAudioList = new ArrayList<String>();
		for(Entry<String, ACBBAudio> audioEntry : currentAudioMap.entrySet()) {
			ACBBAudio audio = audioEntry.getValue();
			if(audio.nextIndex == -1)
				removeAudioList.add(audioEntry.getKey());
			byte[] tmp = audio.next20MsAudio();
			for(int i = 0; i < tmp.length && i < data.length; i++) {
				data[i] = (byte) Math.max(Byte.MIN_VALUE, Math.min(Byte.MAX_VALUE, data[i]+tmp[i]));
			}
		}
		
		for(String removeAudio : removeAudioList) {
			System.out.println("remove: " + removeAudio);
			currentAudioMap.remove(removeAudio);
		}
		
//		for(int i = 0; i < data.length; i++) {
//			data[i] *= volume;
//		}
		if(cmdVolume == null) {
			Util.processVolume(data, new CommandVolume(volume));
		} else {
			Util.processVolume(data, cmdVolume);
		}
		return data;
	}
	
	public void stop() {
		clearCommand();
		currentAudioMap.clear();;
	}
	
	public void clearCommand() {
		audioCommandList.clear();
	}
	
	public void addCommand(AudioCommand cmd) {
		if(cmd instanceof CommandPlay) {
			if(!ACBBMusicDataContainer.isMusicLoaded(((CommandPlay) cmd).getName())) {
				ACBBMusicDataContainer.loadMusic(((CommandPlay) cmd).getName());
			}
		}
		audioCommandList.add(cmd);
	}
	
	public void setVolume(double volume) {
		this.volume = Math.max(0, Math.min(1, volume));
	}
	
	public double getVolume() {
		return volume;
	}

}
