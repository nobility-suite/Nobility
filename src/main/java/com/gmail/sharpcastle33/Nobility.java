package com.gmail.sharpcastle33;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.development.DevelopmentManager;
import com.gmail.sharpcastle33.development.DevelopmentType;
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
	
	public static int currentDay = 0;
	
	public void onEnable() {
		
		nobility = this;
		nobilityRegions = getPlugin(NobilityRegions.class);
		groupMan = new GroupManager();
		estateMan = new EstateManager();

		developmentManager = new DevelopmentManager();
		registerConfig();
		reloadConfig();
	    getCommand("nobility").setExecutor(new CommandListener());
	    DevelopmentType.loadDevelopmentTypes(getConfig().getConfigurationSection("developments"));
	    
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
				developmentManager.subtractUpkeep(development.getDevelopmentType(), estate);				
			}
		}
		
		for (Estate estate : estateMan.estates) {
			for(Development development: estate.getActiveDevelopments()) {
				if (development.isActive()) {
					development.tick();
				}					
			}
		}
	}
	
	private void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();		
	}

}
