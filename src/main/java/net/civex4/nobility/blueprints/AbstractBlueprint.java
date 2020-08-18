package net.civex4.nobility.blueprints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.civex4.nobility.util.Util;
import net.civex4.nobilityitems.NobilityItem;

public class AbstractBlueprint {
  
  private ArrayList<ItemGroup> groups;
  public NobilityItem result;
  public int minRuns;
  public int maxRuns;
  public String name;
  public int minReturn;
  public int maxReturn;
  
  
  public AbstractBlueprint(ArrayList<ItemGroup> groups, NobilityItem result, int minRuns, int maxRuns,
      String name, int minReturn, int maxReturn) {
    super();
    this.groups = groups;
    this.result = result;
    this.minRuns = minRuns;
    this.maxRuns = maxRuns;
    this.name = name;
    this.minReturn = minReturn;
    this.maxReturn = maxReturn;
  }


  /**
   * Create a blueprint using the parameters of an AbstractBlueprint
   * @return Blueprint
   */
  public Blueprint generate() {
    Map<NobilityItem, Integer> ingredients = new HashMap<>();
    
    int resultAmount = Util.randrange(minReturn, maxReturn);
    int runs = Util.randrange(minRuns, maxRuns);
    
    //For each ItemGroup g, generate their result, and add all results to ingredients map.
    for(ItemGroup g : groups) {
      Map<NobilityItem, Integer> generated = g.generate();
      
      for(NobilityItem i : generated.keySet()) {
        ingredients.put(i, generated.get(i));
      }
    }
    
    Blueprint ret = new Blueprint(result, (HashMap<NobilityItem, Integer>) ingredients, runs, name, resultAmount);
    return ret;
  }

}
