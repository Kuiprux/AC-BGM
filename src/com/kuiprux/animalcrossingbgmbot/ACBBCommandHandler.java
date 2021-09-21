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
									chnl.sendMessage("�̹� ������Դϴ�.").queue();
								} else {
									if(guildMusicManager.isPlaying())
										guildMusicManager.stop();
									AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.play(event.getGuild(), chnl, sender);
								}
							} else {
								chnl.sendMessage("`!bgm play <���ø�(�����)>` �Ǵ� `!bgm play <����> <�浵>` �� ��ġ�� ���� �ּ���.").queue();
							}
						} else if (words.length > 4) {
							chnl.sendMessage("����: `!bgm play <���ø�(�����)>` Ȥ�� `!bgm play <����> <�浵>`").queue();
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
								chnl.sendMessage("��ġ ������ �����Ͽ����ϴ�. ���ø� Ȥ�� ��ġ�� �ٽ� Ȯ�����ּ���.").queue();
							}
						}

						break;
					case "stop":
						if(guildMusicManager.isPlaying()) {
							guildMusicManager.stop();
							AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.leave(event.getGuild());
						} else
							chnl.sendMessage("���� ��� ���� �ƴմϴ�.").queue();
						break;
					}
				} else {
					chnl.sendMessage("```\n"
							+ "`Ÿ������ ����� ������ ��`�� BGM�� �ð��� ������ �°� Ʋ�� �ִ� ���Դϴ�.\n"
							+ "!bgm play <���ø�(�����)>\tbgm�� ����մϴ�.\n"
							+ "!bgm play <����> <�浵>\t\tbgm�� ����մϴ�.\n"
							+ "!bgm stop\t\t\t\tbgm�� ����ϴ�.\n"
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
