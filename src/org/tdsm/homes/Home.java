package org.tdsm.homes;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.config.Configuration;

public class Home {
	public String Key_Seperator = "&*&";
	public String Key_Location = "&*&";
	public String Key_Accessers =  ":*:";
	
	public String Name = "";
	public Integer HomeNumber;
	public org.bukkit.Location Location;
	public String Description = "";
	public String Owner = "";
	public List<String> Accessers;
	public Configuration HostFile;
	
	public Home() {
		Accessers = new ArrayList<String>();
		HomeNumber = 0;
	}
	
	public String toFormattedString() {
		String AccessersString = "";
		for(String player : Accessers) {
			if(player.trim().length() > 0) {
				AccessersString += Key_Accessers + player;
			}
		}
		if(AccessersString.startsWith(Key_Accessers)) {
			AccessersString = AccessersString.substring(1, AccessersString.length());
		}
		String ReT = 	//Name + Key_Seperator + For our saving method, Name isn't needed :3
						HomeNumber + Key_Seperator +
						(Location.getX() + ":*:" +
						 Location.getY() + ":*:" +
						 Location.getZ() + ":*:" +
						 Location.getYaw() + ":*:" +
						 Location.getPitch() + ":*:" +
						 Location.getDirection()).trim() + ":*:" +
						 Location.getWorld().getName() + Key_Seperator + 
						 AccessersString + Key_Seperator +
						 Description;
		
		return ReT.trim();
	}
}
