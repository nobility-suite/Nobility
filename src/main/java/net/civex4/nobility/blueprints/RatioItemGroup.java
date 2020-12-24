package net.civex4.nobility.blueprints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.civex4.nobilityitems.NobilityItem;

public class RatioItemGroup extends ItemGroup{
	public ArrayList<NobilityItem> items; //List of potential items
	public ArrayList<Double> ratios; //Percentage of each item, by index
	public ArrayList<Integer> pointValues; //Relative value of each item, by index
	public int minPoints; //total cost of the item group
	public int maxPoints; //total cost of the item group
	
	  public RatioItemGroup(ArrayList<NobilityItem> items, int selectionCount, ArrayList<Double> ratios, ArrayList<Integer> pointValues, int minPoints, int maxPoints) {

		    this.items = items;
		    this.ratios = ratios;
		    this.pointValues = pointValues;
		    this.minPoints = minPoints;
		    this.maxPoints = maxPoints;

	}
	
    public Map<NobilityItem,Integer> generate() {
		    Map<NobilityItem,Integer> ret = new HashMap<>();
		    
		    Random rand = new Random();
		    
		    int totalPoints = rand.nextInt(maxPoints-minPoints) + minPoints;
		  
		    ArrayList<NobilityItem> selectItems = new ArrayList<>(items);
		    for(int i = 0; i < selectItems.size(); i++) {
		    	if(ratios.get(i) >= 0.01) {
		    		int pointsToSpend = (int) (totalPoints * ratios.get(i));
		    		int itemCount = (int) pointsToSpend/pointValues.get(i);
		    		if(itemCount > 0) { ret.put(selectItems.get(i), itemCount); }
		    	}
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
