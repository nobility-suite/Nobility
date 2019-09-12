package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void registerDevelopment(Class development, String name, Map<String, Integer> cost, Material icon, List<String> prerequisites, List<ItemStack> initialCost) {
        types.add(new DevelopmentRegister(development, name, cost, icon, prerequisites, initialCost));
        
    } // registerDevelopment

    public List<DevelopmentRegister> getTypes() {
        return types;
    } // getTypes
    
    public int calculateGains(Estate estate, String resource, Development development) {
    	double regionResource = estate.getRegion().getResource(resource);
    	double level = 1; //Add to the development class
    	double maximum = 10; //5 is arbitrary. Tweak later on.
    	double totalCompetingEstates = 0;
    	double rounded;
    	double productivity = development.getRegister().getProductivity();
    	int returnAmount;
    	
    	for (Estate otherEstate : Nobility.estateMan.estates) {
    		if (estate.getRegion().equals(otherEstate.getRegion())) {
    			for (Development otherDevelopment : otherEstate.getActiveDevelopments()) {
    				if (development.getName() == otherDevelopment.getName()) {
    					totalCompetingEstates =+ totalCompetingEstates * otherDevelopment.getRegister().getProductivity();
    				}
    			} //multiply by each estate's level modifier later on
    		}
    	}
    	
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
    		if (!getStorehouseInventory(estate).contains(cost, cost.getAmount())) {
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
    	
    	if (register.getCost().containsKey("food")) {
    		int foodAmount = countItems(Material.WHEAT, inventory) + countItems(Material.COOKED_BEEF, inventory) + countItems(Material.SWEET_BERRIES, inventory);
    		int foodCost = register.getCost().get("food");
    		if (foodCost > foodAmount) {
    			development.deactivate();
    			development.setActive(false);
    		}
    		
    		boolean wheatBoost = true;
    		boolean beefBoost = true;
    		boolean berryBoost = true;
    		
    		for (int i = 0; i < foodCost; i++) {
    			
    			if (i % 3 == 0) {
    				if (inventory.removeItem(new ItemStack(Material.WHEAT, 1)).isEmpty()) {
    					foodCost++;
    					wheatBoost = false;
    				}
    			} else if (i % 3 == 1) {
    				if (inventory.removeItem(new ItemStack(Material.COOKED_BEEF, 1)).isEmpty()) {
    					foodCost++;
    					beefBoost = false;
    				}
    			} else if (i % 3 == 2) {
    				if (inventory.removeItem(new ItemStack(Material.SWEET_BERRIES, 1)).isEmpty()) {
    					foodCost++;
    					berryBoost = false;
    				}
    			}
    			i++;
    		}
    		if (wheatBoost == true) productivity += .2;
    		if (beefBoost == true) productivity +=.2;
    		if (berryBoost == true) productivity +=.2;
    	}
    	
    	register.setProductivity(productivity);
    }
    
    private int countItems(Material material, Inventory inventory) {
    	int number = 0;
		for (ItemStack item : inventory.getContents()) {
			if (item.getType() == material) {
				number += item.getAmount();
			}
		}
    	return number;    	
    }
    
} // class
