package com.gmail.sharpcastle33.development;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;

public class Gatherer extends Development {

	@Override
    public void init() {
        // TODO
    }

    @Override
    public void activate() {
    	Location loc = estate.getBlock().getLocation().add(1, 0, 1);
		loc.getBlock().setType(Material.SWEET_BERRY_BUSH);
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
    	int returnAmount = Nobility.getDevelopmentManager().calculateGains(estate, "Fruit", this);
		ItemStack returnStack = new ItemStack(Material.SWEET_BERRIES, returnAmount);
		return returnStack;    		
    }

}
