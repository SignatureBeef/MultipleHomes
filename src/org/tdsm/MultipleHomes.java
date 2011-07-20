package org.tdsm;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.tdsm.commands.CommandParser;
import org.tdsm.homes.Home;
import org.tdsm.homes.HomeManager;


public class MultipleHomes extends JavaPlugin {

	private final mhPlayerListener playerListener = new mhPlayerListener(this);
	public CommandParser cmdParser;

	public static final Logger log = Logger.getLogger("Minecraft");
	public static final String PluginFolder = "plugins/MultipleHomes/";
	public final String WorldFolder = PluginFolder + "Worlds/";
	
	/*
	 *	HashMap Layout:
	 *		 	- 						String [End]
	 *		< 							HashMap<String, HashMap<String, List<Home>>>
	 *		  \		- 					String [End]
	 *		 	<						HashMap<String, List<Home>>
	 *			  \
	 *				< 					List of Home [End]
	 * 
	 * (non-Javadoc)
	 * @see org.tdsm.MultipleHomes#worldPlayerData
	 */
	public HashMap<String, HashMap<String, List<Home>>> WorldPlayerData;
	
	
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
	
	public void SetupDirectories() {
		File worldDir = new File(this.WorldFolder);
		if(!worldDir.exists()) {
			if(!worldDir.mkdir()) {
				WritetoConsole("Issue Creating Worlds Folder (1)");
				if(!worldDir.mkdirs()) {
					WritetoConsole("Issue Creating Worlds Folder (2)");
				}
			}
		}
		for(World world : this.getServer().getWorlds()) {
			HashMap<String, List<Home>> worldData = HomeManager.LoadWorldData(WorldFolder + world.getName() + "/");
			if(worldData != null) {
				WorldPlayerData.put(world.getName(), worldData);
			}
		}
	}
	
	public void LoadData() {
		WorldPlayerData = new HashMap<String, HashMap<String, List<Home>>>();
	
	}

}
