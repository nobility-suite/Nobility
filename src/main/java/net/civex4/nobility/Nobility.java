package net.civex4.nobility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import io.github.kingvictoria.NobilityRegions;
import net.civex4.nobility.customItem.AnvilDisable;
import net.civex4.nobility.customItem.BlockPlaceDisable;
import net.civex4.nobility.customItem.BottleBreak;
import net.civex4.nobility.customItem.GlassRecipe;
import net.civex4.nobility.customItem.InteractDisable;
import net.civex4.nobility.customItem.RecipeDisable;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentManager;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.development.behaviors.CollectorManager;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.estate.EstateManager;
import net.civex4.nobility.group.GroupManager;
import net.civex4.nobility.gui.GUICommand;
import net.civex4.nobility.listeners.ChestClick;
import net.civex4.nobility.listeners.CommandListener;
import net.civex4.nobility.listeners.CreateCommand;
import net.civex4.nobility.listeners.EstateCreate;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.ACivMod;

public class Nobility extends ACivMod {
	
	private static GroupManager groupMan;
	private static EstateManager estateMan;
	private static Nobility nobility;
	private static NobilityRegions nobilityRegions;
	private static DevelopmentManager developmentManager;
	private static CollectorManager collectorManager;

	public static int currentDay = 0;
	
	@Override
	public void onEnable() {
		super.onEnable();
		nobility = this;
		nobilityRegions = getPlugin(NobilityRegions.class);
		initializeManagers();
		registerConfig();
		reloadConfig();
		getCommand("nobility").setExecutor(new CommandListener());
		getCommand("test").setExecutor(new GUICommand());
		getCommand("create").setExecutor(new CreateCommand());

		DevelopmentType.loadDevelopmentTypes(getConfig().getConfigurationSection("developments"));

		registerEvents();
		
	}
	
	private static void initializeManagers() {
		groupMan = new GroupManager();
		estateMan = new EstateManager();
		developmentManager = new DevelopmentManager();
		collectorManager = new CollectorManager();
	}
	
	public static GroupManager getGroupManager() {
		return groupMan;
	}
	
	public static EstateManager getEstateManager() {
		return estateMan;
	}

	public static DevelopmentManager getDevelopmentManager() {
		return developmentManager;
	}
	
	public static CollectorManager getCollectorManager() {
		return collectorManager;
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EstateCreate(), this);
		pm.registerEvents(new ChestClick(), this);
		pm.registerEvents(new BottleBreak(), this);
		pm.registerEvents(new BlockPlaceDisable(), this);
		pm.registerEvents(new GlassRecipe(), this);
		pm.registerEvents(new RecipeDisable(), this);
		pm.registerEvents(new InteractDisable(), this);
		pm.registerEvents(new AnvilDisable(), this);
		
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

	public static void tickDay() {
		currentDay += 1;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(ChatColor.GOLD + "Rise and shine! A new dawn is upon us. The current day is: "
					+ ChatColor.BLUE + ChatColor.BOLD + currentDay);
		}

		for (Estate estate : estateMan.getEstates()) {
			for (Development development : estate.getActiveDevelopments()) {
				developmentManager.subtractUpkeep(development.getType(), estate);
			}
		}

		for (Estate estate : estateMan.getEstates()) {
			for (Development development : estate.getActiveDevelopments()) {
				development.tick();
			}
		}
	}

	private void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}
