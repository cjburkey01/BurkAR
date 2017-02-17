package com.cjburkey.plugin.ar.cmd;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.cjburkey.plugin.ar.data.DataHandler;

public class TabCompleterBAR implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1) {
			List<String> peeps = new ArrayList<String>();
			for(String n : DataHandler.getRegisteredPlayerNames()) {
				if(n.startsWith(args[0])) {
					peeps.add(n);
				}
			}
			return peeps;
		}
		return DataHandler.getRegisteredPlayerNames();
	}
	
}