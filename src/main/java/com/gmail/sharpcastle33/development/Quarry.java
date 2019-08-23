package com.gmail.sharpcastle33.development;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;

public class Quarry extends Development {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate() {
    	Location loc = estate.getBlock().getLocation().add(1, 0, -1);
		loc.getBlock().setType(Material.STONECUTTER);
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tick() {
		Nobility.getDevelopmentManager().getStorehouseInventory(estate).addItem(getResourceAmount());
	}
	
	//Change if time bank changes
    private ItemStack getResourceAmount() {
    	int returnAmount = Nobility.getDevelopmentManager().calculateGains(estate, "Stone");
		ItemStack returnStack = new ItemStack(Material.SMOOTH_STONE, returnAmount);
		return returnStack;    		
    }

}
