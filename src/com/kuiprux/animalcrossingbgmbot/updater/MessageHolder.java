package com.kuiprux.animalcrossingbgmbot.updater;

import com.kuiprux.animalcrossingbgmbot.AnimalCrossingBGMBot;

import net.dv8tion.jda.api.entities.Message;

public class MessageHolder {

	Message message;
	
	public MessageHolder() {
		AnimalCrossingBGMBot.EVENT_HANDLER.register(this);
	}
	
	public boolean isAvailable() {
		return message != null;
	}
	
	public Message getMsg() {
		return message;
	}
	
	public long getMsgId() {
		return message.getIdLong();
	}
	
	public void setMsg(Message msg) {
		message = msg;
	}
	
	public void deleteMsg() {
		message.delete().queue();
	}
}
