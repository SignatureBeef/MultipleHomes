package org.tdsm.homes;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.config.Configuration;
import org.tdsm.MultipleHomes;

public class Home {
	 //"I don't like conflicts" - Says the lazy man
	public static String Key_Seperator = "%";
	public static String Key_Location = ":";
	public static String Key_Accessers =  "#";
	
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
		String AccessersString = MultipleHomes.ArrayToString(Accessers.toArray(new String[0]), Key_Accessers);
		if(AccessersString == null || AccessersString.toLowerCase().equals("null")) {
			AccessersString = "";
		}
		String ReT = 	HomeNumber + Key_Seperator +
						(Location.getX() + Key_Location +
						 Location.getY() + Key_Location +
						 Location.getZ() + Key_Location +
						 Location.getYaw() + Key_Location +
						 Location.getPitch()).trim() + Key_Seperator +
						 Location.getWorld().getName() + Key_Seperator + 
						 AccessersString + Key_Seperator +
						 Description;
		
		return ReT.trim();
	}
}
