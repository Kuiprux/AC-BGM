package com.kuiprux.animalcrossingbgmbot;

import javax.security.auth.login.LoginException;

import com.kuiprux.animalcrossingbgmbot.guild.AllGuildMusicHandler;
import com.kuiprux.animalcrossingbgmbot.music.ACBBMusicDataContainer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class AnimalCrossingBGMBot {
	public static JDA JDA_INSTANCE;
	public static final ACBBCommandHandler COMMAND_HANDLER = new ACBBCommandHandler();
	public static final AllGuildMusicHandler ALL_GUILD_MUSIC_MANAGER = new AllGuildMusicHandler();

	public static void main(String[] args) throws LoginException {
		Util.APP_ID = args[1];
		System.out.println("Loading Musics...");
		ACBBMusicDataContainer.loadMusics();
		System.out.println("Done.");
		
	    JDABuilder builder = JDABuilder.createDefault(args[0]);
	    builder.setActivity(Activity.playing("ACNH"));
	    builder.addEventListeners(COMMAND_HANDLER);
	    JDA_INSTANCE = builder.build();
	}
}
