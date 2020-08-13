package net.civex4.nobility.blueprints;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;

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

    ItemStack result = null;
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
    
    if (header.getString("result") != null) {
      result = header.getItemStack("result");
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
      
      if(group.getString("type") != null) {
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
  
  private SimpleItemGroup getSimpleItemGroup(ConfigurationSection group) {
    int selectionCount = 1;
    int minItemCount = 1;
    int maxItemCount = 1;
    
    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
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
    if (list != null) {
      items = getItemStackList(list);
    } else {
      Bukkit.getLogger().warning("itemGroup " + group.getCurrentPath() + " has no items section!");
      returnNull = true;
    }
    
    if (returnNull) return null;
    return new SimpleItemGroup(items, selectionCount, minItemCount, maxItemCount);
    
  }
  
  private ArrayList<ItemStack> getItemStackList(ConfigurationSection list){
    ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    
    for(String itemKey : list.getKeys(false)) {
      ItemStack i = list.getItemStack(itemKey);
      ret.add(i);
    }
    
    return ret;
  }

}
