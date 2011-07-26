package org.tdsm.homes.imports;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.config.Configuration;
import org.tdsm.MultipleHomes;
import org.tdsm.homes.Home;

public class Imports {
	
	public static HashMap<String, List<Home>> ImportHomes(MultipleHomes plugin) {
		File oldHomes = new File(MultipleHomes.PluginFolder + "Homes");
		
		HashMap<String, List<Home>> ImportList = new HashMap<String, List<Home>>();
		
		if (oldHomes.exists()) {
			File[] userFiles = oldHomes.listFiles();
			
			for (int i = 0; i < userFiles.length; i++) {
				if (userFiles[i].isFile()) {
					String fileName = userFiles[i].getName().toLowerCase();
					if(fileName.endsWith(".txt") && fileName.startsWith("home_")) {
						System.out.println("Parsing: " + userFiles[i].getAbsolutePath());
						try {
							int HomeNumber = Integer.parseInt(fileName.substring(5, fileName.length() - 4));
							
							/*
							 * #Saves Users as:
							 * #   ~user:home:world
							 * ~user:X.Y_Z_Yaw_Pitch_World
							 * 
							 * Shitty old format, i know :3
							 */
							
							FileInputStream FileStream = new FileInputStream(userFiles[i].getAbsolutePath());
						    DataInputStream DataInput = new DataInputStream(FileStream);
						    BufferedReader BuffReader = new BufferedReader(new InputStreamReader(DataInput));
						    
						    String RLine = "";
						    while ((RLine = BuffReader.readLine()) != null)   {
						    	if (RLine.startsWith("~")) {
						    		
									String[] split = RLine.split(":");
									if (split.length == 2) {
										String[] values = split[1].split("_");
			
										try {
											Home home = new Home();
											home.Owner = split[0].substring(1).trim();
											//Parse Data
									    	//World HomeWorld = plugin.getServer().createWorld(values[5].trim(), Environment.NORMAL);
											World HomeWorld = plugin.getServer().getWorld(values[5].trim());
											if(HomeWorld != null) {
												home.Location = new Location(HomeWorld, 
														Double.parseDouble(values[0].trim()),
														Double.parseDouble(values[1].trim()),
														Double.parseDouble(values[2].trim()),
														Float.parseFloat(values[3].trim()),
														Float.parseFloat(values[4].trim()));
												home.HomeNumber = HomeNumber;
												
												home.Name = String.valueOf(HomeNumber);
												home.Description = "Imported Home " + home.Name;
												home.HostFile = new Configuration(new File(plugin.WorldFolder + home.Owner + ".mhf"));
												
												if(ImportList.containsKey(home.Owner)) {
													ImportList.get(home.Owner).add(home);
												} else {
													List<Home> homes = new ArrayList<Home>();
													if(homes.add(home)) {
														ImportList.put(home.Owner, homes);
													}
												}
											} else {
										    	System.out.println("No world '" + values[5].trim() + "' has been found");
											}
											
										} catch (Exception e) {
											System.out.println("Exception importing data: " + e.getMessage());
											e.printStackTrace();
										}
									}
								}
						    }
						    BuffReader.close();
						    DataInput.close();
						    FileStream.close();
						} catch(Exception e) {
							plugin.WritetoConsole("Error parsing home data for file: " + userFiles[i].getAbsolutePath());
							plugin.WritetoConsole(e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
		}
		return ImportList;
	}
	
}
