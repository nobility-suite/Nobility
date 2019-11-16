package net.civex4.nobility.development;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.estate.Estate;

public class DevelopmentManager {
	
	public DevelopmentManager() { }
	
	public void subtractUpkeep(DevelopmentType type, Estate estate) {		
		if (type.getUpkeepCost() == null) return;
		Inventory inventory = estate.getInventory();
		for (ItemStack cost : type.getUpkeepCost()) {
			if (inventory.containsAtLeast(cost, cost.getAmount())) {
				inventory.removeItem(cost);
			} else {
				estate.getDevelopment(type).deactivate();
			}

		}
	}
	
	public boolean checkCosts(DevelopmentType type, Inventory inv) {		
		if (type.getInitialCost().isEmpty()) return true;
		if (inv == null) {
			Bukkit.getLogger().warning("You can't check the cost of a null inventory");
			return false;
		}
		for (ItemStack cost : type.getInitialCost()) {
			if (!inv.containsAtLeast(cost, cost.getAmount())) {
				return false;
			}
		}		
		return true;   	
	}
	
	public boolean checkCosts(DevelopmentType type, Estate estate) {
		return checkCosts(type, estate.getInventory());
	}
	
	public boolean subtractCosts(DevelopmentType type, Estate estate) {
		Inventory inv = estate.getInventory();
		return subtractCosts(type, inv);
	}
	
	public boolean subtractCosts(DevelopmentType type, Inventory inv) {
		if (type.getInitialCost() == null) return true;		
		for (ItemStack cost : type.getInitialCost()) {
			if (checkCosts(type, inv)) {
				inv.removeItem(cost);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	
}
