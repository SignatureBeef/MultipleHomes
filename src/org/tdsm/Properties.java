package org.tdsm;

import java.io.File;

import org.bukkit.util.config.Configuration;

public class Properties {
	
	public Configuration config;

	public static String HOME_COMMAND = "homecommand";
	public static String SETHOME_COMMAND = "setcommand";
	public static String DELETEHOME_COMMAND = "deletecommand";
	public static String MAX_HOMES = "maxhomes";
	public static String HOME_ONDEATH = "true";
	
	public Properties(String FilePath) {
		config = new Configuration(new File(FilePath));
		PushData();
	}
	
	public void PushData() {
		config.setProperty(HOME_COMMAND, GetHomeCommand());
		config.setProperty(SETHOME_COMMAND, GetSetHomeCommand());
		config.setProperty(DELETEHOME_COMMAND, GetDeleteHomeCommand());
		config.setProperty(MAX_HOMES, GetMaxHomes());
		config.setProperty(HOME_ONDEATH, GetHomeOnDeath());
		config.save();
	}
	
	public String GetHomeCommand() {
		return config.getString(HOME_COMMAND, "home");
	}
	
	public String GetSetHomeCommand() {
		return config.getString(SETHOME_COMMAND, "sethome");
	}
	
	public String GetDeleteHomeCommand() {
		return config.getString(DELETEHOME_COMMAND, "deletehome");
	}
	
	public Integer GetMaxHomes() {
		return config.getInt(MAX_HOMES, 15);
	}
	
	public Boolean GetHomeOnDeath() {
		return config.getBoolean(HOME_ONDEATH, true);
	}
}
