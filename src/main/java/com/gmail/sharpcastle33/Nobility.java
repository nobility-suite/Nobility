package com.gmail.sharpcastle33;

import io.github.kingvictoria.NobilityRegions;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.sharpcastle33.estate.EstateManager;
import com.gmail.sharpcastle33.group.GroupManager;
import com.gmail.sharpcastle33.listeners.CommandListener;
import com.gmail.sharpcastle33.listeners.EstateCreate;

public class Nobility extends JavaPlugin{
	
	public static GroupManager groupMan;
	public static EstateManager estateMan;
	private static Nobility nobility;
	private static NobilityRegions nobilityRegions;
	private CommandListener commandListener;
	
	public void onEnable() {
		
		nobility = this;
		nobilityRegions = getPlugin(NobilityRegions.class);
		groupMan = new GroupManager();
		estateMan = new EstateManager();
		
	    getCommand("nobility").setExecutor(new CommandListener());

	    registerEvents();

	    
	}
	
	public static GroupManager getGroupManager() {
	    	return groupMan;
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EstateCreate(), this);
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

}
