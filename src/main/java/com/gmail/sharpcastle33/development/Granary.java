package com.gmail.sharpcastle33.development;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;

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
    	Nobility.getDevelopmentManager().getStorehouseInventory(estate).addItem(getWheatAmount());
    }
    

    
    //Change if time bank changes
    private ItemStack getWheatAmount() {
    	int returnAmount = Nobility.getDevelopmentManager().calculateGains(estate, "Wheat", this);
		ItemStack returnStack = new ItemStack(Material.WHEAT, returnAmount);
		return returnStack;    		
    }
}
