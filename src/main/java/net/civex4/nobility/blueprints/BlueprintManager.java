package net.civex4.nobility.blueprints;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
    // TODO Auto-generated method stub
    Bukkit.getServer().getLogger().info("Loading Blueprints...");
    ArrayList<AbstractBlueprint> ret = new ArrayList<AbstractBlueprint>();
    
    for(String blueprintKey : config.getKeys(false)) {
      ret.add(loadBlueprint(config,blueprintKey));
    }

    return ret;
  }
  
  private AbstractBlueprint loadBlueprint(FileConfiguration config, String key) {
    return null;
  }

}
