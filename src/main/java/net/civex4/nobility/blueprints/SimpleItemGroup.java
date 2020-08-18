package net.civex4.nobility.blueprints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.civex4.nobility.util.Util;
import net.civex4.nobilityitems.NobilityItem;

public class SimpleItemGroup extends ItemGroup{
  
  /**
   * An ItemGroup is a group of items from which a selection is made to generate a Blueprint.
   * ItemGroups contain some number of items, a selection count of how many items to choose,
   * and some number detailing how large itemstack sizes should be.
   * 
   * Simple item groups make at random X selections from their list, with each selection containing a stack of between Y and Z items.
   * They do not have any sort of weighting. The same item name cannot be selected twice.
   */
  
  public ArrayList<NobilityItem> items;
  public int selectionCount;
  public int itemCountMin;
  public int itemCountMax;
  
  public SimpleItemGroup(ArrayList<NobilityItem> items, int selectionCount, int itemCountMin,
      int itemCountMax) {

    this.items = items;
    this.selectionCount = selectionCount;
    this.itemCountMin = itemCountMin;
    this.itemCountMax = itemCountMax;
  }

  public void setSelectionCount(int s) {
    this.selectionCount = s;
  }
  
  public void setItemCount(int min, int max) {
    this.itemCountMax = max;
    this.itemCountMin = min;
  }
  
  public Map<NobilityItem,Integer> generate() {
    Map<NobilityItem,Integer> ret = new HashMap<>();
  
    for(int i = 0; i < selectionCount; i++) {
      int count = Util.randrange(itemCountMin, itemCountMax);
      int picker = Util.randrange(0, items.size()-1);
      
      NobilityItem select = items.get(picker);
      items.remove(picker);
      
      ret.put(select,count);
    }
    
    return ret;
  }

}
