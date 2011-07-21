package org.tdsm.commands;

import java.util.ArrayList;
import java.util.List;

import org.tdsm.MultipleHomes;
import org.tdsm.homes.*;

public class Commands {

	public static boolean Home(Command command) {
		command.Player.sendMessage("Yet to implement, " + command.Player.getName());
		
		command.Cancelled = true;
		return false;
	}

	public static boolean SetHome(Command command) {
		try {
			//Command layout: /sethome <number> "<Name>" <Description>.
			if(command.Arguments != null && command.Arguments.length > 3) {
				int Number = -1;
				String Name = "";
				String Description = "";
				
				if(command.Arguments[1] != null && command.Arguments[1].trim().length() > 0) {
					//Parse Home Number
					try {
						Number = Integer.valueOf(command.Arguments[1]);
					} catch(Exception e) {
						command.Player.sendMessage("Unparsable Home Number, Please Try Again.");
						return false;
					}
				}
				if(command.Arguments[2] != null && command.Arguments[2].trim().length() > 0) {
					//Parse Home Name
					if(command.Arguments[2].contains("\"")) {
						try {
							Name = MultipleHomes.ArrayToString(command.Arguments);
							Name = Name.substring(Name.indexOf("\"") + 1, Name.length());
							Name = Name.substring(0, Name.indexOf("\"")).trim();
						} catch(Exception e) {
							command.Player.sendMessage("Please Review Your Command.");
							return false;
						}
					} else {
						command.Player.sendMessage("Please Review Your Command.");
						return false;
					}
				}
				if(command.Arguments[3] != null && command.Arguments[3].trim().length() > 0) {
					//Parse Home Description
					try {
						Description = MultipleHomes.ArrayToString(command.Arguments);
						Description = Description.substring(Description.indexOf(Name) + 
								Name.length() + 1, Description.length()).trim();
					} catch(Exception e) {
						command.Player.sendMessage("Please Review Your Command.");
						return false;
					}
				}
				
				if(Number == -1 || Name == "" || Description == "") {
					command.Player.sendMessage("Please Review Your Command.");
				} else {
					if(command.Plugin.WorldPlayerData.containsKey(command.Player.getName())) {
						List<Home> homes = 
							command.Plugin.WorldPlayerData.get(command.Player.getName());
						if(homes != null && homes.size() > 0) {
							try {
								List<Home> HomestoRemove = new ArrayList<Home>();
								for(Home home : homes) {
									if(home.Name.equals(Name) || home.HomeNumber == Number) { //If the home has the same critical details
										command.Player.sendMessage("Overwriting Old Home(s)...");
										HomestoRemove.add(home);
									}
								}
								//Remove Homes
								for(Home home : HomestoRemove) {
									if(homes.contains(home)) {
										homes.remove(home);
									}
								}
							} catch(Exception e) {
								command.Player.sendMessage("Error Removing Old Home!");
								return false;
							}
							
						}
					}
					HomeManager.AddHome(command.Player, command.Plugin.WorldPlayerData, 
								Name, Description, Number, command.Plugin.WorldFolder, true);
					command.Player.sendMessage("Your Home #" + Number + " has now been set!");
					command.Cancelled = true;
				}
			} else {
				command.Player.sendMessage("Please Review Your Command.");
			}
		} catch(Exception e) {
			command.Player.sendMessage("Error running Command.");
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return false;
	}
}
