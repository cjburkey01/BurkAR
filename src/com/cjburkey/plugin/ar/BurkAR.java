package com.cjburkey.plugin.ar;

import org.bukkit.plugin.java.JavaPlugin;

public class BurkAR extends JavaPlugin {
	
	public static BurkAR instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		if(getConfig().getInt("autoSaveTimeInSeconds") > 1200) {
			getConfig().set("autoSaveTimeInSeconds", 1200);
		} else if(getConfig().getInt("autoSaveTimeInSeconds") < 1) {
			getConfig().set("autoSaveTimeInSeconds", 1);
		}
		if(getConfig().getInt("tickTimeUpdate") > 20) {
			getConfig().set("tickTimeUpdate", 20);
		} else if(getConfig().getInt("tickTimeUpdate") < 1) {
			getConfig().set("tickTimeUpdate", 1);
		}
		saveConfig();
		
		DataHandler.loadDataFromFile();
		
		// Run every 5 ticks forever.
		if(getConfig().getBoolean("saveAutomatically")) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
				DataHandler.saveDataToFile();
			}, 0, getConfig().getInt("autoSaveTimeInSeconds") * 20);
		}
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
			DataHandler.addTicksToAllOnlinePlayers(getConfig().getInt("tickTimeUpdate"));
		}, 0, getConfig().getInt("tickTimeUpdate"));
	}
	
	@Override
	public void onDisable() {
		DataHandler.saveDataToFile();
		
		instance = null;
	}
	
}