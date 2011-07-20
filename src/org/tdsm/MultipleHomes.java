package org.tdsm;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.tdsm.commands.CommandParser;


public class MultipleHomes extends JavaPlugin {

	private final mhPlayerListener playerListener = new mhPlayerListener(this);
	public CommandParser cmdParser;

	public static final Logger log = Logger.getLogger("Minecraft");
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		WritetoConsole("Disabled.");
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		cmdParser = new CommandParser();
		
		PluginManager pm = getServer().getPluginManager(); 
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Normal, this);
		
		WritetoConsole("Enabled.");
	}
	
	public void WritetoConsole(String Msg) {
		//System.out.println("[" + pdfFile.getName() + "] " + Msg);
		try {
			log.info("[MultipleHomes] " + Msg);
		} catch (Exception e) {
			
		}
	}

}
