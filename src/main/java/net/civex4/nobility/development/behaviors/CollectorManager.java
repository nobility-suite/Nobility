package net.civex4.nobility.development.behaviors;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.estate.Estate;

public class CollectorManager {

	public CollectorManager() { }
	
	public ItemStack getResourceAmount(Estate estate, Development development) {   
		//TODO: Get the returnMaterial by checking the region (e.g. if region is jungle and resource is wood, return jungle logs)
		String resource = development.getType().getResource();		
		Material returnMaterial = Material.matchMaterial(resource);
		int returnAmount = calculateGains(estate, development);
		return new ItemStack(returnMaterial, returnAmount);
    }
	
	public int calculateGains(Estate estate, Development development) {
		Collector collector = development.getCollector();
		String resource = development.getType().getResource();
		double regionResource = estate.getRegion().getResource(resource);
		double collectionPower = collector.getCollectionPower();
		double totalCompetingCollectionPower = 0;
		double productivity = collector.getProductivity();
		
		for (Estate otherEstate : Nobility.getEstateManager().getEstates()) {
			if (estate.getRegion().equals(otherEstate.getRegion())) {
				for (Development otherDevelopment : otherEstate.getActiveDevelopments()) {
					if (development.getType().getResource()
							.equalsIgnoreCase(otherDevelopment.getType().getResource())) {
						totalCompetingCollectionPower += otherDevelopment.getCollector().getProductivity();
					}
				}
			}
		}
		
		if (totalCompetingCollectionPower <= regionResource) {
			return (int) Math.floor(collectionPower * productivity);
		} else {
			return (int) Math.floor((regionResource * collectionPower * productivity) 
					/ totalCompetingCollectionPower);
		}
	}
	
	public int countItems(Material material, Inventory inventory) {
		int number = 0;
		for (ItemStack item : inventory.getContents()) {
			if (item != null && item.getType() == material) {
				number += item.getAmount();
			}
		}
		return number;		
	}

}
