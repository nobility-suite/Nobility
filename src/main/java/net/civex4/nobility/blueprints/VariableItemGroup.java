package net.civex4.nobility.blueprints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.civex4.nobility.util.Util;
import net.civex4.nobilityitems.NobilityItem;

public class VariableItemGroup extends ItemGroup{

	public ArrayList<NobilityItem> items;
	public ArrayList<Integer> minCounts;
	public ArrayList<Integer> maxCounts;
	public int selectionCount;
	
	  public VariableItemGroup(ArrayList<NobilityItem> items, int selectionCount, ArrayList<Integer> minCounts,
		     ArrayList<Integer> maxCounts) {

		    this.items = items;
		    this.selectionCount = selectionCount;
		    this.minCounts = minCounts;
		    this.maxCounts = maxCounts;
	}
	
    public Map<NobilityItem,Integer> generate() {
		    Map<NobilityItem,Integer> ret = new HashMap<>();
		  
		    ArrayList<NobilityItem> selectItems = new ArrayList<>(items);
		    for(int i = 0; i < selectionCount; i++) {
		    	
		      int picker = Util.randrange(0, selectItems.size()-1);		  
		      int count = Util.randrange(minCounts.get(picker), maxCounts.get(picker));

		      NobilityItem select = selectItems.get(picker);
		      selectItems.remove(picker);
		      
		      ret.put(select,count);
		    }
		    
		    return ret;
		  }

	public int getDistinctTypes() {
		  return this.items.size();
	}

	@Override
	public String getItemName(int index) {
		NobilityItem ni = items.get(index);
		return ni.getDisplayName();
	}

}
