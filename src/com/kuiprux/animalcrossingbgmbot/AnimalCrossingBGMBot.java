package com.kuiprux.animalcrossingbgmbot;

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
		GoogleCloudHandler.init(args[2], args[3], args[4]);
		Util.APP_ID = args[1];
		
		ACBBMusicDataContainer.loadMusics("weather-sound/");
		ACBBMusicDataContainer.loadMusics("city-folk/bell/");
		
	    JDABuilder builder = JDABuilder.createDefault(args[0]);
	    builder.setActivity(Activity.playing("ACNH"));
	    builder.addEventListeners(EVENT_HANDLER);
	    JDA_INSTANCE = builder.build();
	}
}
