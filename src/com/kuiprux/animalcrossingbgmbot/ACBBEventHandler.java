package com.kuiprux.animalcrossingbgmbot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.kuiprux.animalcrossingbgmbot.guild.GuildMusicManager;
import com.kuiprux.animalcrossingbgmbot.updater.BgmVersion;
import com.kuiprux.animalcrossingbgmbot.updater.GuildDataApplier;
import com.kuiprux.animalcrossingbgmbot.updater.MessageHolder;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ACBBEventHandler extends ListenerAdapter {
	
	public List<WeakReference<MessageHolder>> msgHolderList = new ArrayList<>();

	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		for(int i = 0; i < msgHolderList.size(); i++) {
			MessageHolder msgHolder =  msgHolderList.get(i).get();
			if(msgHolder == null) {
				msgHolderList.remove(i);
				i--;
			} else {
				if(event.getMessageIdLong() == msgHolder.getMsgId())
					msgHolder.setMsg(null);
			}
		}
	}
	
	public void register(MessageHolder msgHolder) {
		msgHolderList.add(new WeakReference<>(msgHolder));
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		MessageChannel chnl = event.getChannel();
		User sender = event.getAuthor();
		
		if(!sender.isBot()) {
			String[] words = event.getMessage().getContentRaw().split(" ");
			if(words[0].equals("!bgm")) {
				if(words.length > 1) {
					GuildMusicManager guildMusicManager = AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.getGuildMusicManager(event.getGuild());
					GuildDataApplier guildDataApplier = guildMusicManager.getGuildDataApplier();
					switch(words[1]) {
					case "version":
						if(words.length == 2) {
							chnl.sendMessage("현재 BGM은 " + guildDataApplier.getVersionName() + " 버전으로 적용되어 있습니다.").queue();
							chnl.sendMessage("사용법: !bgm version <버전>").queue();
							chnl.sendMessage("현재 사용 가능한 버전: accf/타동숲, acnh/모동숲").queue();
						} else if(words.length == 3) {
							boolean success = false;
							switch(words[2]) {
							case "accf":
							case "타동숲":
								guildDataApplier.setVersion(BgmVersion.CITY_FOLK);
								success = true;
								break;
							case "acnh":
							case "모동숲":
								guildDataApplier.setVersion(BgmVersion.NEW_HORIZON);
								success = true;
								break;
							}
							if(success) {
								chnl.sendMessage("BGM을 " + guildDataApplier.getVersionName() + " 버전으로 적용하였습니다.").queue();
								if(guildMusicManager.isPlaying()) {
									guildMusicManager.stop();
									AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.play(event.getGuild(), chnl, sender);
								}
							} else {
								chnl.sendMessage("사용법: !bgm version <버전>").queue();
								chnl.sendMessage("현재 사용 가능한 버전: accf/타동숲, acnh/모동숲").queue();
							}
						} else {
							chnl.sendMessage("사용법: !bgm version <버전>").queue();
							chnl.sendMessage("현재 사용 가능한 버전: accf/타동숲, acnh/모동숲").queue();
						}
						break;
					case "play":
						if (words.length == 2) {
							if (guildDataApplier.isLocationSet()) {
								if(AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.checkAlreadyConnected(event.getGuild(), sender) && guildMusicManager.isPlaying()) {
									chnl.sendMessage("이미 재생중입니다.").queue();
								} else {
									/*if(guildMusicManager.isPlaying())
										guildMusicManager.stop();*/
									AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.play(event.getGuild(), chnl, sender);
									chnl.sendMessage(guildDataApplier.getLocationString() + "을 기준으로 bgm을 재생합니다.").queue();
								}
							} else {
								chnl.sendMessage("`!bgm play <지역명(영어로)>` 또는 `!bgm play <위도> <경도>`로 지역을 선택하세요.").queue();
							}
						} else if (words.length > 4) {
							chnl.sendMessage("사용법: `!bgm play <지역명(영어로)>` 또는 `!bgm play <위도> <경도>`").queue();
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
								chnl.sendMessage(guildDataApplier.getLocationString() + "을(를) 기준으로 bgm을 재생합니다.").queue();
							} else {
								chnl.sendMessage("해당 지역/위치를 찾을 수 없습니다.").queue();
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
					default:
						chnl.sendMessage("```\n"
								+ "`타운으로 놀러가요 동물의 숲` 게임의 BGM을 시간과 날씨에 맞춰 틀어주는 봇입니다.\n"
								+ "!bgm version <버전> bgm 버전을 설정합니다.\n"
								+ "!bgm play <지역명(영어로)>\t지역에 맞는 bgm을 재생합니다.\n"
								+ "!bgm play <위도> <경도>\t\t좌표에 맞는 bgm을 재생합니다.\n"
								+ "!bgm stop\t\t\t\tbgm을 중단합니다.\n"
								+ "```").queue();
					}
				} else {
					chnl.sendMessage("```\n"
							+ "`타운으로 놀러가요 동물의 숲` 게임의 BGM을 시간과 날씨에 맞춰 틀어주는 봇입니다.\n"
							+ "!bgm version <버전> bgm 버전을 설정합니다.\n"
							+ "!bgm play <지역명(영어로)>\t지역에 맞는 bgm을 재생합니다.\n"
							+ "!bgm play <위도> <경도>\t\t좌표에 맞는 bgm을 재생합니다.\n"
							+ "!bgm stop\t\t\t\tbgm을 중단합니다.\n"
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
					AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.tempGetGMM(event.getGuild()).guildDataApplier.befWeather = 
							AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.tempGetGMM(event.getGuild()).guildDataApplier.curWeather;
					AnimalCrossingBGMBot.ALL_GUILD_MUSIC_MANAGER.tempGetGMM(event.getGuild()).guildDataApplier.curWeather = Weather.values()[Integer.parseInt(words[1])];
				}
			}*/
		}
	}
}
