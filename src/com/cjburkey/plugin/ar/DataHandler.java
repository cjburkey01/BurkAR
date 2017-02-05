package com.cjburkey.plugin.ar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Player;

public class DataHandler {
	
	// Times are in ticks since joining.
	private static List<PlayerData> times = new ArrayList<PlayerData>();
	
	public static final void loadDataFromFile() {
		loadTimes();
	}
	
	private static final void loadTimes() {
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
		Collection<? extends Player> players = BurkAR.instance.getServer().getOnlinePlayers();
		for(Player p : players) {
			setTimeOfPlayerInTicks(p, ticksToAdd);
		}
	}
	
	public static final void setTimeOfPlayerInTicks(Player ply, long timeInTicks) {
		for(PlayerData data : times) {
			if(data.getPlayer().equals(ply)) {
				long toAdd = timeInTicks - data.getTicks();
				data.addTicks(toAdd);
				return;
			}
		}
		PlayerData data = new PlayerData(ply, timeInTicks, new ArrayList<Goal>());
		times.add(data);
	}
	
	public static final long getTimeOfPlayerInTicks(Player ply) {
		for(PlayerData data : times) {
			if(data.getPlayer().equals(ply)) {
				return data.getTicks();
			}
		}
		return -1;
	}
	
}