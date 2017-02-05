package com.cjburkey.plugin.ar;

import java.io.File;

public class IO {
	
	public static final File getTimeDataFile() {
		return new File(BurkAR.instance.getDataFolder(), "/times.dat");
	}
	
	public static final File getGoalDataFile() {
		return new File(BurkAR.instance.getDataFolder(), "/goals.txt");
	}
	
}