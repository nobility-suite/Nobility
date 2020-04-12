package net.civex4.nobility.development.behaviors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.kingvictoria.RegionResource;
import io.github.kingvictoria.Region;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.customItem.CustomItem;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.estate.Estate;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.itemHandling.ItemMap;

public class CollectorManager {
//  
//    /*
//     * ================
//     * TODO Deprecate this class, replace with new mine based system instead of collection  power system.
//     * 
//     * 
//     * IMPORTANT
//     * ================
//     */
//
//	public CollectorManager() { }
//	
//	/* For competing estates, the numbers don't add up to the region total because they round down.
//	 * TODO Subtract each estate yield amount from the region total to get the remainder
//	 * For each remainder, give to random estate weighted by the amount they got rounded down
//	 */
//	
//	public ItemMap getResourceAmount(Estate estate, Development development) {
//		
//		RegionResource collectorResource = development.getType().getResource();		
//		if (collectorResource == null) {
//			Bukkit.getLogger().warning(development.getType().getTitle() + "'s resource is not a valid resource. Use a RegionResource enum name");
//			return null;
//		}
//		
//		ItemMap itemMap = new ItemMap();
//		ItemStack collectorItem = collectorResource.resource();
//		int amount;		
//		
//		Collector collector = development.getCollector();
//		double collectionPower = collector.getCollectionPower();
//		double productivity = collector.getProductivity();
//		Region region = estate.getRegion();
//		double totalRegionResource = (double) region.getResource(development.getType().getResource());
//		double totalCompetingCollectionPower = getTotalCollectionPower(estate, development);
//		
//		
//		if (totalCompetingCollectionPower <= totalRegionResource) {
//			amount = (int) Math.floor(collectionPower * productivity);
//		} else {
//			amount = (int) Math.floor((totalRegionResource * collectionPower * productivity) 
//					/ totalCompetingCollectionPower);
//		}
//		switch (collectorResource) {
//		case IRON:
//			break;
//		case STONE:
//			break;
//		case WHEAT:
//			break;
//		case WOOD:
//			break;
//		default:
//			break;
//		}
//			
//		itemMap.addItemAmount(collectorItem, amount);
//		return itemMap;
//
//	}
//	
//	public double getTotalCollectionPower(Estate estate, Development development) {
//		double collectionPower = 0;
//		for (Estate otherEstate : Nobility.getEstateManager().getEstates()) {
//			if (estate.getRegion().equals(otherEstate.getRegion())) {
//				for (Development otherDevelopment : otherEstate.getActiveDevelopments()) {
//					if (development.getType().getResource()
//							.equals(otherDevelopment.getType().getResource())) {
//						int otherPower = otherDevelopment.getCollector().getCollectionPower();
//						double otherProductivity = otherDevelopment.getCollector().getProductivity();
//						collectionPower += ((double) otherPower) * otherProductivity;
//					}
//				}
//			}
//		}
//		return collectionPower;
//	}
//	
//	public int countItems(Material material, Inventory inventory) {
//		int number = 0;
//		for (ItemStack item : inventory.getContents()) {
//			if (item != null && item.getType() == material) {
//				number += item.getAmount();
//			}
//		}
//		return number;		
//	}

}
