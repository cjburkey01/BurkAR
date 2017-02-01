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
		
		DataHandler.loadDataFromFile();
	}
	
	@Override
	public void onDisable() {
		DataHandler.saveDataToFile();
		
		instance = null;
	}
	
}