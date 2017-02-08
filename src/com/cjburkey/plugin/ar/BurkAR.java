package com.cjburkey.plugin.ar;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.cjburkey.plugin.ar.cmd.CommandBAR;
import com.cjburkey.plugin.ar.cmd.TabCompleterBAR;
import com.cjburkey.plugin.ar.data.DataHandler;

public class BurkAR extends JavaPlugin {
	
	public static final String version = "1.0.1";
	
	public static BurkAR instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		if(this.getConfig().getInt("autoSaveTimeInSeconds") > 1200) {
			this.getConfig().set("autoSaveTimeInSeconds", 1200);
		} else if(this.getConfig().getInt("autoSaveTimeInSeconds") < 1) {
			this.getConfig().set("autoSaveTimeInSeconds", 1);
		}
		if(this.getConfig().getInt("tickTimeUpdate") > 20) {
			this.getConfig().set("tickTimeUpdate", 20);
		} else if(this.getConfig().getInt("tickTimeUpdate") < 1) {
			this.getConfig().set("tickTimeUpdate", 1);
		}
		this.saveConfig();
		
		this.getCommand("bar").setExecutor(new CommandBAR());
		this.getCommand("bar").setTabCompleter(new TabCompleterBAR());
		
		DataHandler.loadDataFromFile();
		
		// Run every 5 ticks forever.
		if(getConfig().getBoolean("saveAutomatically")) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
				DataHandler.saveDataToFile();
			}, 0, this.getConfig().getInt("autoSaveTimeInSeconds") * 20);
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			DataHandler.addTicksToAllOnlinePlayers(getConfig().getInt("tickTimeUpdate"));
			DataHandler.checkPlayersForGoals();
		}, 0, this.getConfig().getInt("tickTimeUpdate"));
	}
	
	@Override
	public void onDisable() {
		DataHandler.saveDataToFile();
		
		instance = null;
	}
	
}