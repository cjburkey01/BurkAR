package com.cjburkey.plugin.ar;

import java.io.Serializable;
import java.util.List;
import org.bukkit.entity.Player;

public class PlayerData implements Serializable {
	
	private static final long serialVersionUID = -6747173209275876180L;
	
	private Player player;
	private long ticks;
	private List<Goal> achievedGoals;
	
	public PlayerData(Player player, long ticks, List<Goal> done) {
		this.player = player;
		this.ticks = ticks;
		this.achievedGoals = done;
	}
	
	public Player getPlayer() { return this.player; }
	public long getTicks() { return this.ticks; }
	public Goal[] getAchievedGoals() { return achievedGoals.toArray(new Goal[this.achievedGoals.size()]); }
	public PlayerData clone() { return new PlayerData(this.player, this.ticks, this.achievedGoals); }
	
	public void addTicks(long ticks) { this.ticks += ticks; }
	public void achieve(Goal goal) { this.achievedGoals.add(goal); }
	
}