package com.cjburkey.plugin.ar;

import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class UpdateChecker {
	
	public static final String updateReady() {
		try {
			Util.log("Sending request...");
			URL url = new URL("https://api.github.com/repos/cjburkey01/BurkAR/releases/latest");
			JsonReader jread = Json.createReader(url.openStream());
			JsonObject obj = jread.readObject();
			String v = obj.getString("name").trim();
			Util.log("Got response: '" + v + "'");
			return v;
		} catch(Exception e) {
			Util.error(e, "Couldn't check for an update.");
		}
		return null;
	}
	
}