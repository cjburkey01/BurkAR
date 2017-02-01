package com.cjburkey.plugin.ar;

import java.io.File;

public class IO {
	
	public static final File getStorageFile() {
		return new File(BurkAR.instance.getDataFolder(), "/times.dat");
	}
	
}