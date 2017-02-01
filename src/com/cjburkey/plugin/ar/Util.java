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
	
}