package com.cjburkey.plugin.ar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.entity.Player;

public class DataHandler {
	
	// Times are in ticks since joining.
	private static HashMap<UUID, Long> times = new HashMap<UUID, Long>();
	
	public static final void loadDataFromFile() {
		if(IO.getStorageFile().exists()) {
			long start = System.currentTimeMillis();
			try {
				times.clear();
				FileInputStream fis = new FileInputStream(IO.getStorageFile());
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object read = ois.readObject();
				if(read != null && read instanceof HashMap) {
					HashMap<?, ?> loaded = (HashMap<?, ?>) read;
					for(Entry<?, ?> entry : loaded.entrySet()) {
						times.put(UUID.fromString((String) entry.getKey()), Long.parseLong((String) entry.getValue()));
					}
				}
				ois.close();
				fis.close();
			} catch(Exception e) {
				Util.error(e, "There was an error loading the data file.");
			}
			long timeTaken = System.currentTimeMillis() - start;
			if(BurkAR.instance.getConfig().getBoolean("extraDebugInConsole")) Util.log("Data loaded in " + timeTaken + " ms.");
		} else {
			Util.log("Data file not found, skipping loading process.");
		}
	}
	
	public static final void saveDataToFile() {
		long start = System.currentTimeMillis();
		try {
			FileOutputStream fout = new FileOutputStream(IO.getStorageFile());
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(times);
			oout.flush();
			oout.close();
			fout.close();
		} catch(Exception e) {
			Util.error(e, "There was an error saving to the data file.");
		}
		long timeTaken = System.currentTimeMillis() - start;
		if(BurkAR.instance.getConfig().getBoolean("extraDebugInConsole")) Util.log("Data saved in " + timeTaken + " ms.");
	}
	
	public static final void setTimeOfPlayerInTicks(Player ply, long timeInTicks) {
		times.put(ply.getUniqueId(), timeInTicks);
	}
	
	public static final long getTimeOfPlayerInTicks(Player ply) {
		if(times.containsKey(ply.getUniqueId())) {
			return times.get(ply.getUniqueId());
		}
		
		// Player has not joined the server yet, or file corrupted.
		return -1;
	}
	
}