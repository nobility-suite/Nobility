package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class DevelopmentType {
	
	private static LinkedHashMap<String, DevelopmentType> types = new LinkedHashMap<String, DevelopmentType>();
	
	private String name;
	private Material icon;	
	
	private List<String> prerequisites;
	private List<ItemStack> initialCost;
	private List<ItemStack> upkeepCost;
	private boolean collectsResources;
	private boolean collectsFood;
	private boolean isWall;
	private boolean isStorehouse;
	private String resource;
	


	
	public DevelopmentType(
			String name,
			Material icon,
			List<String> prerequisites,
			List<ItemStack> initialCost,
			List<ItemStack> upkeepCost,
			boolean collectsResources,
			boolean collectsFood,
			boolean isWall,
			boolean isStorehouse,
			String resource
			) {
		this.setName(name);
		this.setIcon(icon);
		this.setInitialCost(initialCost);
		this.setUpkeepCost(upkeepCost);
		this.setCollectsResources(collectsResources);
		this.setCollectsFood(collectsFood);
		this.setWall(isWall);
		this.setStorehouse(isStorehouse);
		this.setResource(resource);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Material getIcon() {
		return icon;
	}

	public void setIcon(Material icon) {
		this.icon = icon;
	}

	public boolean isCollectsResources() {
		return collectsResources;
	}

	public void setCollectsResources(boolean collectsResources) {
		this.collectsResources = collectsResources;
	}

	public boolean isCollectsFood() {
		return collectsFood;
	}

	public void setCollectsFood(boolean collectsFood) {
		this.collectsFood = collectsFood;
	}

	public boolean isWall() {
		return isWall;
	}

	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof DevelopmentType)) return false;
		DevelopmentType other = (DevelopmentType) obj;
		return other.getName().equals(name);
	}
	
	public static void loadDevelopmentTypes(ConfigurationSection config) {		
		for (String key : config.getKeys(false)) {
			Bukkit.getLogger().log(Level.INFO, "Loading Development Type {0}", key);
			DevelopmentType type = getDevelopmentType(config.getConfigurationSection(key));
			if (type != null) {
				types.put(key, type);
				Bukkit.getLogger().log(Level.INFO, "Development type {0} loaded: {1}", new Object[] {key, type});
				//Nobility.getDevelopmentManager().registerDevelopment(development, name, cost, icon, getPrerequisites(), getInitialCost(), resource);
			}
		}
	}
	
	public static DevelopmentType getDevelopmentType(String name) {
		return types.get(name);
	}
	
	public static DevelopmentType getDevelopmentType(ConfigurationSection config) {
		String name = config.getName();
		Material icon = Material.getMaterial(config.getString("icon"));
		List<String> prerequisites = config.getStringList("prerequisites");
		
		//TODO: Change costs to string-integer maps not ItemStacks
		List<ItemStack> initialCost = new ArrayList<ItemStack>();
		for (String key : config.getConfigurationSection("initialCost").getKeys(false)) {
			Material type = Material.getMaterial(key);
			int amount = config.getInt("initialCost." + key);
			initialCost.add(new ItemStack(type, amount));
		}
		List<ItemStack> upkeepCost = new ArrayList<ItemStack>();
		for (String key : config.getConfigurationSection("upkeepCost").getKeys(false)) {
			Material type = Material.getMaterial(key);
			int amount = config.getInt("upkeepCostCost." + key);
			upkeepCost.add(new ItemStack(type, amount));
		}

		//List<ItemStack> initialCost = new ItemStack(Material.getMaterial(config.getString("i)))
		boolean collectsResources = config.getBoolean("collectsResources");
		boolean collectsFood = config.getBoolean("collectsFood");
		boolean isWall = config.getBoolean("isWall");
		boolean isStorehouse = config.getBoolean("isStorehouse");
		String resource = config.getString("resource");
		return new DevelopmentType(
				name, 
				icon,
				prerequisites,
				initialCost,
				upkeepCost,
				collectsResources, 
				collectsFood, 
				isWall,
				isStorehouse,
				resource
				);
	}

	public List<ItemStack> getInitialCost() {
		return initialCost;
	}

	public void setInitialCost(List<ItemStack> initialCost) {
		this.initialCost = initialCost;
	}

	public List<String> getPrerequisites() {
		return prerequisites;
	}

	public void setPrerequisites(List<String> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public List<ItemStack> getUpkeepCost() {
		return upkeepCost;
	}

	public void setUpkeepCost(List<ItemStack> upkeepCost) {
		this.upkeepCost = upkeepCost;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public boolean isStorehouse() {
		return isStorehouse;
	}

	public void setStorehouse(boolean isStorehouse) {
		this.isStorehouse = isStorehouse;
	}
	
	public static LinkedHashMap<String, DevelopmentType> getTypes() {
		return types;
	}
	
}
