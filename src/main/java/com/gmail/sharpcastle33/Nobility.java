package com.gmail.sharpcastle33;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.sharpcastle33.estate.EstateCreate;
import com.gmail.sharpcastle33.estate.EstateManager;
import com.gmail.sharpcastle33.group.GroupManager;
import com.gmail.sharpcastle33.listeners.CommandListener;

public class Nobility extends JavaPlugin{
	
	public static GroupManager groupMan;
	public static EstateManager estateMan;
	private Plugin plugin;
	private CommandListener commandListener;
	
	public void onEnable() {
		
		plugin = this;
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

}
