package net.civex4.nobility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.RegionResource;
import net.civex4.nobility.customItem.CustomItem;
import net.civex4.nobility.customItem.listeners.AnvilDisable;
import net.civex4.nobility.customItem.listeners.BlockPlaceDisable;
import net.civex4.nobility.customItem.listeners.BottleBreak;
import net.civex4.nobility.customItem.listeners.GlassRecipe;
import net.civex4.nobility.customItem.listeners.InteractDisable;
import net.civex4.nobility.customItem.listeners.OpenSmithingTable;
import net.civex4.nobility.customItem.listeners.RecipeDisable;
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
import vg.civcraft.mc.civmodcore.playersettings.PlayerSettingAPI;
import vg.civcraft.mc.civmodcore.playersettings.StringInputSetting;
import vg.civcraft.mc.civmodcore.playersettings.gui.MenuSection;
import vg.civcraft.mc.civmodcore.playersettings.impl.StringSetting;

public class Nobility extends ACivMod {
	
	private static GroupManager groupMan;
	private static EstateManager estateMan;
	private static Nobility nobility;
	private static NobilityRegions nobilityRegions;
	private static DevelopmentManager developmentManager;
	private static CollectorManager collectorManager;
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
		getCommand("nobility").setExecutor(new CommandListener());
		getCommand("test").setExecutor(new GUICommand());
		getCommand("create").setExecutor(new CreateCommand());

		DevelopmentType.loadDevelopmentTypes(getConfig().getConfigurationSection("developments"));

		registerEvents();
		
		ItemStack glassAmount = CustomItem.create(Material.GLASS, "Broken Glass").getItem();
		glassAmount.setAmount(9);
		CustomItem.createRecipe(new ItemStack(Material.GLASS), glassAmount);
		String happinessMessage = "Happiness is just an abstract concept";
		CustomItem happiness = CustomItem.create(Material.EGG, "Happiness", happinessMessage);
		CustomItem.createRecipe(happiness.getItem(), RegionResource.GUN.resource(), RegionResource.BUTTER.resource());
		StringInputSetting<String> string = new StringSetting(this, "Broken Glass", "Glass Name", "glassName", new ItemStack(Material.GLASS), "changes name of glass");
		PlayerSettingAPI.registerSetting(string, DevelopmentType.getDevelopmentMenu());
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
		pm.registerEvents(new OpenSmithingTable(), this);
		
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
	
	public static MenuSection getMenu() {
		return nobilityMenu;
	}

}
