package com.gmail.sharpcastle33.development;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.estate.Estate;

public class Collector implements Developer{
	
	private Estate estate;
	private Development development;
	
	public Collector(Estate estate, Development development) {
		this.estate = estate;
		this.development = development;
	}

	@Override
	public void build() {
		
	}

	@Override
	public void tick() {
		Nobility.getDevelopmentManager().getStorehouseInventory(estate).addItem(getResourceAmount());				
	}
	
	private ItemStack getResourceAmount() {   
		//TODO: Get the returnMaterial by checking the region (e.g. if region is jungle and resource is wood, return jungle logs)
		String resource = development.getDevelopmentType().getResource();		
		Material returnMaterial = Material.matchMaterial(resource);
    	int returnAmount = Nobility.getDevelopmentManager().calculateGains(estate, resource, development);
		ItemStack returnStack = new ItemStack(returnMaterial, returnAmount);
		return returnStack;
    }

}
