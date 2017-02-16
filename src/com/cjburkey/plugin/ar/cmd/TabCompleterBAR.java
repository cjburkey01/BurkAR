package com.cjburkey.plugin.ar.cmd;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.cjburkey.plugin.ar.data.DataHandler;

public class TabCompleterBAR implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("bar") && args.length == 1) {
			return DataHandler.getRegisteredPlayerNames();
		}
		return null;
	}
	
}