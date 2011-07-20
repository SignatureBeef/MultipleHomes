package org.tdsm.commands;

import org.tdsm.homes.HomeManager;

public class Commands {

	public static boolean Home(Command command) {
		command.Player.sendMessage("Yet to implement, " + command.Player.getName());
		return false;
	}
	public static boolean SetHome(Command command) {
		try {
			HomeManager.AddHome(command.Player, command.Plugin.WorldPlayerData, 
					"Test", "test description", command.Plugin.WorldFolder, true);
		} catch(Exception e) {
			command.Player.sendMessage("Error running Command.");
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return false;
	}
}
