package com.cjburkey.plugin.ar.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.cjburkey.plugin.ar.BurkAR;
import com.cjburkey.plugin.ar.IO;
import com.cjburkey.plugin.ar.Util;

public class DataHandler {
	
	// Times are in ticks since joining.
	private static List<PlayerData> times = new ArrayList<PlayerData>();
	private static List<Goal> goals = new ArrayList<Goal>();
	
	public static final void loadDataFromFile() {
		loadTimes();
		loadGoals();
	}
	
	private static final void loadTimes() {
		times.clear();
		if(IO.getTimeDataFile().exists()) {
			long start = System.currentTimeMillis();
			try {
				times.clear();
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(IO.getTimeDataFile()));
				Object read = ois.readObject();
				if(read != null && read instanceof List) {
					List<?> loaded = (List<?>) read;
					for(Object item : loaded) {
						times.add((PlayerData) item);
					}
				}
				ois.close();
			} catch(Exception e) {
				Util.error(e, "There was an error loading the data file.");
			}
			long timeTaken = System.currentTimeMillis() - start;
			if(BurkAR.instance.getConfig().getBoolean("extraDebugInConsole")) Util.log("Data loaded in " + timeTaken + " ms.");
		} else {
			Util.log("Data file not found, skipping loading process.");
		}
	}
	
	private static final void loadGoals() {
		goals.clear();
		try {
			if(IO.getTimeDataFile().exists()) {
				List<String> lines = new ArrayList<String>();
				BufferedReader reader = new BufferedReader(new FileReader(IO.getGoalDataFile()));
				String ln = null;
				while((ln = reader.readLine()) != null) {
					if(!ln.startsWith("#")) {
						lines.add(ln);
					}
				}
				reader.close();
				for(String line : lines) {
					String[] args = line.split(";");
					if(args.length == 3) {
						String name = args[0];
						long ticks = Long.parseLong(args[1]);
						List<String> cmds = new ArrayList<String>();
						Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(args[2]);
						while(m.find()) {
							cmds.add(m.group(1));
						}
						Goal g = new Goal(name, ticks, cmds);
						goals.add(g);
					}
				}
			} else {
				IO.getGoalDataFile().createNewFile();
			}
		} catch(Exception e) {
			Util.error(e, "There was an error loading the goals file.");
		}
	}
	
	public static final void saveDataToFile() {
		long start = System.currentTimeMillis();
		try {
			ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(IO.getTimeDataFile()));
			oout.writeObject(times);
			oout.flush();
			oout.close();
		} catch(Exception e) {
			Util.error(e, "There was an error saving to the data file.");
		}
		long timeTaken = System.currentTimeMillis() - start;
		if(BurkAR.instance.getConfig().getBoolean("extraDebugInConsole")) Util.log("Data saved in " + timeTaken + " ms.");
	}
	
	public static final void addTicksToAllOnlinePlayers(long ticksToAdd) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			long def = getTimeOfPlayerInTicks(p.getUniqueId());
			setTimeOfPlayerInTicks(p.getUniqueId(), ((def == -1) ? 0 : def) + ticksToAdd);
		}
	}
	
	public static final void setTimeOfPlayerInTicks(UUID id, long timeInTicks) {
		for(PlayerData data : times) {
			if(data.getPlayer().equals(id)) {
				long toAdd = timeInTicks - data.getTicks();
				data.addTicks(toAdd);
				return;
			}
		}
		PlayerData data = new PlayerData(Bukkit.getOfflinePlayer(id).getName(), id, timeInTicks, new ArrayList<Goal>());
		times.add(data);
	}
	
	public static final long getTimeOfPlayerInTicks(UUID id) {
		for(PlayerData data : times) {
			if(data.getPlayer().equals(id)) {
				return data.getTicks();
			}
		}
		return -1;
	}
	
	public static final long getTimeFromPlayerName(String name) {
		for(PlayerData dat : times) {
			if(dat.getUsername().equals(name)) {
				return dat.getTicks();
			}
		}
		return -1;
	}
	
	public static final List<String> getRegisteredPlayerNames() {
		List<String> out = new ArrayList<String>();
		for(PlayerData dat : times) {
			out.add(dat.getUsername());
		}
		return out;
	}
	
}