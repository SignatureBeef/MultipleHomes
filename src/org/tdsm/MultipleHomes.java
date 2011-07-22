package org.tdsm;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

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
	public Properties properties;

	public static final Logger log = Logger.getLogger("Minecraft");
	public static final String PluginFolder = "plugins/MultipleHomes/";
	public final String WorldFolder = PluginFolder + "WorldData/";
	
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
	//public HashMap<String, HashMap<String, List<Home>>> WorldPlayerData; //Changed my mind
	public HashMap<String, List<Home>> WorldPlayerData;
	
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		WritetoConsole("Disabled.");
	}
	
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		WritetoConsole("Initialzing...");
		
		SetupDirectories();
		properties = new Properties(PluginFolder + "multiplehomes.properties"); //Create/Load Properties
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		LoadData(); //This uses Worlds.
		
		cmdParser = new CommandParser(properties);
		
		PluginManager pm = getServer().getPluginManager(); 
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Highest, this); //Hopefully it will over rule Essentials
		
		WritetoConsole("Enabled.");
	}
	
	public void WritetoConsole(String Msg) {
		log.info("[MultipleHomes] " + Msg);
	}
	
	public void SetupDirectories() {
		String[] Folders = new String[] { PluginFolder, this.WorldFolder };
		for(String folder : Folders) {
			File worldDir = new File(folder);
			if(!worldDir.exists()) {
				if(!worldDir.mkdir()) {
					WritetoConsole("Issue Creating Folder (1): " + folder);
					if(!worldDir.mkdirs()) {
						WritetoConsole("Issue Creating Folder (2): " + folder);
					}
				}
			}
		}
	}
	
	public void LoadData() {
		WorldPlayerData = new HashMap<String, List<Home>>();
		
		File WorldDataFolder = new File(WorldFolder);
	    File[] PlayerHomeFileList = WorldDataFolder.listFiles();

	    for (int i = 0; i < PlayerHomeFileList.length; i++) {
	    	if (PlayerHomeFileList[i].isFile()) {
	    		if(PlayerHomeFileList[i].getName().toLowerCase().endsWith(".mhf")) {
	    			String PlayerName = PlayerHomeFileList[i].getName().substring(0, 
	    				  				PlayerHomeFileList[i].getName().length()-4).trim();

	    			System.out.println("Loading: " + PlayerHomeFileList[i].getAbsolutePath());

	    			List<Home> playerHomes = HomeManager.LoadPlayerHomes(
	    				  						PlayerHomeFileList[i].getAbsolutePath(),
	    				  						PlayerName,
	    				  						this.getServer());
	    	  
	    			if(playerHomes != null) {
	    				WorldPlayerData.put(PlayerName, playerHomes);
	    			} else {
	    				System.out.print("Error loading Player Data");
	    			}
	    		}
	    	} 
	    }
	}
	
	public static String ArrayToString(String[] Array, String Deliminator) {
		StringBuilder StringBuilder = new StringBuilder();
		for(String str : Array) {
			StringBuilder.append(Deliminator +  str);
		}
		String ReT = StringBuilder.toString().trim();
		if(ReT.length() > 0) {
			if(ReT.startsWith(Deliminator)) {
				ReT = ReT.substring(1, ReT.length());
			}
			return ReT.trim();
		}
		return null;
	}

}
