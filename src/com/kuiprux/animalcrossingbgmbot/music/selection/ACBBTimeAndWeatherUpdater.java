package com.kuiprux.animalcrossingbgmbot.music.selection;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

import org.json.JSONObject;

import com.kuiprux.animalcrossingbgmbot.Util;

import it.sauronsoftware.cron4j.Scheduler;

public class ACBBTimeAndWeatherUpdater {

	ACBBMusicSelectionManager selectionManager;
	Scheduler timeScheduler;
	Scheduler weatherScheduler;
	
	boolean isEnabled;

	String appid;
	String lat;
	String lon;
	String city;
	
	TimeZone timezone = null;
	Clock clock;
	//TODO apply timezone
	
	boolean isLocationSet = false;

	public ACBBTimeAndWeatherUpdater(ACBBMusicSelectionManager selectionManager) {
		this.selectionManager = selectionManager;
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		if(isEnabled) {
			new Thread() {
				public void run() {
					updateTime();
					updateWeather();
					init();
				}
			}.start();
			
			timeScheduler = new Scheduler();
			timeScheduler.setTimeZone(timezone);
			timeScheduler.schedule("0 * * * *", new Runnable() {
				public void run() {
					updateTime();
					updateWeather();
					selectionManager.applyTime();
					selectionManager.applyWeatherSound();
				}
			});
			timeScheduler.start();
			weatherScheduler = new Scheduler();
			weatherScheduler.setTimeZone(timezone);
			weatherScheduler.schedule("1-59 * * * *", new Runnable() {
				public void run() {
					updateWeather();
					selectionManager.applyWeather();
					selectionManager.applyWeatherSound();
				}
			});
			weatherScheduler.start();
		} else {
			timeScheduler.stop();
			weatherScheduler.stop();
			timezone = null;
		}
	}
	
	private void init() {
		selectionManager.init();
	}

	private void updateTime() {
		selectionManager.updateTime(OffsetDateTime.now(clock).getHour());
	}
	
	private boolean updateWeather() {
		if((lat == null || lon == null) && city == null)
			return false;
		
		if(city == null)
			return updateWeather(lat, lon);
		else
			return updateWeather(city);
	}

	private boolean updateWeather(String lat, String lon) {
		return updateWeather(lat, lon, true);
	}
	private boolean updateWeather(String lat, String lon, boolean shouldUpdate) {
		return updateWeatherUrl("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + Util.APP_ID, shouldUpdate);
	}

	private boolean updateWeather(String city) {
		return updateWeather(city, true);
	}
	private boolean updateWeather(String city, boolean shouldUpdate) {
		return updateWeatherUrl("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + Util.APP_ID, shouldUpdate);
	}
	
	private boolean updateWeatherUrl(String url, boolean shouldUpdate) {
		String data = Util.sendGet(url);
		if (data == null)
			return false;
		JSONObject obj = new JSONObject(data);
		double rainMMPerHour = 0.0;
		double snowMMPerHour = 0.0;
		if (obj.has("rain")) {
			JSONObject rain = obj.getJSONObject("rain");
			rainMMPerHour = rain.getDouble("1h");
		}
		if (obj.has("snow")) {
			JSONObject snow = obj.getJSONObject("snow");
			snowMMPerHour = snow.getDouble("1h");
		}

		if (shouldUpdate)
			selectionManager.updateWeather(rainMMPerHour, snowMMPerHour);
		if (timezone == null) {
			timezone = TimeZone.getDefault();
			timezone.setRawOffset(obj.getInt("timezone"));
			clock = Clock.offset(Clock.systemUTC(), Duration.ofSeconds(timezone.getRawOffset()));
		}
		return true;
	}

	public boolean setLatLon(String lat, String lon) {
		TimeZone befTimezone = timezone;
		Clock befClock = clock;
		timezone = null;
		clock = null;
		if(updateWeather(lat, lon, false)) {
			this.lat = lat;
			this.lon = lon;
			isLocationSet = true;
			return true;
		}
		timezone = befTimezone;
		clock = befClock;
		return false;
	}
	
	public boolean setCity(String city) {
		TimeZone befTimezone = timezone;
		Clock befClock = clock;
		timezone = null;
		clock = null;
		if(updateWeather(city, false)) {
			this.city = city;
			isLocationSet = true;
			return true;
		}
		timezone = befTimezone;
		clock = befClock;
		return false;
	}
	
	public boolean isLocationSet() {
		return isLocationSet;
	}

}
