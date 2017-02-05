package com.cjburkey.plugin.ar;

import java.util.ArrayList;
import java.util.List;

public class Goal {
	
	private String name;
	private long ticks;
	private List<String> commands;
	
	public Goal(String name, long ticks, String... cmds) {
		this.name = name;
		this.ticks = ticks;
		commands = new ArrayList<String>();
		for(String cmd : cmds) { commands.add(cmd); }
	}
	
	public String getName() { return this.name; }
	public long getRequiredTicks() { return this.ticks; }
	public String[] getCommands() { return this.commands.toArray(new String[this.commands.size()]); }
	public Goal clone() { return new Goal(this.name, this.ticks, this.getCommands()); }
	
}