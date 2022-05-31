package com.kuiprux.animalcrossingbgmbot.guild;

import com.kuiprux.animalcrossingbgmbot.music.ACBBAudioSendHandler;
import com.kuiprux.animalcrossingbgmbot.music.ACBBChannel;
import com.kuiprux.animalcrossingbgmbot.music.ACBBMixer;
import com.kuiprux.animalcrossingbgmbot.updater.GuildDataApplier;

import net.dv8tion.jda.api.entities.Guild;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
	
	Guild guild;
	ACBBMixer mixer = new ACBBMixer();
	ACBBAudioSendHandler audioSendHandler = new ACBBAudioSendHandler(mixer);
	public GuildDataApplier guildDataApplier; //TODO public test
	
	public GuildMusicManager(Guild guild) {
		this.guild = guild;
		mixer.acbbChannelList.add(new ACBBChannel());
		guildDataApplier = new GuildDataApplier(guild, mixer);
	}
	
	public boolean isPlaying() {
		return mixer.isPlaying();
	}
	
	public void play() {
		guildDataApplier.play();
	}
	
	public void stop() {
		guildDataApplier.stop();
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
		return guildDataApplier.setCity(city);
	}
	
	public boolean setLatLon(String lat, String lon) {
		return guildDataApplier.setLatLon(lat, lon);
	}
	
	public void setBellPattern(int[] bellPattern) {
		guildDataApplier.setBellPattern(bellPattern);
	}
	
	public GuildDataApplier getGuildDataApplier() {
		return guildDataApplier;
	}

}
