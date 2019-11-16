package net.civex4.nobility.development;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.estate.Estate;

public class DevelopmentManager {
	
	public DevelopmentManager() { }
	
	public void subtractUpkeep(DevelopmentType type, Estate estate) {		
		if (type.getUpkeepCost() == null) return;		
		Inventory inventory = estate.getInventory();
		for (ItemStack cost : type.getUpkeepCost()) {
			inventory.remove(cost);
		}
	}
	
	public Boolean checkCosts(DevelopmentType developmentType, Estate estate) {
		for (ItemStack cost : developmentType.getInitialCost()) {
			if (!estate.getInventory().containsAtLeast(cost, cost.getAmount())) {
				return false;
			}
		}		
		return true;   	
	}
	
	public void subtractCosts(DevelopmentType type, Estate estate) {
		if (type.getInitialCost() == null) return;		
		for (ItemStack cost : type.getInitialCost()) {
			estate.getInventory().removeItem(cost);
		}  	
	}

	
}
