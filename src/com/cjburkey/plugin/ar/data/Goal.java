package com.cjburkey.plugin.ar.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Goal implements Serializable {
	
	private static final long serialVersionUID = -2671348811398745125L;
	
	private String name;
	private long ticks;
	private List<String> commands;
	private String msgServer;
	private String msgClient;
	
	public Goal(String name, long ticks, List<String> cmds, String svr, String cl) {
		this.name = name;
		this.ticks = ticks;
		commands = new ArrayList<String>();
		for(String cmd : cmds) { commands.add(cmd); }
		this.msgServer = svr;
		this.msgClient = cl;
	}
	
	public String getName() { return this.name; }
	public long getRequiredTicks() { return this.ticks; }
	public String[] getCommands() { return this.commands.toArray(new String[this.commands.size()]); }
	public String getServerMessage() { return this.msgServer; }
	public String getClientMessage() { return this.msgClient; }
	
	public Goal clone() { return new Goal(this.name, this.ticks, this.commands, this.msgServer, this.msgClient); }
	public boolean equals(Object other) {
		if(other instanceof Goal) {
			Goal otro = (Goal) other;
			return otro.name.equals(this.name) && otro.ticks == this.ticks;
		}
		return false;
	}
	
}