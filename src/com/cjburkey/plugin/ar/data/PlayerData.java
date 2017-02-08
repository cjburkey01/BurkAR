package com.cjburkey.plugin.ar.data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class PlayerData implements Serializable {
	
	private static final long serialVersionUID = -6747173209275876180L;
	
	private String plyName;
	private UUID uuid;
	private long ticks;
	private List<Goal> achievedGoals;
	
	public PlayerData(String playerName, UUID player, long ticks, List<Goal> done) {
		this.plyName = playerName;
		this.uuid = player;
		this.ticks = ticks;
		this.achievedGoals = done;
	}
	
	public String getUsername() { return this.plyName; }
	public UUID getPlayer() { return this.uuid; }
	public long getTicks() { return this.ticks; }
	public Goal[] getAchievedGoals() { return achievedGoals.toArray(new Goal[this.achievedGoals.size()]); }
	public PlayerData clone() { return new PlayerData(this.plyName, this.uuid, this.ticks, this.achievedGoals); }
	
	public void addTicks(long ticks) { this.ticks += ticks; }
	public void achieve(Goal goal) { this.achievedGoals.add(goal); }
	
}