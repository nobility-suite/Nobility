package net.civex4.nobility.blueprints;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.developments.AbstractWorkshop;
import net.civex4.nobilityitems.NobilityItem;
import net.civex4.nobilityitems.NobilityItems;

public class BlueprintManager {
  
  public ArrayList<AbstractBlueprint> blueprints;
  static FileConfiguration blueprintConfig;
  
  public void init(File config) {
    if (!config.exists()) {        
      Nobility.getNobility().saveResource("blueprints.yml", false);
    }
    
    blueprints = new ArrayList<AbstractBlueprint>();
    
    try {
      blueprintConfig = YamlConfiguration.loadConfiguration(config);
      blueprints = loadBlueprints(blueprintConfig);
    }catch(IllegalArgumentException e) {
      Bukkit.getLogger().severe("Blueprint config does not exist.");
      e.printStackTrace();
    }
  }
  
  public boolean recipeExists(ItemStack i, AbstractWorkshop d) {
		Block b = d.inputChest.getBlock();
		if(b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
			Chest chest = (Chest) b.getState();
			Inventory inv = chest.getInventory();
			if(inv.contains(i)) {
				return true;
			}else return false;
		}
		return false;
  }
  
  public ArrayList<ItemStack> listBlueprints(Inventory inv){
	  ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
	  for(ItemStack i : inv.getContents()) {
		  if(i != null)
		  if(i.getType() == Material.PAPER) {
			  if(i.hasItemMeta() && i.getItemMeta().getDisplayName().contains("Blueprint")) {
				  ret.add(i);
			  }
		  }
	  }
	  return ret;
  }

  private ArrayList<AbstractBlueprint> loadBlueprints(FileConfiguration config) {
    Bukkit.getServer().getLogger().info("Loading Blueprints...");
    ArrayList<AbstractBlueprint> ret = new ArrayList<AbstractBlueprint>();
    
    for(String blueprintKey : config.getKeys(false)) {
      AbstractBlueprint blueprint = loadBlueprint(config,blueprintKey);
      if (blueprint != null) {
        ret.add(blueprint);
        Bukkit.getServer().getLogger().info("Loaded " + blueprintKey);
      } else {
        Bukkit.getServer().getLogger().warning("Failed to load " + blueprintKey);
      }
    }
    Bukkit.getServer().getLogger().info(ret.size() + " Blueprints Loaded!");

    return ret;
  }
  
  private AbstractBlueprint loadBlueprint(FileConfiguration config, String key) {
    ConfigurationSection header = config.getConfigurationSection(key);
    if (header == null) {
      Bukkit.getServer().getLogger().warning("Malformed section " + key);
      return null;
    }

    NobilityItem result = null;
    int minRuns = 0;
    int maxRuns = 0;
    String name = "";
    int minReturn = 0;
    int maxReturn = 0;
    
    ArrayList<ItemGroup> itemGroups = new ArrayList<ItemGroup>();

    boolean returnNull = false;
    
    
    if (header.getString("name") != null) {
      name = header.getString("name");
    } else {
      Bukkit.getLogger().warning("Blueprint " + header.getCurrentPath() + " has no name!");
      returnNull = true;
    }
    
    if (header.getString("minRuns") != null) {
      minRuns = header.getInt("minRuns");
    } else {
      Bukkit.getLogger().warning("Blueprint " + header.getCurrentPath() + " has no minRuns!");
      returnNull = true;
    }
    
    if (header.getString("minReturn") != null) {
      minReturn = header.getInt("minReturn");
    } else {
      Bukkit.getLogger().warning("Blueprint " + header.getCurrentPath() + " has no minReturn!");
      returnNull = true;
    }
    
    if (header.getString("maxRuns") != null) {
      maxRuns = header.getInt("maxRuns");
    } else {
      Bukkit.getLogger().warning("Blueprint " + header.getCurrentPath() + " has no maxRuns!");
      returnNull = true;
    }
    
    if (header.getString("maxReturn") != null) {
      maxReturn = header.getInt("maxReturn");
    } else {
      Bukkit.getLogger().warning("Blueprint " + header.getCurrentPath() + " has no maxReturn!");
      returnNull = true;
    }
    
    if (header.isString("result")) {
      try {
        result = NobilityItems.getItemByName(header.getString("result"));
      } catch (IllegalArgumentException e) {
        Bukkit.getLogger().warning("Blueprint " + header.getCurrentPath() + " has invalid NobilityItem in result!");
        returnNull = true;
      }

    } else {
      Bukkit.getLogger().warning("Blueprint " + header.getCurrentPath() + " has no result!");
      returnNull = true;
    }
    
    ConfigurationSection groups = header.getConfigurationSection("itemGroups");
    if (groups != null) {
      itemGroups = getItemGroups(groups);
      if (itemGroups == null) {
        returnNull = true;
      }
    } else {
      Bukkit.getLogger().warning("Blueprint " + header.getCurrentPath() + " has no itemGroups!");
      returnNull = true;
    }
    
    if (returnNull) return null;
    return new AbstractBlueprint(itemGroups, result, minRuns, maxRuns, name, minReturn, maxReturn);
  }

  private ArrayList<ItemGroup> getItemGroups(ConfigurationSection groups) {
    ArrayList<ItemGroup> ret = new ArrayList<ItemGroup>();

    boolean returnNull = false;

    for(String groupKey : groups.getKeys(false)) {
      ConfigurationSection group = groups.getConfigurationSection(groupKey);
      
      if(group.getString("type") != null) { //Parse Item Groups Here
        String type = group.getString("type");
        
        if(type.equalsIgnoreCase("SIMPLE")) {
          ItemGroup g = getSimpleItemGroup(group);
          if (g == null) {
            returnNull = true;
          } else {
            ret.add(g);
          }
          continue;
        }
        
        if(type.equalsIgnoreCase("VARIABLE")) {
        	ItemGroup g = getVariableItemGroup(group);
            if (g == null) {
                returnNull = true;
              } else {
                ret.add(g);
              }
              continue;
        }
        
        if(type.equalsIgnoreCase("RATIO")) {
        	ItemGroup g = getRatioItemGroup(group);
            if (g == null) {
                returnNull = true;
              } else {
                ret.add(g);
              }
              continue;
        }
        
        /*
         * add more cases here
         */

        Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has invalid type '" + type + "'! Try 'SIMPLE'?");
        returnNull = true;
      } else {
        Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no type!");
        returnNull = true;
      }
    }
    
    if (returnNull) return null;
    return ret;
  }
  
  
  /*
   *     ig2:                    #Wood and Stone supplies
      type: VARIABLE            
      selectionCount: 2             
      items:                 
        - rough_planks 3-5
        - rough_stone 5-6
        - bricks 6-7
        - iron_ingot 7-8
        - steel_ingot 8-25
   */
  
  
  private ItemGroup getVariableItemGroup(ConfigurationSection group) {
	  int selectionCount = 1;
	    
	    ArrayList<NobilityItem> items = new ArrayList<NobilityItem>();
	    ArrayList<Integer> minCounts = new ArrayList<Integer>();
	    ArrayList<Integer> maxCounts = new ArrayList<Integer>();
	    boolean returnNull = false;
	    
	    if(group.getString("selectionCount") != null) {
	      selectionCount = group.getInt("selectionCount");
	    } else {
	      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no selectionCount!");
	      returnNull = true;
	    }
	    
	    ConfigurationSection list = group.getConfigurationSection("items");
	    if (group.isList("items")) {
	      List<String> stringItems = group.getStringList("items");
	      //TODO variable parsing here
	      for (String string : stringItems) {
	    	  String[] arr = string.split(" ");
	    	  String[] counts = arr[1].split("-");
	    	  minCounts.add(Integer.parseInt(counts[0]));
	    	  maxCounts.add(Integer.parseInt(counts[1]));
	        try {
	          items.add(NobilityItems.getItemByName(arr[0]));
	        } catch (IllegalArgumentException e) {
	          Bukkit.getLogger().severe("itemGroup " + group.getCurrentPath() + " has invalid NobilityItem " + string + " in items!");
	          returnNull = true;
	        }
	      }
	    } else {
	      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no items!");
	      returnNull = true;
	    }
	    
	    if (returnNull) return null;
	    return new VariableItemGroup(items,selectionCount,minCounts,maxCounts); 
	    
}
  
  /*
   *     ig2:                    #Wood and Stone supplies
      type: RATIO            
      minCount: 50
      maxCount: 100             
      items:                 
        - rough_planks 3/0.2
        - rough_stone 5/0.1
        - bricks 6/0.5
        - iron_ingot 7/0.15
        - steel_ingot 8/0.05
   */
  
  private ItemGroup getRatioItemGroup(ConfigurationSection group) {
	  int minPoints = 1;
	  int maxPoints = 1;
	    ArrayList<NobilityItem> items = new ArrayList<NobilityItem>();
	    ArrayList<Double> ratios = new ArrayList<Double>();
	    ArrayList<Integer> pointValues = new ArrayList<Integer>();
	    boolean returnNull = false;
	    
	    if(group.getString("minPoints") != null) {
	      minPoints = group.getInt("minPoints");
	    } else {
	      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no minPoints!");
	      returnNull = true;
	    }
	    
	    if(group.getString("maxPoints") != null) {
		      maxPoints = group.getInt("maxPoints");
		    } else {
		      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no maxPoints!");
		      returnNull = true;
		    }
	    
	    ConfigurationSection list = group.getConfigurationSection("items");
	    if (group.isList("items")) {
	      List<String> stringItems = group.getStringList("items");
	      //TODO variable parsing here
	      for (String string : stringItems) {
	    	  String[] arr = string.split(" ");
	    	  String[] counts = arr[1].split("/");
	    	  
	    	  pointValues.add(Integer.parseInt(counts[0]));
	    	  ratios.add(Double.parseDouble(counts[1]));

	        try {
	          items.add(NobilityItems.getItemByName(arr[0]));
	        } catch (IllegalArgumentException e) {
	          Bukkit.getLogger().severe("itemGroup " + group.getCurrentPath() + " has invalid NobilityItem " + string + " in items!");
	          returnNull = true;
	        }
	      }
	    } else {
	      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no items!");
	      returnNull = true;
	    }
	    
	    if (returnNull) return null;
	    return new RatioItemGroup(items,ratios,pointValues,minPoints,maxPoints); 
	    
}

private SimpleItemGroup getSimpleItemGroup(ConfigurationSection group) {
    int selectionCount = 1;
    int minItemCount = 1;
    int maxItemCount = 1;
    
    ArrayList<NobilityItem> items = new ArrayList<NobilityItem>();
    boolean returnNull = false;
    
    if(group.getString("selectionCount") != null) {
      selectionCount = group.getInt("selectionCount");
    } else {
      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no selectionCount!");
      returnNull = true;
    }
    
    if(group.getString("minItemCount") != null) {
      minItemCount = group.getInt("minItemCount");
    } else {
      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no minItemCount!");
      returnNull = true;
    }
    
    if(group.getString("maxItemCount") != null) {
      maxItemCount = group.getInt("maxItemCount");
    } else {
      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no maxItemCount!");
      returnNull = true;
    }
    
    ConfigurationSection list = group.getConfigurationSection("items");
    if (group.isList("items")) {
      List<String> stringItems = group.getStringList("items");
      for (String string : stringItems) {
        try {
          items.add(NobilityItems.getItemByName(string));
        } catch (IllegalArgumentException e) {
          Bukkit.getLogger().severe("itemGroup " + group.getCurrentPath() + " has invalid NobilityItem " + string + " in items!");
          returnNull = true;
        }
      }
    } else {
      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no items!");
      returnNull = true;
    }
    
    if (returnNull) return null;
    return new SimpleItemGroup(items, selectionCount, minItemCount, maxItemCount);
    
  }
  
  public AbstractBlueprint getAbstractBlueprintFromItem(NobilityItem ni) {
	  for(AbstractBlueprint abp : this.blueprints) {
		  if(abp.result.equals(ni)) {
			  return abp;
		  }
	  }
	  return null;
  }

}
