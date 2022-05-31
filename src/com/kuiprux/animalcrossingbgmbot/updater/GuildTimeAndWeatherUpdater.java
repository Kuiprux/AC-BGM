package com.kuiprux.animalcrossingbgmbot.updater;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.TimeZone;

import org.json.JSONObject;

import com.kuiprux.animalcrossingbgmbot.Util;

import it.sauronsoftware.cron4j.Scheduler;

public class GuildTimeAndWeatherUpdater {

	GuildDataApplier dataApplier;
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

	public GuildTimeAndWeatherUpdater(GuildDataApplier dataApplier) {
		this.dataApplier = dataApplier;
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
					applyTime();
					loadNextMusic();
				}
			});
			timeScheduler.start();
			weatherScheduler = new Scheduler();
			weatherScheduler.setTimeZone(timezone);
			weatherScheduler.schedule("* * * * *", new Runnable() {
				public void run() {
					try {
						Thread.sleep(30*1000);
					} catch (InterruptedException e) {
					}
					updateWeather();
					dataApplier.updateImage();
					dataApplier.applyWeather();
					dataApplier.applyWeatherSound();
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
		dataApplier.init();
	}

	private void updateTime() {
		dataApplier.updateTime(getCurrentHour());
	}
	
	private void applyTime() {
		dataApplier.applyTime(); //idk if this is right
	}
	
	private void loadNextMusic() {
		dataApplier.loadNextMusic();
	}
	
	private int getCurrentHour() {
		return OffsetDateTime.now(clock).getHour();
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
		double rainMMPerHour = -1.0;
		double snowMMPerHour = -1.0;
		if (obj.has("rain")) {
			JSONObject rain = obj.getJSONObject("rain");
			rainMMPerHour = rain.getDouble("1h");
		}
		if (obj.has("snow")) {
			JSONObject snow = obj.getJSONObject("snow");
			snowMMPerHour = snow.getDouble("1h");
		}

		if (shouldUpdate)
			dataApplier.updateWeather(rainMMPerHour, snowMMPerHour);
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
			this.city = null;
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
	
	public String getLocationString() {
		if((lat == null || lon == null) && city == null)
			return null;
		
		if(city == null)
			return "위도 " + lat + ", 경도 " + lon;
		else
			return city + " 지역";
	}

}
