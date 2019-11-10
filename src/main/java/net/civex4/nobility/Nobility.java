package net.civex4.nobility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import io.github.kingvictoria.NobilityRegions;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentManager;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.estate.EstateManager;
import net.civex4.nobility.group.GroupManager;
import net.civex4.nobility.gui.GUICommand;
import net.civex4.nobility.gui.TextInputListener;
import net.civex4.nobility.listeners.ChestClick;
import net.civex4.nobility.listeners.CommandListener;
import net.civex4.nobility.listeners.DevelopmentGUI;
import net.civex4.nobility.listeners.EstateCreate;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.ACivMod;

public class Nobility extends ACivMod {
	
	public static GroupManager groupMan;
	public static EstateManager estateMan;
	private static Nobility nobility;
	private static NobilityRegions nobilityRegions;
	private static DevelopmentManager developmentManager;

	public static int currentDay = 0;
	
	@Override
	public void onEnable() {
		super.onEnable();
		nobility = this;
		nobilityRegions = getPlugin(NobilityRegions.class);
		groupMan = new GroupManager();
		estateMan = new EstateManager();

		developmentManager = new DevelopmentManager();
		registerConfig();
		reloadConfig();
		getCommand("nobility").setExecutor(new CommandListener());
		getCommand("test").setExecutor(new GUICommand());
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
		pm.registerEvents(new TextInputListener(), this);
	}


	public void onDisable() {

	}

	/**
	 * Gets the instance of the Nobility plugin
	 *
	 * @return Nobility Plugin
	 */
	public static Nobility getNobility() {
		return nobility;
	}

	/**
	 * Gets the instance of the NobilityRegions plugin
	 *
	 * @return NobilityRegions Plugin
	 */
	public static io.github.kingvictoria.NobilityRegions getNobilityRegions() {
		return nobilityRegions;
	}

	public static DevelopmentManager getDevelopmentManager() {
		return developmentManager;
	}

	public static void tickDay() {
		currentDay += 1;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(ChatColor.GOLD + "Rise and shine! A new dawn is upon us. The current day is: "
					+ ChatColor.BLUE + ChatColor.BOLD + currentDay);
		}

		//DO STUFF
		for (Estate estate : estateMan.estates) {
			for (Development development : estate.getActiveDevelopments()) {
				developmentManager.subtractUpkeep(development.getDevelopmentType(), estate);
			}
		}

		for (Estate estate : estateMan.estates) {
			for (Development development : estate.getActiveDevelopments()) {
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
