package org.tdsm;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class mhPlayerListener extends PlayerListener {
	private static MultipleHomes plugin;
	
	public mhPlayerListener(MultipleHomes instance) {
		plugin = instance;
	}
		
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(!plugin.isEnabled()) { return; }
		//Que Processes
		plugin.cmdParser.ParseCommand(event.getMessage(), event.getPlayer());
	}
}
