package com.gmail.sharpcastle33;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.sharpcastle33.development.Butcher;
import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.development.DevelopmentManager;
import com.gmail.sharpcastle33.development.Granary;
import com.gmail.sharpcastle33.development.Storehouse;
import com.gmail.sharpcastle33.estate.Estate;
import com.gmail.sharpcastle33.estate.EstateManager;
import com.gmail.sharpcastle33.group.GroupManager;
import com.gmail.sharpcastle33.listeners.ChestClick;
import com.gmail.sharpcastle33.listeners.CommandListener;
import com.gmail.sharpcastle33.listeners.DevelopmentGUI;
import com.gmail.sharpcastle33.listeners.EstateCreate;

import io.github.kingvictoria.NobilityRegions;
import net.md_5.bungee.api.ChatColor;

public class Nobility extends JavaPlugin{
	
	public static GroupManager groupMan;
	public static EstateManager estateMan;
	private static Nobility nobility;
	private static NobilityRegions nobilityRegions;
	private static DevelopmentManager developmentManager;
	private CommandListener commandListener;
	
	public static int currentDay = 0;
	
	public void onEnable() {
		
		nobility = this;
		nobilityRegions = getPlugin(NobilityRegions.class);
		groupMan = new GroupManager();
		estateMan = new EstateManager();

		developmentManager = new DevelopmentManager();
		/* Temporary */
		List<String> storehousePrerequisites = new ArrayList<>();
		storehousePrerequisites.add("Storehouse");
		developmentManager.registerDevelopment(Granary.class, "Granary", new HashMap<>(), Material.BREAD, storehousePrerequisites);
		developmentManager.registerDevelopment(Granary.class, "Logging Camp", new HashMap<>(), Material.BREAD, storehousePrerequisites);
		developmentManager.registerDevelopment(Storehouse.class, "Storehouse", new HashMap<>(), Material.CHEST, new ArrayList<>());
		developmentManager.registerDevelopment(Butcher.class, "Butcher", new HashMap<>(), Material.BEEF, storehousePrerequisites);
		/* End Temporary Code */
		
	    getCommand("nobility").setExecutor(new CommandListener());

	    registerEvents();

	    
	}
	
	public static GroupManager getGroupManager() {
	    	return groupMan;
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EstateCreate(), this);
		pm.registerEvents(new ChestClick(), this);
		pm.registerEvents(new DevelopmentGUI(), this);
	}
	
	public void onDisable() {
		
	}

	/**
	 * Gets the instance of the Nobility plugin
	 * @return Nobility Plugin
	 */
	public static Nobility getNobility() {
		return nobility;
	} // getNobility

	/**
	 * Gets the instance of the NobilityRegions plugin
	 * @return NobilityRegions Plugin
	 */
	public static NobilityRegions getNobilityRegions() {
		return nobilityRegions;
	} // getNobilityRegions

	public static DevelopmentManager getDevelopmentManager() {
		return developmentManager;
	}
	
	public static void tickDay() {
		currentDay += 1;
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(ChatColor.GOLD + "Rise and shine! A new dawn is upon us. The current day is: " + ChatColor.BLUE + ChatColor.BOLD + currentDay);
		}
		//DO STUFF
		for (Estate estate : estateMan.estates) {
			for(Development development: estate.getActiveDevelopments()) {
				development.tick();
			}
		}
	}

}
