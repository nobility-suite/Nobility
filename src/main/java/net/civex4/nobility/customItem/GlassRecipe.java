package net.civex4.nobility.customItem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.api.ItemAPI;

/**
 * Custom recipe
 * 9 broken glass = 1 normal glass
 * Only can accept one broken glass
 * TODO Add custom recipe creation tool 
 */

public class GlassRecipe implements Listener {
	
	@EventHandler
	public void onGlassCraft(PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		ItemStack[] items = inv.getMatrix();
		for (ItemStack item : items) {
			if (item == null) return;
			if (!CustomItem.isCustomItem(item) 
					|| item.getAmount() != 1 
					|| !ItemAPI.getDisplayName(item).contains("Broken Glass")) {
				return;
			}
		}
		inv.setResult(new ItemStack(Material.GLASS));
		
	}

}
