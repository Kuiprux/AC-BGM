package com.kuiprux.animalcrossingbgmbot.updater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import com.kuiprux.animalcrossingbgmbot.Util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildMessageManager {

	TextChannel channel;
	MessageHolder msgHolder;
	
	MessageEmbed lastMsg;
	
	private static final Font DATA_FONT = new Font("D2Coding ligature", Font.PLAIN, 10);

	public GuildMessageManager(Guild guild) {
		List<TextChannel> chnls = guild.getTextChannelsByName(Util.CHANNEL_NAME, false);

		if (chnls.size() > 0)
			this.channel = chnls.get(0);
		else
			guild.createTextChannel(Util.CHANNEL_NAME).queue(channel -> this.channel = channel);
		msgHolder = new MessageHolder();
	}

	public boolean updateChannel(TextChannel channel) {
		if(this.channel.equals(channel))
			return false;
		
		this.channel = channel;
		if(msgHolder.isAvailable()) {
			msgHolder.deleteMsg();
			channel.sendMessage(lastMsg).queue(msg -> msgHolder.setMsg(msg));//TODO what if lastMsg == null?
		}
		
		return true;
	}

	public void updateMsg(int hour, double rainMMPerHour, double snowMMPerHour) {
		
	}

	public void updateMsgContent(MessageEmbed newMsg) {
		if (msgHolder.isAvailable()) {
			msgHolder.getMsg().editMessage(newMsg).queue();
		} else {
			channel.sendMessage(newMsg).queue(msg -> msgHolder.setMsg(msg));
		}
	}

	public MessageEmbed getEmbedMessage() {
return null;//TODO
	}
	
	
	
	
	
	
	
	/*
	 File path = ... // base path of the images

// load source images
BufferedImage image = ImageIO.read(new File(path, "image.png"));
BufferedImage overlay = ImageIO.read(new File(path, "overlay.png"));

// create the new image, canvas size is the max. of both image sizes
int w = Math.max(image.getWidth(), overlay.getWidth());
int h = Math.max(image.getHeight(), overlay.getHeight());
BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

// paint both images, preserving the alpha channels
Graphics g = combined.getGraphics();
g.drawImage(image, 0, 0, null);
g.drawImage(overlay, 0, 0, null);

g.dispose();

// Save as new image
ImageIO.write(combined, "PNG", new File(path, "combined.png"));
	 */
	private static BufferedImage processImage() throws Exception {
		BufferedImage img = ImageIO.read(new File(Util.BACKGROUND_PATH));
		Graphics g = img.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g.setFont(DATA_FONT);
		g2.setColor(Color.BLACK);
		int rightX = img.getWidth()-Util.FRAME_GAP-Util.ICON_GAP;
		int upY = Util.FRAME_GAP;
		drawImage(g, ImageIO.read(new File(Util.TEST_PATH)), rightX, upY);
		drawString(g, "1023hPa", rightX-Util.ICON_SIZE/2, upY+Util.ICON_SIZE+Util.TEXT_OFFSET+g.getFontMetrics().getHeight());
		
		return img;

	}
	
	private static void drawItem(Graphics g, BufferedImage icon, String text) {
		
	}
	
	private static void drawImage(Graphics g, BufferedImage img, int rightX, int upY) {
		g.drawImage(img, rightX-Util.ICON_SIZE, upY, Util.ICON_SIZE, Util.ICON_SIZE, null);
	}
	
	private static void drawString(Graphics g, String str, int centerX, int upY) {
		int width = g.getFontMetrics().stringWidth(str);
		g.drawString(str, centerX-width/2, upY);
	}
	
	public static void main(String[] args) throws Exception {
		ImageIO.write(processImage(), "png", new File("sans.png"));
	}

}
