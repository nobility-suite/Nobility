package net.civex4.nobility.customItem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.customItem.CustomItem;

public class RecipeDisable implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onCraft(PrepareItemCraftEvent event) {
		CraftingInventory cInv = event.getInventory();
		ItemStack[] items = cInv.getMatrix();
		
		// if any item is a custom item, set result to null
		for (ItemStack item : items) {
			if (item == null) continue;
			if (CustomItem.isCustomItem(item) 
					&& !CustomItem.get(item).canCraft()) {
				cInv.setResult(CustomItem.getNullItem());

			}
		}

		
	}

}
