package com.cjburkey.plugin.ar.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.cjburkey.plugin.ar.BurkAR;
import com.cjburkey.plugin.ar.Util;

public class EventPlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if(e.getPlayer().hasPermission("burkar.info")) {
			String v = BurkAR.updateAvailable;
			if(v != null) {
				Util.chat(e.getPlayer(), String.format(Util.getLangMsg("Update"), BurkAR.version, v));
			} else {
				Util.chat(e.getPlayer(), Util.getLangMsg("NoUpdate"));
			}
		}
	}
	
}