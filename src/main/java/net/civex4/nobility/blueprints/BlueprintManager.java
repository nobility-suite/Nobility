package net.civex4.nobility.blueprints;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class BlueprintManager {
  
  public ArrayList<AbstractBlueprint> blueprints;
  static FileConfiguration blueprintConfig;
  
  public void init(File config) {
    
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
      ret.add(loadBlueprint(config,blueprintKey));
    }

    return ret;
  }
  
  private AbstractBlueprint loadBlueprint(FileConfiguration config, String key) {
    ConfigurationSection header = config.getConfigurationSection(key);

    ItemStack result = null;
    int minRuns = 0;
    int maxRuns = 0;
    String name = "";
    int minReturn = 0;
    int maxReturn = 0;
    
    ArrayList<ItemGroup> itemGroups = new ArrayList<ItemGroup>();
    
    
    if (header.getString("name") != null) {
      name = header.getString("name");
    }
    
    if (header.getString("minRuns") != null) {
      minRuns = header.getInt("minRuns");
    }
    
    if (header.getString("minReturn") != null) {
      minReturn = header.getInt("minReturn");
    }
    
    if (header.getString("maxRuns") != null) {
      maxRuns = header.getInt("maxRuns");
    }
    
    if (header.getString("maxReturn") != null) {
      maxReturn = header.getInt("maxReturn");
    }
    
    if (header.getString("result") != null) {
      result = header.getItemStack("result");
    }
    
    ConfigurationSection groups = header.getConfigurationSection("itemGroups");
    
    itemGroups = getItemGroups(groups);
    
    return new AbstractBlueprint(itemGroups, result, minRuns, maxRuns, name, minReturn, maxReturn); //TODO
  }

  private ArrayList<ItemGroup> getItemGroups(ConfigurationSection groups) {
    // TODO Auto-generated method stub
    ArrayList<ItemGroup> ret = new ArrayList<ItemGroup>();
    
    for(String groupKey : groups.getKeys(false)) {
      ConfigurationSection group = groups.getConfigurationSection(groupKey);
      
      if(group.getString("type") != null) {
        String type = group.getString("type");
        
        if(type.equalsIgnoreCase("SIMPLE")) {
          ItemGroup g = getSimpleItemGroup(group);
          ret.add(g);
        }
        
        /*
         * add more cases here
         */
      }
    }
    
    return ret;
  }
  
  private SimpleItemGroup getSimpleItemGroup(ConfigurationSection group) {
    int selectionCount = 1;
    int minItemCount = 1;
    int maxItemCount = 1;
    
    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
    
    if(group.getString("selectionCount") != null) {
      selectionCount = group.getInt("selectionCount");
    }
    
    if(group.getString("minItemCount") != null) {
      minItemCount = group.getInt("minItemCount");
    }
    
    if(group.getString("maxItemCount") != null) {
      maxItemCount = group.getInt("maxItemCount");
    }
    
    //TODO load itemstacks
    
    return new SimpleItemGroup(items, selectionCount, minItemCount, maxItemCount);
    
  }

}
