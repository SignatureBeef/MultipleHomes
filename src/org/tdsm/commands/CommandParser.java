package org.tdsm.commands;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class CommandParser {

	public HashMap<String, CommandInfo> commands;
			
	public CommandParser() {
		commands = new HashMap<String, CommandInfo>();
		
		try {			
			AddCommand("home")
				.SetDescripton("Personalized Home Teleportation")
				.SetRestricted(false)
				.SetCommand(Commands.class.getDeclaredMethod("Home", Command.class));
			
		} catch(Exception e) {
			
		}
	}
	
	public CommandInfo AddCommand (String prefix)
    {        
        CommandInfo cmd = new CommandInfo();
        cmd.SetName(prefix);
        commands.put(prefix, cmd);
        
        return cmd;
    }
	
	public boolean ParseCommand(String CommandLine, Player player) {
		String sentcommands = CommandLine.trim();
		if(sentcommands.startsWith("/")) {
			sentcommands = sentcommands.substring(1, sentcommands.length());
		}
		
		String[] Commands = sentcommands.split(" ");
		if(Commands[0] != null && Commands[0].length() > 0) {
			CommandInfo cmdInfo = commands.get(Commands[0]);			
			if(cmdInfo != null) {
				if(cmdInfo.Restricted) {  //&& player.hasPermission("mh." + cmdInfo.Name)) {
					player.sendMessage("Sorry, You do not have access to this feature.");
				} else {
					player.sendMessage("Sorry, You do not have access to this feature.");
					try {
						Command nCommand = new Command();
						nCommand.Player = player;
						nCommand.Arguments = Commands;
						cmdInfo.Command.invoke(Commands.class.newInstance(), nCommand);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//switch(cmdInfo.Name) {
					//	case "home": {
					//		break;
					//	}
					//}
				}
			}
		}
		return false;
	}
}
