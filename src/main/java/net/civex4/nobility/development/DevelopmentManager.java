package net.civex4.nobility.development;

import java.util.ArrayList;
import java.util.HashMap;

import net.civex4.nobility.developments.MiningCamp;

public class DevelopmentManager {
	private HashMap<String,DevelopmentBlueprint> blueprints;
	private HashMap<String,DevelopmentBlueprint> upgrades;

	public DevelopmentManager() { 
		blueprints = new HashMap<String,DevelopmentBlueprint>();
		blueprints = loadBlueprints();
		
		upgrades = new HashMap<String,DevelopmentBlueprint>();
		upgrades = loadUpgrades();
	}

	private HashMap<String,DevelopmentBlueprint> loadUpgrades() {
		HashMap<String,DevelopmentBlueprint> ret = new HashMap<String,DevelopmentBlueprint>();
		

		
		
		return ret;
	}
	
	public HashMap<String,DevelopmentBlueprint> getBlueprints(){
		return this.blueprints;
	}
	
	public HashMap<String,DevelopmentBlueprint> getUpgrades(){
		return this.upgrades;
	}

	private HashMap<String,DevelopmentBlueprint>loadBlueprints() {
		HashMap<String,DevelopmentBlueprint> ret = new HashMap<String,DevelopmentBlueprint>();
		
		DevelopmentBlueprint miningCamp = new DevelopmentBlueprint();
		miningCamp.result = new MiningCamp();
		
		ret.put(miningCamp.result.name,miningCamp);
		
		return ret;
	}
	
//	public void subtractUpkeep(DevelopmentType type, Estate estate) {		
//		if (type.getUpkeepCost() == null) return;
//		Inventory inventory = estate.getInventory();
//		for (ItemStack cost : type.getUpkeepCost()) {	
//			if (inventory.containsAtLeast(cost, cost.getAmount())) {
//				inventory.removeItem(cost);
//			} else {
//				estate.getDevelopment(type).deactivate();
//			}
//
//		}
//	}
//	
//	public boolean checkCosts(DevelopmentType type, Inventory inv) {		
//		if (type.getInitialCost().isEmpty()) return true;
//		if (inv == null) {
//			Bukkit.getLogger().warning("You can't check the cost of a null inventory");
//			return false;
//		}
//		for (ItemStack cost : type.getInitialCost()) {
//			if (!inv.containsAtLeast(cost, cost.getAmount())) {
//				return false;
//			}
//		}		
//		return true;   	
//	}
//	
//	public boolean checkCosts(DevelopmentType type, Estate estate) {
//		return checkCosts(type, estate.getInventory());
//	}
//	
//	public boolean subtractCosts(DevelopmentType type, Estate estate) {
//		Inventory inv = estate.getInventory();
//		return subtractCosts(type, inv);
//	}
//	
//	public boolean subtractCosts(DevelopmentType type, Inventory inv) {
//		if (type.getInitialCost() == null) return true;		
//		boolean ok = true;
//		for (ItemStack cost : type.getInitialCost()) {
//			inv.removeItem(cost);
//		} 
//		/* else {
//			Nobility.getNobility().warning("You cannot subtract costs if an inventory does not contain the costs");
//		} */
//		return ok;
//	}

	
}
