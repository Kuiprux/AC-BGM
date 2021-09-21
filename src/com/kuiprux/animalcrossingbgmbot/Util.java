package com.kuiprux.animalcrossingbgmbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.kuiprux.animalcrossingbgmbot.music.audiocommand.CommandVolume;

public class Util {
	
	public static String APP_ID;

	public static final int AUDIO_CHANNEL = 2;
	public static final int BYTE_IN_A_FRAME = 2;
	public static final int SAMPLE_SIZE_IN_A_MILLIS = 48;
	public static final int BYTE_IN_A_MILLIS = AUDIO_CHANNEL*BYTE_IN_A_FRAME*SAMPLE_SIZE_IN_A_MILLIS;
	public static final int BYTE_IN_TWENTY_MILLIS = BYTE_IN_A_MILLIS*20;
	
	public static final int[] DEFAULT_BELL_PATTERN = new int[] 
					{2, 2, 14, -1,
					9, -1, -1, 8,
					-1, 7, -1, 5,
					-1, 2, 5, 7};
	
/*
	public static void mergeMusicBytes(ByteBuffer[] data, int interval, ByteBuffer dst) {
		mergeMusicBytes(data, new int[] {interval}, dst);
	}

	public static void mergeMusicBytes(ByteBuffer[] data, int[] intervals, ByteBuffer dst) {
		int[] addedSamples = new int[dst.capacity()/2];
		int startPoint = 0;
		for(int i = 0; i < data.length; i++) {
			startPoint += (i == 0 ? 0 : (intervals.length <= i-1 ? intervals[0] : intervals[i-1]));
			int curIndex = startPoint;
			if(startPoint*2 >= dst.capacity())
				break;
			dst.position(startPoint*2);
			data[i].position(0);
			
			while(dst.hasRemaining() && data[i].hasRemaining()) {
				addedSamples[curIndex]++;
				short tmp = dst.getShort();
				short val = (short) (((float)tmp + data[i].getShort()) / addedSamples[curIndex]);
				dst.position(dst.position()-2);
				dst.putShort((short) (val-tmp));
				curIndex++;
			}
		}
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < data.length; j++) {
				if(j >= data[j].capacity())
					break;
				System.out.print(data[j].array()[i] + "\t");
			}
			System.out.println(dst.array()[i]);
		}
	}
	
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(40);
		ByteBuffer[] data = new ByteBuffer[8];
		for(int i = 0; i < data.length; i++) {
			ByteBuffer b = ByteBuffer.allocate(100);
			while(b.hasRemaining()) {
				b.putShort((short) (i*10+10));
			}
			data[i] = b;
		}

		mergeMusicBytes(data, 2, buf);
		buf.position(0);
		while(buf.hasRemaining()) {
			System.out.println(buf.getShort());
		}
	}*/
	
	public static void main(String[] args) {
		String a = "35.8255645, 127.0965806";
		String[] b = a.split(", ");
		System.out.println(b[0]);
		System.out.println(b[1]);
		sendGet("https://api.openweathermap.org/data/2.5/weather?lat=" + b[0] + "&lon=" + b[1] + "&units=metric&appid=4edb7a7275a573efa92d57c0a9cce216");
	}

	public static String sendGet(String targetUrl) {
		try {
			URL url = new URL(targetUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch(Exception e) {
			System.err.println(e.getClass() + ": " + e.getMessage());
		}
		return null;
	}
/*
	public static void processVolume(byte[] data, CommandVolume cmd) {
		int value;
		for(int i = 0; i < data.length/4; i++) {
			//left
			value = ((data[i*2] & 0xFF) << 8) | ((data[i*2+1] & 0xFF));
			value *= cmd.getVolume(20.0*i/(data.length/4));
			data[i*2] = (byte) ((value >> 8) & 0xFF);
			data[i*2+1] = (byte) (value & 0xFF);
			//right
			value = ((data[i*2+2] & 0xFF) << 8) | ((data[i*2+3] & 0xFF));
			value *= cmd.getVolume(20.0*i/(data.length/4));
			data[i*2+2] = (byte) ((value >> 8) & 0xFF);
			data[i*2+3] = (byte) (value & 0xFF);
		}
	}*/

//TODO temp
	public static void processVolume(byte[] data, CommandVolume cmd) {
		for(int i = 0; i < data.length/4; i++) {
			for(int j = 0; j < 4; j++) {
				data[i*4+j] *= cmd.getVolume(20.0*i/(data.length/4));
			}
		}
	}
	
	
}
