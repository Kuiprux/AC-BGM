package com.kuiprux.animalcrossingbgmbot.guild;

import com.kuiprux.animalcrossingbgmbot.music.ACBBAudioSendHandler;
import com.kuiprux.animalcrossingbgmbot.music.ACBBChannel;
import com.kuiprux.animalcrossingbgmbot.music.ACBBMixer;
import com.kuiprux.animalcrossingbgmbot.music.selection.ACBBMusicSelectionManager;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
	
	ACBBMixer mixer = new ACBBMixer();
	ACBBAudioSendHandler audioSendHandler = new ACBBAudioSendHandler(mixer);
	public ACBBMusicSelectionManager musicSelectionManager = new ACBBMusicSelectionManager(mixer); //TODO public test
	
	public GuildMusicManager() {
		mixer.acbbChannelList.add(new ACBBChannel());
	}
	
	public boolean isPlaying() {
		return mixer.isPlaying();
	}
	
	public void play() {
		musicSelectionManager.play();
	}
	
	public void stop() {
		musicSelectionManager.stop();
	}

	/**
	 * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
	 */
	public ACBBAudioSendHandler getSendHandler() {
		return audioSendHandler;
	}
	
	public ACBBMixer getMixer() {
		return mixer;
	}
	
	public boolean setCity(String city) {
		return musicSelectionManager.setCity(city);
	}
	
	public boolean setLatLon(String lat, String lon) {
		return musicSelectionManager.setLatLon(lat, lon);
	}
	
	public void setBellPattern(int[] bellPattern) {
		musicSelectionManager.setBellPattern(bellPattern);
	}
	
	public ACBBMusicSelectionManager getMusicSelectionManager() {
		return musicSelectionManager;
	}

}
