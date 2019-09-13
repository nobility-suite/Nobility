package com.gmail.sharpcastle33;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.sharpcastle33.development.Butcher;
import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.development.DevelopmentManager;
import com.gmail.sharpcastle33.development.Gatherer;
import com.gmail.sharpcastle33.development.Granary;
import com.gmail.sharpcastle33.development.IronMine;
import com.gmail.sharpcastle33.development.LoggingCamp;
import com.gmail.sharpcastle33.development.Quarry;
import com.gmail.sharpcastle33.development.Storehouse;
import com.gmail.sharpcastle33.development.VictoryPoint;
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
		
		//Cost to create Granary, Butcher, or Gatherer
		List<ItemStack> foodInitialCost = new ArrayList<>();
		ItemStack wood = new ItemStack(Material.OAK_LOG, 10);
		ItemStack stoneFood = new ItemStack(Material.SMOOTH_STONE, 5);
		foodInitialCost.add(wood);
		foodInitialCost.add(stoneFood);
		
		//Cost to create Logging Camp, Quarry, or Iron Mine
		List<ItemStack> resourceInitialCost = new ArrayList<>();
		ItemStack iron = new ItemStack(Material.IRON_INGOT, 5);
		ItemStack stoneResource = new ItemStack(Material.SMOOTH_STONE, 5);
		resourceInitialCost.add(iron);
		resourceInitialCost.add(stoneResource);
		
		//Victory Point initial cost
		List<ItemStack> victoryPointInitialCost = new ArrayList<>();
		ItemStack ironVC = new ItemStack(Material.IRON_INGOT, 25);
		ItemStack stoneVC = new ItemStack(Material.SMOOTH_STONE, 25);
		ItemStack woodVC = new ItemStack(Material.OAK_LOG, 25);
		victoryPointInitialCost.add(ironVC);
		victoryPointInitialCost.add(stoneVC);
		victoryPointInitialCost.add(woodVC);
		
		//Cost of upkeep for Granary, Butcher, and Gatherer
		HashMap<String, Integer> foodUpkeep = new HashMap<>();
		foodUpkeep.put("Food", 1);
		
		HashMap<String, Integer> resourceUpkeep = new HashMap<>();
		resourceUpkeep.put("Food", 2);
		
		//Cost of upkeep for Victory Point
		HashMap<String, Integer> victoryPointUpkeep = new HashMap<>();
		victoryPointUpkeep.put("Hardware", 3);
		victoryPointUpkeep.put("Food", 3);
		
		developmentManager.registerDevelopment(Granary.class, "Granary", foodUpkeep, Material.BREAD, storehousePrerequisites, foodInitialCost);
		developmentManager.registerDevelopment(LoggingCamp.class, "Logging Camp", resourceUpkeep, Material.IRON_AXE, storehousePrerequisites, resourceInitialCost);
		developmentManager.registerDevelopment(Storehouse.class, "Storehouse", new HashMap<>(), Material.CHEST, new ArrayList<>(), new ArrayList<>());
		developmentManager.registerDevelopment(Butcher.class, "Butcher", foodUpkeep, Material.BEEF, storehousePrerequisites, foodInitialCost);
		developmentManager.registerDevelopment(Gatherer.class, "Gatherer", foodUpkeep, Material.SWEET_BERRIES, storehousePrerequisites, foodInitialCost);
		developmentManager.registerDevelopment(Quarry.class, "Quarry", resourceUpkeep, Material.SMOOTH_STONE, storehousePrerequisites, resourceInitialCost);
		developmentManager.registerDevelopment(IronMine.class, "Iron Mine", resourceUpkeep, Material.IRON_PICKAXE, storehousePrerequisites, resourceInitialCost);
		developmentManager.registerDevelopment(VictoryPoint.class, "Victory Point", victoryPointUpkeep, Material.BELL, storehousePrerequisites, victoryPointInitialCost);
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
				developmentManager.subtractUpkeep(development.getRegister(), estate, development);
				if (development.isActive()) {
					development.tick();
				}
			}
		}
	}

}
