package org.tdsm.commands;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.tdsm.MultipleHomes;
import org.tdsm.Properties;

/*
 * FUCKING CAUTION
 * 		Delegates are bloody unsafe, Make sure you catch every bloody possible 
 * 		error or the whole function will seize to function, It will not give a precise 
 * 		stack trace.
 */
public class CommandParser {

	public HashMap<String, CommandInfo> commands;
			
	public CommandParser(Properties properties) {
		commands = new HashMap<String, CommandInfo>();
		
		try {
			//Home Command
			AddCommand(properties.GetHomeCommand())
			.SetDescripton("Personalized Home Teleportation.")
			.SetRestricted(false)
			.SetHelpText("/" + properties.GetHomeCommand() + " <Number:Name>")
			.SetCommand(Commands.class.getDeclaredMethod("Home", Command.class));

			//Set Home Command
			AddCommand(properties.GetSetHomeCommand())
			.SetDescripton("Set a Personalized Home.")
			.SetRestricted(false)
			.SetHelpText("/" + properties.GetSetHomeCommand() + " <Number> \"<Name>\" <Description> (p.s. Surround Name with \"!")
			.SetCommand(Commands.class.getDeclaredMethod("SetHome", Command.class));
			
			//Delete Home Command
			AddCommand(properties.GetDeleteHomeCommand())
			.SetDescripton("Delete a Home Permanently.")
			.SetRestricted(false)
			.SetHelpText("/" + properties.GetDeleteHomeCommand() + " <Number:Name>")
			.SetCommand(Commands.class.getDeclaredMethod("DeleteHome", Command.class));
			
			//Home List Command
			AddCommand("homelist")
			.SetDescripton("Get a List of Your Current Homes")
			.SetRestricted(false)
			.SetHelpText("/homelist")
			.SetCommand(Commands.class.getDeclaredMethod("HomeList", Command.class));
			
			//MH's Help/Info Command
			AddCommand("multiplehomes")
			.SetDescripton("MultipleHomes Help & Info.")
			.SetRestricted(false)
			.SetHelpText("/multiplehomes [usage <command>]")
			.SetCommand(Commands.class.getDeclaredMethod("HelpAndInfo", Command.class));
			AddCommand("mh")
			.SetDescripton("MultipleHomes Help & Info.")
			.SetRestricted(false)
			.SetHelpText("/mh [usage <command>]")
			.SetCommand(Commands.class.getDeclaredMethod("HelpAndInfo", Command.class));
			
			//Home Invite Command
			AddCommand("homeinvite")
			.SetDescripton("Allows another Player to use a home of Your own.")
			.SetRestricted(false)
			.SetHelpText("/homeinvite <PlayerName> <Number:Name>")
			.SetCommand(Commands.class.getDeclaredMethod("HomeInvite", Command.class));
			
			//Home List Command
			AddCommand("homevisit")
			.SetDescripton("Allows another Player to use a home of Your own.")
			.SetRestricted(false)
			.SetHelpText("/homevisit <PlayerName> <Number:Name>")
			.SetCommand(Commands.class.getDeclaredMethod("HomeVisit", Command.class));
			
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
	
	public boolean ParseCommand(String CommandLine, Player player, MultipleHomes Plugin) {
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
					try {
						//Invoke the Set Command =D
						Command nCommand = new Command();
						nCommand.Player = player;
						nCommand.Arguments = Commands;
						nCommand.Plugin = Plugin;
						if(cmdInfo.Command.invoke(Commands.class.newInstance(), nCommand).getClass() != null) {
							return nCommand.Cancelled; //Did the function process correctly?
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}
}
