package com.cjburkey.plugin.ar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {
	
	public static final void log(Object msg) {
		Bukkit.getServer().getConsoleSender().sendMessage("[" + BurkAR.instance.getName() + "] " + color("" + msg));
	}
	
	public static final String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static final void error(Throwable e, String msg) {
		log("&4&l" + msg);
		log("&4&l  " + e.getMessage());
		e.printStackTrace();
	}
	
	public static final void chat(CommandSender to, String msg) {
		to.sendMessage(color(msg));
	}
	
	public static final String formatTime(long ticks) {
		long seconds = ticks / 20l;
	    String positive = String.format(getLangMsg("TimeFormat"), seconds / 3600, (seconds % 3600) / 60, seconds % 60);
	    return seconds < 0 ? "-" + positive : positive;
	}
	
	public static final String getLangMsg(String title) {
		return BurkAR.instance.getConfig().getString("lang" + title);
	}
	
}