package net.civex4.nobility.blueprints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import net.civex4.nobility.util.Util;

public class AbstractBlueprint {
  
  private ArrayList<ItemGroup> groups;
  public ItemStack result;
  public int minRuns;
  public int maxRuns;
  public String name;
  public int minReturn;
  public int maxReturn;
  
  
  /**
   * Create a blueprint using the parameters of an AbstractBlueprint
   * @return Blueprint
   */
  public Blueprint generate() {
    Map<ItemStack, Integer> ingredients = new HashMap<ItemStack, Integer>();
    
    int resultAmount = Util.randrange(minReturn, maxReturn);
    int runs = Util.randrange(minRuns, maxRuns);
    
    //For each ItemGroup g, generate their result, and add all results to ingredients map.
    for(ItemGroup g : groups) {
      Map<ItemStack, Integer> generated = g.generate();
      
      for(ItemStack i : generated.keySet()) {
        ingredients.put(i, generated.get(i));
      }
    }
    
    Blueprint ret = new Blueprint(result, (HashMap<ItemStack, Integer>) ingredients, runs, name, resultAmount);
    return ret;
  }

}
