package com.gmail.sharpcastle33.development;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.estate.Estate;

public class Granary extends Development {

    @Override
    public void init() {
        // TODO
    }

    @Override
    public void activate() {
    	Location loc = estate.getBlock().getLocation().add(0, 0, 1);
		loc.getBlock().setType(Material.HAY_BLOCK);
    }

    @Override
    public void deactivate() {
        // TODO
    }
    
    @Override
    public void tick() {	
    	getStorehouseInventory().addItem(calculateGains(estate));
    }
    
    //Change if storehouse inventory and creation is changed
    private Inventory getStorehouseInventory() {
    	Block storehouse = estate.getBlock().getLocation().add(0, 0, -1).getBlock();    	
    	if(storehouse.getState() instanceof InventoryHolder){
    		InventoryHolder ih = (InventoryHolder) storehouse.getState();
    		Inventory storehouseInventory = ih.getInventory();
        	return storehouseInventory;
    	} else {
    		return null;
    	}    	
    }
    
    //Change if time bank changes
    private ItemStack calculateGains(Estate estate) {
    	double regionWheat = estate.getRegion().getResource("Wheat");
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
    	
    	if (totalCompetingEstates * maximum <= regionWheat) {
    		returnAmount = (int) (maximum * level);
    		ItemStack returnStack = new ItemStack(Material.WHEAT, returnAmount);
    		return returnStack;
    	}
    	
    	if (totalCompetingEstates * maximum > regionWheat) {
    		returnAmount = (int) ((regionWheat * level) / totalCompetingEstates);
    		ItemStack returnStack = new ItemStack(Material.WHEAT, returnAmount);
    		return returnStack;    		
    	} else {    		
    		return null;
    	}    	
    }
}
