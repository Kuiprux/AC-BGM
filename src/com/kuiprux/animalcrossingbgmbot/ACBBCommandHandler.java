package com.kuiprux.animalcrossingbgmbot;

import com.kuiprux.animalcrossingbgmbot.guild.GuildMusicManager;
import com.kuiprux.animalcrossingbgmbot.music.selection.ACBBMusicSelectionManager;
import com.kuiprux.animalcrossingbgmbot.music.selection.Weather;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ACBBCommandHandler extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		MessageChannel chnl = event.getChannel();
		User sender = event.getAuthor();
		
		if(!sender.isBot()) {
			String[] words = event.getMessage().getContentRaw().split(" ");
			if(words[0].equals("!bgm")) {
				if(words.length > 1) {
					GuildMusicManager guildMusicManager = AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.getGuildMusicManager(event.getGuild());
					ACBBMusicSelectionManager musicSelectionManager = guildMusicManager.getMusicSelectionManager();
					switch(words[1]) {
					case "play":
						if (words.length == 2) {
							if (musicSelectionManager.isLocationSet()) {
								if(AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.checkAlreadyConnected(event.getGuild(), sender) && guildMusicManager.isPlaying()) {
									chnl.sendMessage("이미 재생중입니다.").queue();
								} else {
									if(guildMusicManager.isPlaying())
										guildMusicManager.stop();
									AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.play(event.getGuild(), chnl, sender);
								}
							} else {
								chnl.sendMessage("`!bgm play <도시명(영어로)>` 또는 `!bgm play <위도> <경도>` 로 위치를 정해 주세요.").queue();
							}
						} else if (words.length > 4) {
							chnl.sendMessage("사용법: `!bgm play <도시명(영어로)>` 혹은 `!bgm play <위도> <경도>`").queue();
						} else {
							boolean success = false;
							if (words.length == 3) {
								success = guildMusicManager.setCity(words[2]);
							} else {
								success = guildMusicManager.setLatLon(words[2], words[3]);
							}

							if (success) {
								if(guildMusicManager.isPlaying())
									guildMusicManager.stop();
								AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.play(event.getGuild(), chnl, sender);
							} else {
								chnl.sendMessage("위치 설정에 실패하였습니다. 도시명 혹은 위치를 다시 확인해주세요.").queue();
							}
						}

						break;
					case "stop":
						if(guildMusicManager.isPlaying()) {
							guildMusicManager.stop();
							AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.leave(event.getGuild());
						} else
							chnl.sendMessage("현재 재생 중이 아닙니다.").queue();
						break;
					}
				} else {
					chnl.sendMessage("```\n"
							+ "`타운으로 놀러가요 동물의 숲`의 BGM을 시간과 날씨에 맞게 틀어 주는 봇입니다.\n"
							+ "!bgm play <도시명(영어로)>\tbgm을 재생합니다.\n"
							+ "!bgm play <위도> <경도>\t\tbgm을 재생합니다.\n"
							+ "!bgm stop\t\t\t\tbgm을 멈춥니다.\n"
							+ "```").queue();
				}
			}
			
			
			/*
			System.out.println(sender.getId());
			if(sender.getId().equals("368687855003893763")) {
				System.out.println(event.getMessage().getContentRaw());
				String[] words = event.getMessage().getContentRaw().split(" ");
				switch(words[0]) {
				case "pl":
					AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.play(event.getGuild(), chnl, sender);
					break;
				case "ll":
					AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.tempGetGMM(event.getGuild()).setLatLon(words[1], words[2]);
					break;
				case "wt":
					AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.tempGetGMM(event.getGuild()).musicSelectionManager.befWeather = 
							AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.tempGetGMM(event.getGuild()).musicSelectionManager.curWeather;
					AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.tempGetGMM(event.getGuild()).musicSelectionManager.curWeather = Weather.values()[Integer.parseInt(words[1])];
				}
			}*/
		}
	}
}
