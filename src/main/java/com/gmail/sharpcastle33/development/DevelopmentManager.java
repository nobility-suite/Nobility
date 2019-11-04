package com.gmail.sharpcastle33.development;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.estate.Estate;

public class DevelopmentManager {
	//private List<Development> types; // List of all types of developments

	
	public DevelopmentManager() {
		//types = new ArrayList<>();
	} // constructor
	
	public int calculateGains(Estate estate, String resource, Development development) {
		double regionResource = estate.getRegion().getResource(resource);
		double level = 1; //Add to the development class
		double maximum = 10; //arbitrary. Tweak later on.
		double rounded;
		double totalCompetingEstates = 0;
		double productivity = development.getProductivity();
		int returnAmount;
		
		//Calculate productivity by counting the other estates with the same development in the same region
		for (Estate otherEstate : Nobility.estateMan.estates) {
			if (estate.getRegion().equals(otherEstate.getRegion())) {
				for (Development otherDevelopment : otherEstate.getActiveDevelopments()) {
					if (development.getDevelopmentType().getName().equalsIgnoreCase(otherDevelopment.getDevelopmentType().getName())) {
						totalCompetingEstates += level * otherDevelopment.getProductivity();
					}
				} //multiply by each estate's level modifier later on
			}
		}
		
		if (totalCompetingEstates * maximum <= regionResource) {
			rounded = Math.floor(maximum * level * productivity);
			returnAmount = (int) rounded;
			return returnAmount;
		} else if (totalCompetingEstates * maximum > regionResource) {
			rounded = Math.floor((regionResource * level * productivity) / totalCompetingEstates);
			returnAmount = (int) rounded;
			return returnAmount;
		} else {			
			return 0;
		}
	}
	
	//Change if storehouse inventory and creation is changed
	public Inventory getStorehouseInventory(Estate estate) {
		Block storehouse = estate.getBlock().getLocation().add(1, 0, 0).getBlock();		
		if(storehouse.getState() instanceof InventoryHolder){
			InventoryHolder ih = (InventoryHolder) storehouse.getState();
			Inventory storehouseInventory = ih.getInventory();
			return storehouseInventory;
		} else {
			return null;
		}		
	}
		
	public Boolean checkCosts(DevelopmentType developmentType, Estate estate) {
		for (ItemStack cost : developmentType.getInitialCost()) {
			if (!getStorehouseInventory(estate).containsAtLeast(cost, cost.getAmount())) {
				return false;
			}
		}		
		return true;   	
	}
	
	public void subtractCosts(DevelopmentType type, Estate estate) {
		if(type.getInitialCost() == null) return;		
		for (ItemStack cost : type.getInitialCost()) {
			getStorehouseInventory(estate).removeItem(cost);
		}  	
	}
	
	public void subtractUpkeep(DevelopmentType type, Estate estate) {
		
		if (type.getUpkeepCost() == null) return;
		
		Inventory inventory = getStorehouseInventory(estate);
		for (ItemStack cost : type.getUpkeepCost()) {
			inventory.remove(cost);
		}
		
		
		/*
		 * The plan for this is the development's productivity will
		 * increase in yield if there is a variety of upkeep items
		 * 
		 * I also want to abstract the upkeep costs to something more
		 * general like "food" instead of "WHEAT"
		 */
		//double productivity = .4d;
		
		
		/*
		if (development.getDevelopmentType().getUpkeepCost().containsKey("Food")) {	
			int foodCost = register.getCost().get("Food");
			/* Broken code
			 * Might fix later
			int foodAmount = countItems(Material.WHEAT, inventory) + countItems(Material.COOKED_BEEF, inventory) + countItems(Material.SWEET_BERRIES, inventory);
			
			if (foodCost > foodAmount) {
				development.deactivate();
				development.setActive(false);
				return;
			}
			
			boolean wheatBoost = true;
			boolean beefBoost = true;
			boolean berryBoost = true;
			int random = (int) (Math.floor(Math.random() * 3d));
			int total = 3;
			for (int i = 0 + random; i < total + random; i++) {
				int removeAmount = foodCost / 3;
				if (i % 3 == 0) {
					if (!inventory.removeItem(new ItemStack(Material.WHEAT, removeAmount)).isEmpty()) {
						wheatBoost = false;
						total++;
					}
				} else if (i % 3 == 1) {
					if (!inventory.removeItem(new ItemStack(Material.COOKED_BEEF, removeAmount)).isEmpty()) {
						beefBoost = false;
						total++;
					}
				} else if (i % 3 == 2) {
					if (!inventory.removeItem(new ItemStack(Material.SWEET_BERRIES, removeAmount)).isEmpty()) {
						berryBoost = false;
						total++;
					}
				}
				i++;
			}
			if (wheatBoost == true) productivity += .2;
			if (beefBoost == true) productivity +=.2;
			if (berryBoost == true) productivity +=.2;
			
			
			//Current replacement
			ItemStack[] listOfFoods = new ItemStack[3];
			listOfFoods[0] = new ItemStack(Material.COOKED_BEEF);
			listOfFoods[1] = new ItemStack(Material.WHEAT);
			listOfFoods[2] = new ItemStack(Material.SWEET_BERRIES);
			for (int i = 0; i < listOfFoods.length; i++) {
				if (inventory.containsAtLeast(listOfFoods[i], foodCost)) {
					listOfFoods[i].setAmount(foodCost);
					inventory.removeItem(listOfFoods[i]);
					productivity += .2d;
				}
			}			
		}
		
		if (register.getCost().containsKey("Hardware")) {
			Bukkit.broadcastMessage("There is a hardware cost");
			ItemStack[] listOfHardwares = new ItemStack[3];
			int hardwareCost = register.getCost().get("Hardware");
			listOfHardwares[0] = new ItemStack(Material.IRON_INGOT);
			listOfHardwares[1] = new ItemStack(Material.SMOOTH_STONE);
			listOfHardwares[2] = new ItemStack(Material.OAK_LOG);
			for (int i = 0; i < listOfHardwares.length; i++) {
				if (inventory.containsAtLeast(listOfHardwares[i], hardwareCost)) {
					listOfHardwares[i].setAmount(hardwareCost);
					inventory.removeItem(listOfHardwares[i]);
					Bukkit.broadcastMessage("Enough hardware");
				} else {
					development.deactivate();
					development.setActive(false);
					Bukkit.broadcastMessage("Not enough hardware");
				}
			}
		}
		*/
		
		//Bukkit.broadcastMessage("The productivity of " + development.getName() + " is " + productivity);
		//development.setProductivity(productivity);
	}
	
	public int countItems(Material material, Inventory inventory) {
		int number = 0;
		for (ItemStack item : inventory.getContents()) {
			if (item != null && item.getType() == material) {
				number += item.getAmount();
			}
		}
		return number;		
	}
	
} // class
