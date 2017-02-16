package com.cjburkey.plugin.ar.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.cjburkey.plugin.ar.BurkAR;
import com.cjburkey.plugin.ar.Util;
import com.cjburkey.plugin.ar.data.DataHandler;
import com.cjburkey.plugin.ar.data.Goal;

public class CommandBAR implements CommandExecutor {

	public boolean onCommand(CommandSender plyr, Command cmd, String label, String[] args) {
		if(plyr instanceof Player) {
			this.player((Player) plyr, args);
		} else {
			this.console(plyr, args);
		}
		return true;
	}
	
	private void console(CommandSender console, String[] args) {
		if(args.length == 0) {
			Util.chat(console, Util.getLangMsg("Usage"));
		} else {
			if(args[0].equalsIgnoreCase("--v")) {
				Util.chat(console, String.format(Util.getLangMsg("Info"), BurkAR.version));
			} else {
				this.other(console, args[0]);
			}
		}
	}
	
	private void player(Player player, String[] args) {
		if(player.hasPermission("burkar.use")) {
			if(args.length == 0) {
				self(player);
			} else {
				if(args[0].equalsIgnoreCase("--v")) {
					if(player.hasPermission("burkar.info")) {
						Util.chat(player, String.format(Util.getLangMsg("Info"), BurkAR.version));
					} else {
						Util.chat(player, Util.getLangMsg("LackPermInfo"));
					}
				} else {
					if(player.hasPermission("burkar.others")) {
						other(player, args[0]);
					} else {
						Util.chat(player, Util.getLangMsg("LackPermOthers"));
					}
				}
			}
		} else {
			Util.chat(player, Util.getLangMsg("LackPerm"));
		}
	}
	
	private void self(Player player) {
		long time = DataHandler.getTimeOfPlayerInTicks(player.getUniqueId());
		if(time == -1) {
			Util.chat(player, Util.getLangMsg("Err"));
		} else {
			Goal next = DataHandler.nextGoal(player.getUniqueId());
			if(next != null) {
				Util.chat(player, String.format(Util.getLangMsg("Self"), Util.formatTime(time), next.toString(), Util.formatTime(next.getRequiredTicks() - time)));
			} else {
				Util.chat(player, String.format(Util.getLangMsg("SelfNone"), Util.formatTime(time)));
			}
		}
	}
	
	private void other(CommandSender sender, String name) {
		long time = DataHandler.getTimeFromPlayerName(name);
		if(time == -1) {
			Util.chat(sender, Util.getLangMsg("Err"));
		} else {
			Util.chat(sender, String.format(Util.getLangMsg("Other"), name, Util.formatTime(time)));
		}
	}
	
}