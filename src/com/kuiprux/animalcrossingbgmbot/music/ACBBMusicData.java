package com.kuiprux.animalcrossingbgmbot.music;

public class ACBBMusicData {
	
	byte[] data;
	int loopStartIndex;

	public ACBBMusicData(byte[] data) {
		this(data, 0, 1);
	}
	
	public ACBBMusicData(byte[] data, int numerator, int denominator) {
		this.data = data;
		//System.out.println("qq " + data.length);
		//System.out.println("ww " + data.length%4);
		//System.out.println("ee " + (data.length/4)%denominator);
		loopStartIndex = ((data.length/4)*numerator/denominator)*4;
	}

	public byte[] getData() {
		return data;
	}

	public int getLoopStartIndex() {
		return loopStartIndex;
	}
	
}
