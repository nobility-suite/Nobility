package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.estate.Estate;

public class Butcher extends Development {

    @Override
    public void init() {
    	//TODO
    }

    @Override
    public void activate() {
    	//<Temporary Code>
    	//Visual indication storehouse is built
    	Location loc = estate.getBlock().getLocation().add(-1, 0, 1);
		loc.getBlock().setType(Material.SMOKER);
		//</Temporary Code>
    }

    @Override
    public void deactivate() {
        // TODO
    }

	@Override
	public void tick() {
		Nobility.getDevelopmentManager().getStorehouseInventory(estate).addItem(getMeatAmount());	
	}
    
	private ItemStack getMeatAmount() {
		int returnAmount = Nobility.getDevelopmentManager().calculateGains(estate, "Beef");
		ItemStack returnStack = new ItemStack(Material.COOKED_BEEF, returnAmount);
		return returnStack;    		
    }
    
}