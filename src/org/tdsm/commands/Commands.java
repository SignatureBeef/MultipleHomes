package org.tdsm.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.tdsm.MultipleHomes;
import org.tdsm.homes.*;

public class Commands {

	public static boolean Home(Command command) {
		try {
			//Command Layout: /home <number:name>
			if(command.Arguments != null && command.Arguments.length > 1) {
				String Name = MultipleHomes.ArrayToString(command.Arguments, " ");
				Name = Name.substring(Name.indexOf(command.Arguments[0]) + command.Arguments[0].length(),
						Name.length()).trim();
				Home home = HomeManager.GetPlayerHome(command.Player.getName(), 
						Name, command.Plugin.WorldPlayerData);
				
				if(home == null) {
					int HomeNumber = 0;
					try {
						HomeNumber = Integer.valueOf(command.Arguments[1]);
					} catch(Exception e) {
						
					}
					home = HomeManager.GetPlayerHome(command.Player.getName(), 
							HomeNumber, command.Plugin.WorldPlayerData);
				}
				
				if(home != null) {
					if(home.HomeNumber <= command.Plugin.properties.GetMaxHomes()) {
						command.Player.teleport(home.Location);
						command.Player.sendMessage(ChatColor.DARK_GREEN + "You have been Teleported to " + home.Name);
					} else {
						command.Player.sendMessage(ChatColor.DARK_RED + "Sorry, But That Home is out of Bounds.");
					}
				} else {
					command.Player.sendMessage(ChatColor.DARK_RED + "The Specified Home Name/Number '" +
							Name + "' Cannot be Located.");
				}
				
			} else {
				command.Player.sendMessage(ChatColor.DARK_RED + "Please Review Your Command.");
			}
			
		} catch(Exception e) {
			command.Player.sendMessage(ChatColor.DARK_RED + "Error running Command.");
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
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
						command.Player.sendMessage(ChatColor.DARK_RED + "Unparsable Home Number, Please Try Again.");
						return false;
					}
				}
				if(command.Arguments[2] != null && command.Arguments[2].trim().length() > 0) {
					//Parse Home Name
					if(command.Arguments[2].contains("\"")) {
						try {
							Name = MultipleHomes.ArrayToString(command.Arguments, " ");
							Name = Name.substring(Name.indexOf("\"") + 1, Name.length());
							Name = Name.substring(0, Name.indexOf("\"")).trim();
						} catch(Exception e) {
							command.Player.sendMessage(ChatColor.DARK_RED + "Please Review Your Command.");
							return false;
						}
					} else {
						command.Player.sendMessage(ChatColor.DARK_RED + "Please Review Your Command.");
						return false;
					}
				}
				if(command.Arguments[3] != null && command.Arguments[3].trim().length() > 0) {
					//Parse Home Description
					try {
						Description = MultipleHomes.ArrayToString(command.Arguments, " ");
						Description = Description.substring(Description.indexOf(Name) + 
								Name.length() + 1, Description.length()).trim();
					} catch(Exception e) {
						command.Player.sendMessage(ChatColor.DARK_RED + "Please Review Your Command.");
						return false;
					}
				}
				
				if(Number == -1 || Name == "" || Description == "") {
					command.Player.sendMessage(ChatColor.DARK_RED + "Please Review Your Command.");
				} else {
					if(Number > command.Plugin.properties.GetMaxHomes()) {
						command.Player.sendMessage(ChatColor.DARK_RED + "Sorry, But That Home is out of Bounds.");
						return false;
					}
					if(command.Plugin.WorldPlayerData.containsKey(command.Player.getName())) {
						List<Home> homes = 
							command.Plugin.WorldPlayerData.get(command.Player.getName());
						if(homes != null && homes.size() > 0) {
							try {
								List<Home> HomestoRemove = new ArrayList<Home>();
								for(Home home : homes) {
									if(home.Name.equals(Name) || home.HomeNumber == Number) { //If the home has the same critical details
										command.Player.sendMessage(ChatColor.DARK_GREEN + "Overwriting Old Home(s)...");
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
								command.Player.sendMessage(ChatColor.DARK_RED + "Error Removing Old Home!");
								return false;
							}
							
						}
					}
					HomeManager.AddHome(command.Player, command.Plugin.WorldPlayerData, 
								Name, Description, Number, command.Plugin.WorldFolder, true);
					command.Player.sendMessage(ChatColor.DARK_GREEN + "Your Home #" + Number + " has now been set!");
					command.Cancelled = true;
				}
			} else {
				command.Player.sendMessage(ChatColor.DARK_RED + "Please Review Your Command.");
			}
		} catch(Exception e) {
			command.Player.sendMessage(ChatColor.DARK_GREEN + "Error running Command.");
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean HomeList(Command command) {
		if(command.Plugin.WorldPlayerData.containsKey(command.Player.getName())) {
			StringBuilder StringBuilder = new StringBuilder();
			for(Home home : command.Plugin.WorldPlayerData.get(command.Player.getName())) {
				StringBuilder.append(", " + home.Name);
			}
			String Homes = StringBuilder.toString().trim();
			if(Homes.startsWith(",")) {
				Homes = Homes.substring(1, Homes.length()).trim();
			}
			command.Player.sendMessage(ChatColor.DARK_GREEN + "Available Homes: " + Homes);
		} else {
			command.Player.sendMessage(ChatColor.DARK_GREEN + "You have 0 Homes Configured.");
		}
		command.Cancelled = true;
		return false;
	}

	public static boolean DeleteHome(Command command) {
		try {
			//Command Layout: /deletehome <number:name>
			if(command.Arguments != null && command.Arguments.length > 1) {
				String Name = MultipleHomes.ArrayToString(command.Arguments, " ");
				Name = Name.substring(Name.indexOf(command.Arguments[0]) + command.Arguments[0].length(),
						Name.length()).trim();
				Home home = HomeManager.GetPlayerHome(command.Player.getName(), 
						Name, command.Plugin.WorldPlayerData);
				
				if(home == null) {
					int HomeNumber = 0;
					try {
						HomeNumber = Integer.valueOf(command.Arguments[1]);
					} catch(Exception e) {
						
					}
					home = HomeManager.GetPlayerHome(command.Player.getName(), 
							HomeNumber, command.Plugin.WorldPlayerData);
				}
				
				if(home != null) {
					boolean removed = false;
					while(command.Plugin.WorldPlayerData.get(command.Player.getName()).contains(home)) { //Defiantly not needed, but atm for unstable release, multiple occurrences may be possible
						try {
							command.Plugin.WorldPlayerData.get(command.Player.getName()).remove(home);
							removed = true;
						} catch(Exception e) {
							break;
						}
					}
					if(removed && HomeManager.SavePlayerHomes(command.Player.getName(), command.Plugin.WorldPlayerData, home.HostFile)) {
						command.Player.sendMessage(ChatColor.DARK_GREEN + "That Home is now Removed!");
					} else {
						command.Player.sendMessage(ChatColor.DARK_RED + "Failed to Remove Home!");
					}
				} else {
					command.Player.sendMessage(ChatColor.DARK_RED + "The Specified Home Name/Number '" +
							Name + "' Cannot be Located.");
				}
				
			} else {
				command.Player.sendMessage(ChatColor.DARK_RED + "Please Review Your Command.");
			}
			
		} catch(Exception e) {
			command.Player.sendMessage(ChatColor.DARK_RED + "Error running Command.");
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		command.Cancelled = true;
		return false;
	}

	public static boolean HelpAndInfo(Command command) {
		try {
			if(command.Arguments.length > 2 && command.Arguments[1] != null && command.Arguments[2] != null &&
				command.Arguments[1].trim().length() > 0 && command.Arguments[2].trim().length() > 0 &&
				command.Plugin.cmdParser.commands.containsKey(command.Arguments[2].trim().toLowerCase()) &&
				command.Arguments[1].trim().equals("usage")) {
				
				//Wow that was long
				command.Player.sendMessage(ChatColor.DARK_PURPLE + "Command Usage:");
				command.Player.sendMessage(ChatColor.DARK_PURPLE + "   " + command.Plugin.cmdParser.commands.get(
																			command.Arguments[2].trim().toLowerCase())
																			.HelpText);
			} else {
				command.Player.sendMessage(ChatColor.DARK_PURPLE + "MultipleHomes b" + command.Plugin.getDescription().getVersion());
				for(CommandInfo cmdInfo : command.Plugin.cmdParser.commands.values()) {
					command.Player.sendMessage(ChatColor.DARK_PURPLE + "  /" + cmdInfo.Name + " - " + cmdInfo.Descripton);
				}
			}
			command.Cancelled = true;
		} catch(Exception e) {
			command.Player.sendMessage(ChatColor.DARK_RED + "Error running Command.");
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return true;
	}
}
