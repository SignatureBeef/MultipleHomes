package org.tdsm.homes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
import org.tdsm.MultipleHomes;

public class HomeManager {
	
	/*
	 * Configuration Keys:
	 * 		homenames	- A list of home names within the config
	 * 		<homename>	- Contains the data of the home
	 */
	
	public static boolean SavePlayerHomes(String Player, HashMap<String, List<Home>> WorldData, Configuration config) {
		List<String> HomeNames = new ArrayList<String>();
		Configuration dataFile = null;
		if(WorldData.containsKey(Player)) {
			List<Home> Homes = WorldData.get(Player);
			if(Homes.size() > 0) {
				dataFile = Homes.get(0).HostFile;
				for(Home home : Homes) {
					if(!HomeNames.contains(home.Name)) {
						HomeNames.add(home.Name);
					}
					
					dataFile.setProperty(home.Name, home.toFormattedString());
				}
			} else {
				
			}
		}
		if(dataFile == null) {
			dataFile = config;
		}
		if(dataFile != null) {
			dataFile.setProperty("homenames", MultipleHomes.ArrayToString(HomeNames.toArray(new String[0]), "!"));
			return dataFile.save();
		}
		return false;
	}

	public static List<Home> LoadPlayerHomes(String PlayerDataFile, String Owner, Server server) { //TODO
		Configuration configuration = new Configuration(new File(PlayerDataFile));
		configuration.load();
		configuration.save(); //Push Data
		
		/*	0 - 1
		 * 	1 - &*&-9.538771567363128:*:85.0:*:2.670170053741026:*:-138.29997:*:24.749989
		 * 	2 - &*&world
		 * 	3 - &*&
		 * 	4 - &*&a
		 * 
		 */

		List<Home> HomeList = new ArrayList<Home>();
		String[] HomeNames = configuration.getString("homenames", "").split("!");
		
		for(String HomeName : HomeNames) {
			String homeString = configuration.getString(HomeName, "");
			if(homeString != "") {
				String[] Parts = homeString.split(Home.Key_Seperator);
				if(Parts != null && Parts.length > 3) { //4 Parts to this, location, world etc.
					int HomeNumber = -1;
					Location HomeLocation = null;
					World HomeWorld = null;
					List<String> Accessers = new ArrayList<String>();
					String Description = "";

					if(Parts[0] != null && Parts[0].trim().length() > 0) {
						try {
							HomeNumber = Integer.valueOf(Parts[0]);
						} catch(Exception e) {
						}
					}
					//Get World
					if(Parts[2] != null && Parts[2].trim().length() > 0) {
						try {
							HomeWorld = server.getWorld(Parts[2]);
							if(HomeWorld == null) {
								System.out.println("Incorrect world name!");
							}
						} catch(Exception e) {
							
						}
					}
					//Determine Accessers
					if(Parts[3] != null && Parts[3].trim().length() > 0) {
						try {
							String[] AccessersArray = Parts[3].split(Home.Key_Accessers);
							for(String player : AccessersArray) {
								if(player.trim().length() > 0) {
									Accessers.add(player.trim());
								}
							}
						} catch(Exception e) {
							
						}
					}
					//Get Description
					if(Parts[4] != null && Parts[4].trim().length() > 0) {
						try {
							Description = MultipleHomes.ArrayToString(Parts, " ");
							Description = Description.substring(Description.lastIndexOf(Parts[4]), Description.length());
						} catch(Exception e) {
							
						}
					}
					
					//Do Location
					if(Parts[1] != null && Parts[1].trim().length() > 0) {
						try {
							String[] LocationParts = Parts[1].split(Home.Key_Location);
							if(LocationParts != null && LocationParts.length > 4) {
								double X = 0;
								double Y = 0;
								double Z = 0;
								float Yaw = 0f;
								float Pitch = 0f;
								
								//Do X
								if(LocationParts[0] != null && LocationParts[0].trim().length() > 0) {
									try {
										X = Double.valueOf(LocationParts[0]);
									} catch(Exception e) {
										
									}
								}
								
								//Do Y
								if(LocationParts[1] != null && LocationParts[1].trim().length() > 0) {
									try {
										Y = Double.valueOf(LocationParts[1]);
									} catch(Exception e) {
										
									}
								}
								
								//Do Y
								if(LocationParts[2] != null && LocationParts[2].trim().length() > 0) {
									try {
										Z = Double.valueOf(LocationParts[2]);
									} catch(Exception e) {
										
									}
								}
								
								//Do Yaw
								if(LocationParts[3] != null && LocationParts[3].trim().length() > 0) {
									try {
										Pitch = Float.valueOf(LocationParts[3]);
									} catch(Exception e) {
										
									}
								}
								
								//Do Pitch
								if(LocationParts[4] != null && LocationParts[4].trim().length() > 0) {
									try {
										Yaw = Float.valueOf(LocationParts[4]);
									} catch(Exception e) {
										
									}
								}
								
								//if(X != -1 && Y != -1 && Z != -1 && Yaw != -1f && Pitch != -1f) {
									HomeLocation = new Location(HomeWorld, X, Y, Z, Yaw, Pitch);
								//}
								
							}
						} catch(Exception e) {
							
						}
					}
					//Create Home if all values are valid
					if(HomeNumber != -1 && HomeLocation != null && HomeWorld != null && Accessers != null) {
						if(!HomeDataContainsData(HomeList, HomeName, HomeNumber)) { //Data isn't already in Home List
							Home newHome = new Home();
							newHome.Accessers = Accessers;
							newHome.Description = Description;
							newHome.Name = HomeName;
							newHome.HomeNumber = HomeNumber;
							newHome.Location = HomeLocation;
							newHome.Owner = Owner;
							newHome.HostFile = configuration;
							
							HomeList.add(newHome);
							//System.out.println("Successfully Loaded Home: " + HomeName);
						}
					} else {
						//Debug shit :3
						System.out.println("Failed to Load Home: " + HomeName);
						System.out.println("Due to:" );
						String IssueName = "";
						if(HomeNumber == -1) {
							IssueName += ", HomeNumber";
						}
						if(HomeLocation == null) {
							IssueName += ", HomeLocation";
						}
						if(HomeWorld == null) {
							IssueName += ", HomeWorld";
						}
						if(Accessers == null) {
							IssueName += ", Accessers";
						}
						if(IssueName.startsWith(",")) {
							IssueName = IssueName.substring(1, IssueName.length()).trim();
						} else {
							IssueName = "None... O.o";
						}
						System.out.println(IssueName);
					}
				}
			}
		}
		
		return HomeList;
	}
	
	public static boolean HomeDataContainsData(List<Home> MasterArray, String Name, int Number) {
		for(Home home : MasterArray) {
			if(home.Name.toLowerCase().trim().equals(Name.toLowerCase().trim()) &&
					home.HomeNumber == Number) {
				return true;
			}
		}
		return false;
	}
	
	public static void AddHome(Player player, HashMap<String, List<Home>> WorldPlayerData,
			String HomeName, String Description, int HomeNumber, String WorldDataPath, boolean PushDataSave) {

		Home playerHome = new Home();
		
		//Initialize the home with valid data
		playerHome.Name = HomeName;
		playerHome.Description = Description;
		playerHome.HostFile = new Configuration(new File(WorldDataPath + player.getName() + ".mhf"));
		playerHome.Owner = player.getName();
		playerHome.Location = player.getLocation();
		playerHome.HomeNumber = HomeNumber;
		
		if(WorldPlayerData.containsKey(player.getName())) {
			WorldPlayerData.get(player.getName()).add(playerHome); //Add home to previous list
		} else {
			List<Home> homeList = new ArrayList<Home>();
			
			homeList.add(playerHome); //Add home to players home list
			WorldPlayerData.put(player.getName(), homeList); //Add Home list to global data list
		}
		if(PushDataSave) {
			SavePlayerHomes(player.getName(), WorldPlayerData, null);
		}
	}
	
	public static List<Home> GetPlayerHomes(String Player, HashMap<String, List<Home>> WorldData) {
		/*if(WorldData.containsKey(Player)) {
			return WorldData.get(Player);
		}*/
		for(String player : WorldData.keySet()) {
			if(player.toLowerCase().trim().equals(Player.toLowerCase().trim())) {
				return WorldData.get(player);
			}
		}
		return null;
	}
	
	public static Home GetPlayerHome(String PlayerName, String HomeName, HashMap<String, List<Home>> WorldData) {
		List<Home> HomeList = GetPlayerHomes(PlayerName, WorldData);
		if(HomeList != null) {
			for(Home home : HomeList) {
				if(home.Name.trim().toLowerCase().equals(HomeName.trim().toLowerCase())) {
					return home;
				}
			}
		}
		return null;
	}
	
	public static Home GetPlayerHome(String PlayerName, int HomeNumber, HashMap<String, List<Home>> WorldData) {
		List<Home> HomeList = GetPlayerHomes(PlayerName, WorldData);
		if(HomeList != null) {
			for(Home home : HomeList) {
				if(home.HomeNumber == HomeNumber) {
					return home;
				}
			}
		}
		return null;
	}
}
