package org.tdsm.commands;

import java.lang.reflect.Method;

public class Commands {

	public static Method Home(Command command) {
		command.Player.sendMessage("Works?");
		return null;
	}
}
