package net.civex4.nobility.development;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.gui.Renamable;

public class DevelopmentType {
	
	private static LinkedHashMap<String, DevelopmentType> types = new LinkedHashMap<String, DevelopmentType>();
	
	private String name; //Internal; Generate if config is null as the title in oneWordForm
	private String title; //External; Change to "Renamable"
	private Material icon; //Change to "MaterialInput"
	
	private List<String> prerequisites; //Change to "SelectFromList"
	private List<ItemStack> initialCost; //Change to "ItemInput"
	private List<ItemStack> upkeepCost; //Change to "ItemInput"
	private boolean isCollector; //Change to "Booleanable"
	private boolean isWall;      // "     "    "	
	private boolean isStorehouse;// "     "    "
	private String resource; //Needs to be changed to "SelectFromList" from the NobilityRegion "RegionResource" enum
	
	Renamable titleRenamable = new Renamable() {		
		String title;
		
		@Override
		public void setName(String name) {
			this.title = name;		
		}
		
		@Override
		public String getName() {
			return title;
		}
	};
	

	
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
			String resource
			) {
		this.setName(name);
		this.setTitle(title);
		this.setIcon(icon);
		this.setPrerequisites(prerequisites);
		this.setInitialCost(initialCost);
		this.setUpkeepCost(upkeepCost);
		this.setCollector(isCollector);
		this.setWall(isWall);
		this.setStorehouse(isStorehouse);
		this.setResource(resource);
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
		
		ArrayList<String> prerequisites = new ArrayList<String>();
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

		//List<ItemStack> initialCost = new ItemStack(Material.getMaterial(config.getString("i)))
		boolean isCollector = config.getBoolean("isCollector");
		boolean isWall = config.getBoolean("isWall");
		boolean isStorehouse = config.getBoolean("isStorehouse");
		String resource = config.getString("resource");
		
		//Check if the string is a material
		if (config.getConfigurationSection("resource") != null) {
			resource = resource.toUpperCase();
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
		try {
			return prerequisites;
		} catch (NullPointerException e) {
			prerequisites = new ArrayList<>();
			return prerequisites;
		}
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
	
	/*
	 * Development GUI pseudo code
	 * 
	 * new clickableInventory
	 * 
	 * for development types
	 * 	for changeabale things in development types
	 * 		create a clickable
	 * 		if thing is instanceOf renamable
	 * 			New TextInput
	 * 		if thing is instanceOf changeableBoolean
	 * 			New BooleanButton
	 * 				onClick {
	 * 					thing.state = state
	 * 				}
	 * 		if thing is instanceOf itemInput
	 * 			New ItemInputButton
	 * 				onClick 
	 * 					choose item
	 * 					enter number
	 * 	Add clickable to inventory
	 * 
	 * 
	 * Server Setttings -> DevelopmentTypes -> Changeables -> sub changeable menus
	 *  |
	 *  L> Regions / This region -> Region Changeables -> sub changeable menus
	 *  
	 * 
	 */
	
	
	
	
	

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


	
}
