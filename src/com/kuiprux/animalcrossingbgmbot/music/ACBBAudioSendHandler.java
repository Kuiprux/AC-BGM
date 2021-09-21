package com.kuiprux.animalcrossingbgmbot.music;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.kuiprux.animalcrossingbgmbot.Util;

import net.dv8tion.jda.api.audio.AudioSendHandler;

/**
 * This is a wrapper around AudioPlayer which makes it behave as an
 * AudioSendHandler for JDA. As JDA calls canProvide before every call to
 * provide20MsAudio(), we pull the frame in canProvide() and use the frame we
 * already pulled in provide20MsAudio().
 */
public class ACBBAudioSendHandler implements AudioSendHandler {

	ACBBMixer mixer;
	private final ByteBuffer buffer;
	
	public ACBBAudioSendHandler(ACBBMixer mixer) {
		this.mixer = mixer;
		this.buffer = ByteBuffer.allocate(Util.BYTE_IN_TWENTY_MILLIS);
		this.buffer.order(ByteOrder.BIG_ENDIAN);
	}

	@Override
	public boolean canProvide() {
		return mixer.isPlaying();
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		mixer.next20MsAudio(buffer);
		return buffer.flip();
	}

	@Override
	public boolean isOpus() {
		return false;
	}
}
