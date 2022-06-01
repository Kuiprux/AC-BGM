package com.kuiprux.animalcrossingbgmbot;

import java.util.Map;

import javax.security.auth.login.LoginException;

import com.kuiprux.animalcrossingbgmbot.guild.AllGuildMusicHandler;
import com.kuiprux.animalcrossingbgmbot.music.ACBBMusicDataContainer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class AnimalCrossingBGMBot {
	public static JDA JDA_INSTANCE;
	public static final ACBBEventHandler EVENT_HANDLER = new ACBBEventHandler();
	public static final AllGuildMusicHandler ALL_GUILD_MUSIC_MANAGER = new AllGuildMusicHandler();

	public static void main(String[] args) throws LoginException {
		Map<String, String> env = System.getenv();
		GoogleCloudHandler.init(env.get("PROJECT_ID"), env.get("BUCKET_NAME"));
		Util.APP_ID = env.get("APP_ID");
		
		ACBBMusicDataContainer.loadDefaultMusics("weather-sound/");
		ACBBMusicDataContainer.loadDefaultMusics("city-folk/bell/");
		
	    JDABuilder builder = JDABuilder.createDefault(env.get("JDA_TOKEN"));
	    builder.setActivity(Activity.playing("ACNH"));
	    builder.addEventListeners(EVENT_HANDLER);
	    JDA_INSTANCE = builder.build();
	}
}
