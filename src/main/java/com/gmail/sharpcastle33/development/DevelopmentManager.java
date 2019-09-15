package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.estate.Estate;

public class DevelopmentManager {
    private List<DevelopmentRegister> types; // List of all types of developments

    
    public DevelopmentManager() {
        types = new ArrayList<>();
    } // constructor

    public void registerDevelopment(Class development, String name, Map<String, Integer> cost, Material icon, List<String> prerequisites, List<ItemStack> initialCost, String resource) {
        types.add(new DevelopmentRegister(development, name, cost, icon, prerequisites, initialCost, resource));
        
    } // registerDevelopment

    public List<DevelopmentRegister> getTypes() {
        return types;
    } // getTypes
    
    public int calculateGains(Estate estate, String resource, Development development) {
    	double regionResource = estate.getRegion().getResource(resource);
    	double level = 1; //Add to the development class
    	double maximum = 10; //5 is arbitrary. Tweak later on.
    	double rounded;
    	double totalCompetingEstates = 0;
    	double productivity = development.getRegister().getProductivity();
    	int returnAmount;
    	
    	for (Estate otherEstate : Nobility.estateMan.estates) {
    		if (estate.getRegion().equals(otherEstate.getRegion())) {
    			for (Development otherDevelopment : otherEstate.getActiveDevelopments()) {
    				//Bukkit.broadcastMessage("Development: " + otherDevelopment.getRegister().getName());
    				if (development.getRegister().getName().equalsIgnoreCase(otherDevelopment.getRegister().getName())) {
    					//Bukkit.broadcastMessage("Developments equal");
    					totalCompetingEstates += level * otherDevelopment.getRegister().getProductivity();
    				}
    			} //multiply by each estate's level modifier later on
    		}
    	}
    	Bukkit.broadcastMessage("Number of competing estates: " + totalCompetingEstates);
    	if (totalCompetingEstates * maximum <= regionResource) {
    		rounded = Math.floor(maximum * level * productivity);
    		returnAmount = (int) rounded;
    		return returnAmount;
    	}
    	
    	if (totalCompetingEstates * maximum > regionResource) {
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
    
    public Boolean checkCosts(DevelopmentRegister register, Estate estate) {
		for (ItemStack cost : register.getInitialCost()) {
    		if (!getStorehouseInventory(estate).containsAtLeast(cost, cost.getAmount())) {
    			return false;
    		}
		}		
    	return true;   	
    }
    
    public void subtractCosts(DevelopmentRegister register, Estate estate) {
		if(register.getInitialCost() == null) return;		
    	for (ItemStack cost : register.getInitialCost()) {
			Nobility.getDevelopmentManager().getStorehouseInventory(estate).removeItem(cost);
		}  	
    }
    
    public void subtractUpkeep(DevelopmentRegister register, Estate estate, Development development) {
    	
    	if (register.getCost() == null) return;
    	
    	Inventory inventory = getStorehouseInventory(estate);
		double productivity = .4d;
    	
    	if (register.getCost().containsKey("Food")) {	
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
    		*/
    		
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
    	
    	//Bukkit.broadcastMessage("The productivity of " + development.getName() + " is " + productivity);
    	register.setProductivity(productivity);
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
