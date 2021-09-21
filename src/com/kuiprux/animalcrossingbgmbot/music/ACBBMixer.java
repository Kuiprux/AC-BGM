package com.kuiprux.animalcrossingbgmbot.music;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ACBBMixer {

	public final List<ACBBChannel> acbbChannelList = new ArrayList<>();
	
	public final ACBBChannel bgmChannel = new ACBBChannel();
	public final ACBBChannel weatherChannel = new ACBBChannel();
	
	boolean isPlaying = false;
	
	public ACBBMixer() {
		acbbChannelList.add(bgmChannel);
		acbbChannelList.add(weatherChannel);
	}

	public byte[] next20MsAudio(ByteBuffer buffer) {
		buffer.clear();
		byte[] data = new byte[buffer.capacity()];
		for(ACBBChannel channel : acbbChannelList) {
			byte[] tmp = channel.next20MsAudio();
			for(int i = 0; i < tmp.length && i < data.length; i++) {
				data[i] = (byte) Math.max(Byte.MIN_VALUE, Math.min(Byte.MAX_VALUE, data[i]+tmp[i]));
			}
		}
		buffer.put(data);
		return data;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
		if(!isPlaying) {
			for(ACBBChannel channel : acbbChannelList) {
				channel.stop();
			}
		}
	}

}
