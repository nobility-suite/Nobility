package net.civex4.nobility.development;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import io.github.kingvictoria.RegionResource;
import net.civex4.nobility.Nobility;
import vg.civcraft.mc.civmodcore.playersettings.PlayerSettingAPI;
import vg.civcraft.mc.civmodcore.playersettings.gui.MenuSection;

public class DevelopmentType {
	
	private static LinkedHashMap<String, DevelopmentType> types = new LinkedHashMap<String, DevelopmentType>();
	
	private static MenuSection developmentMenu = Nobility.getMenu().createMenuSection("Developments", "Settings");
	
	private String name; // Internal Slug
	private String title; // Beautiful Title
	private Material icon;
	
	private List<String> prerequisites;
	private List<ItemStack> initialCost;
	private List<ItemStack> upkeepCost;
	
	private boolean isCollector;
	private boolean isWall;	
	private boolean isStorehouse;
	private RegionResource resource;
	
	
	public DevelopmentType(
			String name,
			String title,
			Material icon,
			List<String> prerequisites,
			List<ItemStack> initialCost,
			List<ItemStack> upkeepCost,
			boolean isCollector,
			boolean isWall,
			boolean isStorehouse,
			RegionResource resource
			) {
		this.name = name;
		this.title = title;
		this.icon = icon;
		this.prerequisites = prerequisites;
		this.initialCost = initialCost;
		this.upkeepCost = upkeepCost;
		this.isCollector = isCollector;
		this.isWall = isWall;
		this.isStorehouse = isStorehouse;
		this.resource = resource;
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
		String title = config.getString("title");
		Material icon = Material.getMaterial(config.getString("icon"));
		
		ArrayList<String> prerequisites = new ArrayList<>();
		List<String> listOfPrerequisites = config.getStringList("prerequisites");
		prerequisites.addAll(listOfPrerequisites);
		
		//TODO: Change costs to string-integer maps not ItemStacks
		List<ItemStack> initialCost = new ArrayList<ItemStack>();
		if (config.getConfigurationSection("initialCost") != null) { 
			for (String key : config.getConfigurationSection("initialCost").getKeys(false)) {
				Material type = Material.getMaterial(key);
				int amount = config.getInt("initialCost." + key);
				initialCost.add(new ItemStack(type, amount));
			}
		}
		List<ItemStack> upkeepCost = new ArrayList<ItemStack>();
		if (config.getConfigurationSection("upkeepCost") != null) { 
			for (String key : config.getConfigurationSection("upkeepCost").getKeys(false)) {
				Material type = Material.getMaterial(key);
				int amount = config.getInt("upkeepCost." + key);
				upkeepCost.add(new ItemStack(type, amount));
			}
		}

		boolean isCollector = config.getBoolean("isCollector");
		boolean isWall = config.getBoolean("isWall");
		boolean isStorehouse = config.getBoolean("isStorehouse");
		String resourceString = config.getString("resource");
		RegionResource resource = null;
		if (resourceString != null) {
			resource = RegionResource.getResource(resourceString);
			if (resource == null) {
				Bukkit.getLogger().warning("Could not parse config " + name + " because the resource is not a valid RegionResource");
				return null;
			}
			Nobility.getNobility().info("Resource " + resource.toString() + " added to " + title + " development type");
		}

		return new DevelopmentType(
				name, 
				title,
				icon,
				prerequisites,
				initialCost,
				upkeepCost,
				isCollector,
				isWall,
				isStorehouse,
				resource
				);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static List<String> getTitles(List<DevelopmentType> types) {
		List<String> titles = new ArrayList<>();
		for (DevelopmentType type : types) {
			titles.add(type.getTitle());
		}
		return titles;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Material getIcon() {
		return icon;
	}

	public void setIcon(Material icon) {
		this.icon = icon;
	}

	public boolean isWall() {
		return isWall;
	}

	public void setWall(boolean isWall) {
		this.isWall = isWall;
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

	public RegionResource getResource() {
		if (!this.isCollector) {
			Bukkit.getLogger().warning("You shouldn't try get the resource of something that isn't a collector");
		}
		
		return resource;
	}

	public void setResource(RegionResource resource) {
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

	public boolean isCollector() {
		return isCollector;
	}

	public void setCollector(boolean isCollector) {
		this.isCollector = isCollector;
	}

	public static MenuSection getDevelopmentMenu() {
		return developmentMenu;
	}
	
}
