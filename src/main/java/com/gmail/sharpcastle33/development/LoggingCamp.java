package com.gmail.sharpcastle33.development;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;

public class LoggingCamp extends Development {

    @Override
    public void init() {
        // TODO
    }

    @Override
    public void activate() {
    	Location loc = estate.getBlock().getLocation().add(0, 0, 1);
		loc.getBlock().setType(Material.OAK_LOG);
    }

    @Override
    public void deactivate() {
        // TODO
    }
    
    @Override
    public void tick() {	
    	Nobility.getDevelopmentManager().getStorehouseInventory(estate).addItem(getResourceAmount());
    }
    

    
    //Change if time bank changes
    private ItemStack getResourceAmount() {
    	int returnAmount = Nobility.getDevelopmentManager().calculateGains(estate, "Log");
		ItemStack returnStack = new ItemStack(Material.OAK_LOG, returnAmount);
		return returnStack;    		
    }
}
