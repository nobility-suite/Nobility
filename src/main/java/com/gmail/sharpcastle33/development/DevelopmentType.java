package com.gmail.sharpcastle33.development;

import java.util.LinkedHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ocelot.Type;

import com.gmail.sharpcastle33.Nobility;

public class DevelopmentType {
	
	private static LinkedHashMap<String, DevelopmentType> types = new LinkedHashMap<String, DevelopmentType>();
	
	private String name;
	private Material icon;	
	
	private boolean collectsResources;
	private boolean collectsFood;
	private boolean isWall;
	
	public DevelopmentType(
			String name,
			Material icon,
			boolean collectsResources,
			boolean collectsFood,
			boolean isWall
			) {
		this.setName(name);
		this.setIcon(icon);
		this.setCollectsResources(collectsResources);
		this.setCollectsFood(collectsFood);
		this.setWall(isWall);
		
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
			}
		}
	}
	
	public static DevelopmentType getDevelopmentType(String name) {
		return types.get(name);
	}
	
	public static DevelopmentType getDevelopmentType(ConfigurationSection config) {
		String name = config.getName();
		Material icon = Material.getMaterial(config.getString("icon"));
		boolean collectsResources = config.getBoolean("collectsResources");
		boolean collectsFood = config.getBoolean("collectsFood");
		boolean isWall = config.getBoolean("isWall");
		
		return new DevelopmentType(name, icon, collectsResources, collectsFood, isWall);
	}
	
}
