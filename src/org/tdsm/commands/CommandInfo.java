package org.tdsm.commands;

import java.lang.reflect.Method;

public class CommandInfo {
	public String Name = "";
	public String Descripton = "";
	public Method Command;
	public boolean Restricted;
	
	public CommandInfo SetName(String name) {
		Name = name;
		return this;
	}
	
	public CommandInfo SetCommand(Method command) {
		Command = command;
		return this;
	}
	
	public CommandInfo SetDescripton(String descripton) {
		Descripton = descripton;
		return this;
	}
	
	public CommandInfo SetRestricted(boolean restricted) {
		Restricted = restricted;
		return this;
	}
}
