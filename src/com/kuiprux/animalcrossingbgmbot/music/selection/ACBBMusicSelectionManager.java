package com.kuiprux.animalcrossingbgmbot.music.selection;

import com.kuiprux.animalcrossingbgmbot.Util;
import com.kuiprux.animalcrossingbgmbot.music.ACBBAudio;
import com.kuiprux.animalcrossingbgmbot.music.ACBBMixer;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandAudioVolume;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandMoveAudio;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandPlay;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandStop;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandVolume;
import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandWait;

public class ACBBMusicSelectionManager {
	
	ACBBMixer mixer;
	ACBBTimeAndWeatherUpdater updater = new ACBBTimeAndWeatherUpdater(this);
	
	int[] bellPattern = Util.DEFAULT_BELL_PATTERN;
	
	int curHour;
	public Weather befWeather; //TODO public test
	public Weather curWeather; //TODO public test
	
	double rainMMPerHour = 0.0;
	double snowMMPerHour = 0.0;
	
	public ACBBMusicSelectionManager(ACBBMixer mixer) {
		this.mixer = mixer;
	}

	public void play() {
		mixer.acbbChannelList.get(0).audioCommandList.add(new CommandPlay("testmusic", "test"));
		mixer.setPlaying(true);
		//System.out.println("gmm");
		
		updater.setEnabled(true);
	}
	
	public void stop() {
		mixer.setPlaying(false);
		updater.setEnabled(false);
	}
	
	void init() {
		mixer.bgmChannel.clearCommand();
		mixer.bgmChannel.addCommand(new CommandStop());
		startBgm("weather-main", 1);

		mixer.weatherChannel.clearCommand();
		mixer.weatherChannel.addCommand(new CommandStop());
		startWeather("weather-sound-main", getVolume());
	}
	
	public void startBgm(String channelName, double volume) {
		startBgm(channelName, null, volume);
	}

	public void startBgm(String channelName, ACBBAudio audioToSync, double volume) {
		//DEB System.out.println("BGM: " + curWeather);
		String weatherString = curWeather == Weather.NORMAL ? "normal" : (curWeather == Weather.RAIN ? "rain" : "snow");
		mixer.bgmChannel.addCommand(new CommandPlay(weatherString + "-" + curHour, channelName, true, audioToSync, volume));
	}

	public void startWeather(String channelName, double volume) {
		//DEB System.out.println("weather: " + curWeather);
		String weatherString = curWeather == Weather.NORMAL ? "normal" : (curWeather == Weather.RAIN ? "rain" : "snow");
		mixer.weatherChannel.addCommand(new CommandPlay(weatherString, channelName, true, volume));
	}
	
	void updateTime(int hour) {
		curHour = hour;
		//DEB System.out.println(hour);
	}
	
	void applyTime() {
		mixer.bgmChannel.clearCommand();
		mixer.bgmChannel.addCommand(new CommandAudioVolume("weather-main", 0, 4000));
		mixer.bgmChannel.addCommand(new CommandWait(5000));
		mixer.bgmChannel.addCommand(new CommandStop());
		addBell();
		startBgm("weather-main", 1);
	}
	
	private void addBell() {
		for(int i = 0; i < bellPattern.length; i++) {
			int bell = bellPattern[i];
			if(bell >= 0)
				mixer.bgmChannel.addCommand(new CommandPlay("bell-" + bell, "bell-" + i));//TODO weather
			mixer.bgmChannel.addCommand(new CommandWait(700));
		}
		mixer.bgmChannel.addCommand(new CommandWait(7000));
	}
	
	
	//TODO test public
	public void updateWeather(double rainMMPerHour, double snowMMPerHour) {
		//DEB System.out.println("rain: " + rainMMPerHour);
		//DEB System.out.println("snow: " + snowMMPerHour);
		befWeather = curWeather;
		this.rainMMPerHour = rainMMPerHour;
		this.snowMMPerHour = snowMMPerHour;
		//TODO the code below is for test
		
		if(rainMMPerHour > 1 && rainMMPerHour > snowMMPerHour) {
			curWeather = Weather.RAIN;
		} else if(snowMMPerHour > 1) {
			curWeather = Weather.SNOW;
		} else if(Math.max(rainMMPerHour, snowMMPerHour) < 0.8 || curWeather == null) {
			curWeather = Weather.NORMAL;
		}
		
		
	}
	
	void applyWeather() {
		//DEB System.out.println(curWeather);
		//DEB System.out.println(befWeather);
		if(curWeather != befWeather) {
			mixer.bgmChannel.addCommand(new CommandAudioVolume("weather-main", 0, 5000));
			startBgm("weather-tmp", mixer.bgmChannel.currentAudioMap.get("weather-main"), 0);
			mixer.bgmChannel.addCommand(new CommandAudioVolume("weather-tmp", 1, 5000));
			mixer.bgmChannel.addCommand(new CommandWait(5000));
			mixer.bgmChannel.addCommand(new CommandMoveAudio("weather-tmp", "weather-main"));
		}
	}
	
	void applyWeatherSound() {
		if(curWeather != befWeather) {
			mixer.weatherChannel.addCommand(new CommandAudioVolume("weather-sound-main", 0, 5000));
			startWeather("weather-sound-tmp", 0);
			mixer.weatherChannel.addCommand(new CommandAudioVolume("weather-sound-tmp", 1, 5000));
			mixer.weatherChannel.addCommand(new CommandWait(5000));
			mixer.weatherChannel.addCommand(new CommandMoveAudio("weather-sound-tmp", "weather-sound-main"));

			if(curWeather == Weather.NORMAL)
				mixer.weatherChannel.addCommand(new CommandVolume(getVolume(), 5000));
		}
		if(curWeather != Weather.NORMAL) {
			mixer.weatherChannel.addCommand(new CommandVolume(getVolume(), 5000));
		}
	}
	
	private double getVolume() {
		double volume = 1;
		if(curWeather != Weather.NORMAL) {
			volume = Math.min(Math.max(rainMMPerHour, snowMMPerHour), 5) / 5;
			//System.out.println("RAIN/SNOW VOLUME: " + volume);
		}
		return volume;
	}

	public boolean setLatLon(String lat, String lon) {
		return updater.setLatLon(lat, lon);
	}
	
	public boolean setCity(String city) {
		return updater.setCity(city);
	}
	
	public void setBellPattern(int[] bellPattern) {
		this.bellPattern = bellPattern;
	}
	
	public boolean isLocationSet() {
		return updater.isLocationSet();
	}

}
