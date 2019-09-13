package com.gmail.sharpcastle33.development;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;

public class VictoryPoint extends Development{

	@Override
    public void init() {
        // TODO
    }

    @Override
    public void activate() {
    	Location loc = estate.getBlock().getLocation().add(2, 0, 1);
		loc.getBlock().setType(Material.BELL);
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
    	int returnAmount = (int) (this.register.getProductivity() * 10d);
		ItemStack returnStack = new ItemStack(Material.BELL, returnAmount);
		return returnStack;
    }
}
