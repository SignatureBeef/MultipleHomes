package org.tdsm.homes;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class Home {
	public String Name = "";
	public org.bukkit.Location Location;
	public String Description = "";
	public String Owner = "";
	public List<Player> Accessers;
	public Configuration HostFile;
	public String WorldName = "";
}
