package com.kuiprux.animalcrossingbgmbot.guild;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class AllGuildMusicHandler {

	private final Map<Long, GuildMusicManager> musicManagers;

	public AllGuildMusicHandler() {
	    this.musicManagers = new HashMap<>();
	  }
	
	public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager();
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;
	}
	
	public void play(Guild guild, MessageChannel sentChannel, User sender) {
		if(checkAndJoin(guild, sentChannel, sender)) {
			GuildMusicManager gmm = getGuildMusicManager(guild);
			gmm.play();
		}
	}
	
	public boolean checkAndJoin(Guild guild, MessageChannel sentChannel, User sender) {
		VoiceChannel vChannel = getVoiceChannel(guild, sender);
		if(vChannel == null) {
			sentChannel.sendMessage("먼저 보이스 채널에 들어가 주세요.").queue();
			return false;
		}
		AudioManager audioManager = guild.getAudioManager();
	    if(checkAlreadyConnected(guild, sender)) {
	    	return true;
	    }
	    audioManager.openAudioConnection(vChannel);
	    return true;
	}
	private VoiceChannel getVoiceChannel(Guild guild, User sender) {
		for(GuildChannel channel : guild.getChannels(true)) {
			if(channel instanceof VoiceChannel) {
				for(Member member : channel.getMembers()) {
					if(sender.equals(member.getUser())) {
						return (VoiceChannel) channel;
					}
				}
			}
		}
		return null;
	}
	
	public boolean checkAlreadyConnected(Guild guild, User sender) {
		VoiceChannel vChannel = getVoiceChannel(guild, sender);
		AudioManager audioManager = guild.getAudioManager();
	    return audioManager.isConnected() && audioManager.getConnectedChannel().equals(vChannel);
	}
	
	public void leave(Guild guild) {
		guild.getAudioManager().closeAudioConnection();
	}
}
