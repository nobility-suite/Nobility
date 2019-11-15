package net.civex4.nobility.development;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;

public class Collector implements DevelopmentBehavior{
	
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
		return new ItemStack(returnMaterial, returnAmount);
    }

	@Override
	public List<Clickable> getClickables() {
		// INFO
		return null;
		// 
	}

}
