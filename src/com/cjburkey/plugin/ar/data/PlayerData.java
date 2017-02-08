package com.cjburkey.plugin.ar.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import com.cjburkey.plugin.ar.Util;

public class PlayerData implements Serializable {
	
	private static final long serialVersionUID = -6747173209275876180L;
	
	private String plyName;
	private UUID uuid;
	private long ticks;
	private HashMap<String, Long> achievedGoals;
	
	public PlayerData(String playerName, UUID player, long ticks, HashMap<String, Long> done) {
		this.plyName = playerName;
		this.uuid = player;
		this.ticks = ticks;
		this.achievedGoals = done;
	}
	
	public String getUsername() { return this.plyName; }
	public UUID getPlayer() { return this.uuid; }
	public long getTicks() { return this.ticks; }
	public Set<Entry<String, Long>> getAchievedGoals() { return achievedGoals.entrySet(); }
	public boolean hasAchieved(Goal g) { return achievedGoals.containsKey(g.getName()) && achievedGoals.get(g.getName()) == g.getRequiredTicks(); }
	public PlayerData clone() { return new PlayerData(this.plyName, this.uuid, this.ticks, this.achievedGoals); }
	
	public void addTicks(long ticks) { this.ticks += ticks; }
	public void achieve(Goal goal) {
		this.achievedGoals.put(goal.getName(), goal.getRequiredTicks());
		if(!Bukkit.getPlayer(this.uuid).hasPermission("burkar.ignore")) {
			for(String cmd : goal.getCommands()) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%p%", this.plyName));
			}
			
			if(!goal.getServerMessage().isEmpty()) Util.broadcast(goal.getServerMessage().replaceAll("%p%", plyName));
			if(!goal.getClientMessage().isEmpty()) Util.chat(Bukkit.getPlayer(this.uuid), goal.getClientMessage().replaceAll("%p%", plyName));
		}
	}
	
}