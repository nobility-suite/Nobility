package net.civex4.nobility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import io.github.kingvictoria.NobilityRegions;
import net.civex4.nobility.claim.ClaimManager;
import net.civex4.nobility.database.utility.DatabaseBuilder;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentManager;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.estate.EstateManager;
import net.civex4.nobility.group.GroupManager;
import net.civex4.nobility.gui.GUICommand;
import net.civex4.nobility.listeners.ChestClick;
import net.civex4.nobility.listeners.CommandListener;
import net.civex4.nobility.listeners.CreateCommand;
import net.civex4.nobility.listeners.EstateCommandListener;
import net.civex4.nobility.workers.WorkerManager;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.ACivMod;
import vg.civcraft.mc.civmodcore.playersettings.PlayerSettingAPI;
import vg.civcraft.mc.civmodcore.playersettings.gui.MenuSection;

public class Nobility extends ACivMod {
	
	private static GroupManager groupMan;
	private static EstateManager estateMan;
	private static Nobility nobility;
	private static NobilityRegions nobilityRegions;
	private static DevelopmentManager developmentManager;
	private static WorkerManager workerManager;
	
	private static ClaimManager claimManager;
	private static MenuSection nobilityMenu = PlayerSettingAPI.getMainMenu().createMenuSection("Nobility", "Settings");
	
	public static int currentDay = 0;
	
	
	@Override
	public void onEnable() {
		super.onEnable();
		nobility = this;
		nobilityRegions = getPlugin(NobilityRegions.class);
		initializeManagers();
		registerConfig();
		reloadConfig();
		this.getCommand("nobility").setExecutor(new CommandListener());
		this.getCommand("test").setExecutor(new GUICommand());
		this.getCommand("estate").setExecutor(new EstateCommandListener());
		this.getCommand("create").setExecutor(new CreateCommand());
		
		//DevelopmentType.loadDevelopmentTypes(getConfig().getConfigurationSection("developments"));

		registerEvents();

		new DatabaseBuilder().setUpDatabase();
	}
	
	private static void initializeManagers() {
		groupMan = new GroupManager();
		estateMan = new EstateManager();
		developmentManager = new DevelopmentManager();
		claimManager = new ClaimManager();
		workerManager = new WorkerManager();
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
	
	public static ClaimManager getClaimManager() {
		return claimManager;
	}
	
	public static WorkerManager getWorkerManager() {
		return workerManager;
	}
	
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		//pm.registerEvents(new EstateCreate(), this);
		pm.registerEvents(new ChestClick(), this);

		
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
				//developmentManager.subtractUpkeep(development.getType(), estate);
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
	
	
	
	public static MenuSection getMenu() {
		return nobilityMenu;
	}

}
