package com.cjburkey.plugin.ar.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
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
			if(IO.getGoalDataFile().exists()) {
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
					if(args.length == 5) {
						String name = args[0].trim();
						long ticks = Long.parseLong(args[1].trim());
						List<String> cmds = new ArrayList<String>();
						Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(args[2].trim());
						while(m.find()) {
							String toAdd = m.group(1);
							cmds.add(toAdd.substring(1, toAdd.length() - 1));
						}
						Goal g = new Goal(name, ticks, cmds, args[3].trim(), args[4].trim());
						goals.add(g);
					}
				}
			} else {
				IO.getGoalDataFile().createNewFile();
				try {
					FileWriter writer = new FileWriter(IO.getGoalDataFile());
					writer.write("# Format: NAME;TIME_IN_TICKS;\"command one\" \"command two\" \"etc\";SERVER_MSG;PLAYER_MSG");
					writer.close();
				} catch(Exception e) {
					Util.error(e, "There was an error creating the default goals file.");
				}
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
		PlayerData dat = getData(id);
		if(dat != null) {
			long toAdd = timeInTicks - dat.getTicks();
			dat.addTicks(toAdd);
			return;
		}
		PlayerData data = new PlayerData(Bukkit.getOfflinePlayer(id).getName(), id, timeInTicks, new HashMap<String, Long>());
		times.add(data);
	}
	
	public static final long getTimeOfPlayerInTicks(UUID id) {
		PlayerData dat = getData(id);
		if(dat != null) {
			return dat.getTicks();
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
	
	public static final boolean hasAchieved(UUID ply, Goal ach) {
		PlayerData dat = getData(ply);
		if(dat != null) {
			return dat.hasAchieved(ach);
		}
		return false;
	}
	
	public static final PlayerData getData(UUID ply) {
		for(PlayerData dat : times) {
			if(dat.getPlayer().equals(ply)) {
				return dat;
			}
		}
		return null;
	}
	
	public static final void checkPlayersForGoals() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			for(Goal g : goals) {
				if(getTimeOfPlayerInTicks(p.getUniqueId()) >= g.getRequiredTicks()) {
					if(!hasAchieved(p.getUniqueId(), g)) {
						PlayerData dat = getData(p.getUniqueId());
						if(dat != null) {
							dat.achieve(g);
						}
					}
				}
			}
		}
	}
	
	public static final Goal hashToGoal(Entry<String, Long> ent) {
		for(Goal g : goals) {
			if(g.getName().equals(ent.getKey()) && g.getRequiredTicks() == ent.getValue()) {
				return g;
			}
		}
		return null;
	}
	
	public static final Goal nextGoal(UUID ply) {
		PlayerData dat = getData(ply);
		if(dat.getTicks() > -1) {
			Set<Entry<String, Long>> achieved = dat.getAchievedGoals();
			List<Goal> ached = new ArrayList<Goal>();
			for(Entry<String, Long> ach : achieved) {
				Goal g = hashToGoal(ach);
				if(g != null) {
					ached.add(g);
				}
			}
			ached.sort(Comparator.comparing(Goal::getRequiredTicks));
			if(ached.size() >= goals.size()) { return null; }
			return goals.get(ached.size());
		}
		return null;
	}
	
}