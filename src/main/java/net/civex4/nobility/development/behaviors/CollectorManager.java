package net.civex4.nobility.development.behaviors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.kingvictoria.RegionResource;
import io.github.kingvictoria.Region;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.estate.Estate;
import vg.civcraft.mc.civmodcore.itemHandling.ItemMap;

public class CollectorManager {

	public CollectorManager() { }
	
	public ItemMap getResourceAmount(Estate estate, Development development) {
		
		RegionResource collectorResource = development.getType().getResource();		
		if (collectorResource == null) {
			Bukkit.getLogger().warning(development.getType().getTitle() + "'s resource is not a valid resource. Use a RegionResource enum name");
			return null;
		}
		
		ItemMap itemMap = new ItemMap();
		ItemStack collectorItem = collectorResource.resource();
		int amount;		
		
		Collector collector = development.getCollector();
		double collectionPower = collector.getCollectionPower();
		double productivity = collector.getProductivity();
		Region region = estate.getRegion();
		double totalRegionResource = (double) region.getResource(development.getType().getResource());
		double totalCompetingCollectionPower = getTotalCollectionPower(estate, development);
		
		
		if (totalCompetingCollectionPower <= totalRegionResource) {
			amount = (int) Math.floor(collectionPower * productivity);
		} else {
			amount = (int) Math.floor((totalRegionResource * collectionPower * productivity) 
					/ totalCompetingCollectionPower);
		}
		
		itemMap.addItemAmount(collectorItem, amount);
		return itemMap;
	}
	
	private double getTotalCollectionPower(Estate estate, Development development) {
		double collectionPower = 0;
		for (Estate otherEstate : Nobility.getEstateManager().getEstates()) {
			if (estate.getRegion().equals(otherEstate.getRegion())) {
				for (Development otherDevelopment : otherEstate.getActiveDevelopments()) {
					if (development.getType().getResource()
							.equals(otherDevelopment.getType().getResource())) {
						int otherPower = otherDevelopment.getCollector().getCollectionPower();
						double otherProductivity = otherDevelopment.getCollector().getProductivity();
						collectionPower += ((double) otherPower) * otherProductivity;
					}
				}
			}
		}
		return collectionPower;
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
