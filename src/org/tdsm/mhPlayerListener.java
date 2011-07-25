package org.tdsm;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.tdsm.homes.Home;
import org.tdsm.homes.HomeManager;

public class mhPlayerListener extends PlayerListener {
	private static MultipleHomes plugin;
	
	public mhPlayerListener(MultipleHomes instance) {
		plugin = instance;
	}
		
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(!plugin.isEnabled()) { return; }
		//Que Processes [TODO]
		if(plugin.cmdParser.ParseCommand(event.getMessage(), event.getPlayer(), plugin)) {
			event.setCancelled(true);
		}
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if(!plugin.isEnabled() || !plugin.properties.GetHomeOnDeath()) { return; }
		List<Home> hList = HomeManager.GetPlayerHomes(event.getPlayer().getName(), plugin.WorldPlayerData);
		if(hList != null && hList.size() > 0) {
			try {
				int DeathHome = hList.get(0).HostFile.getInt("deathhome", -1);
				if(DeathHome != -1) {
					if(DeathHome <= (hList.size()-1) && DeathHome <= plugin.properties.GetMaxHomes()) {
						for(Home home : hList) {
							if(home.HomeNumber == DeathHome) {
								event.getPlayer().teleport(home.Location);
								return;
							}
						}
					} else {
						event.getPlayer().sendMessage(ChatColor.DARK_RED + "The Death Home specified is out of bounds!");
					}
				}
			} catch(Exception e) {
				
			}
		}
	}
}
