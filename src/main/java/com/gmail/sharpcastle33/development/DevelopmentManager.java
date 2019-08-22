package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.estate.Estate;

public class DevelopmentManager {
    private List<DevelopmentRegister> types; // List of all types of developments

    public DevelopmentManager() {
        types = new ArrayList<>();
    } // constructor

    public void registerDevelopment(Class development, String name, Map<String, Integer> cost, Material icon, List<String> prerequisites) {
        types.add(new DevelopmentRegister(development, name, cost, icon, prerequisites));
    } // registerDevelopment

    public List<DevelopmentRegister> getTypes() {
        return types;
    } // getTypes
    
    public int calculateGains(Estate estate, String resource) {
    	double regionResource = estate.getRegion().getResource(resource);
    	double level = 1; //Add to the development class
    	double maximum = 5; //5 is arbitrary. Tweak later on.
    	double totalCompetingEstates = 0;
    	int returnAmount;
    	
    	for (Estate otherEstate : Nobility.estateMan.estates) {
    		if (estate.getRegion().equals(otherEstate.getRegion())) {    			
    			totalCompetingEstates++; //multiply by each estate's level modifier later on
    			//level can be 0 if the development is inactive
    		}
    	}
    	
    	if (totalCompetingEstates * maximum <= regionResource) {
    		returnAmount = (int) (maximum * level);
    		return returnAmount;

    	}
    	
    	if (totalCompetingEstates * maximum > regionResource) {
    		returnAmount = (int) ((regionResource * level) / totalCompetingEstates);
    		return returnAmount;
    	} else {    		
    		return 0;
    	}    	
    }
    
    //Change if storehouse inventory and creation is changed
    public Inventory getStorehouseInventory(Estate estate) {
    	Block storehouse = estate.getBlock().getLocation().add(0, 0, -1).getBlock();    	
    	if(storehouse.getState() instanceof InventoryHolder){
    		InventoryHolder ih = (InventoryHolder) storehouse.getState();
    		Inventory storehouseInventory = ih.getInventory();
        	return storehouseInventory;
    	} else {
    		return null;
    	}    	
    }
    
    
    
} // class
