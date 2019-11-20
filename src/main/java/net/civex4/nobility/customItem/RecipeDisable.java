package net.civex4.nobility.customItem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class RecipeDisable implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onCraft(PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		ItemStack[] items = inv.getMatrix();
		
		// if any item is a custom item, set result to null
		for (ItemStack item : items) {
			if (item == null) continue;
			if (CustomItem.isCustomItem(item) 
					&& !CustomItem.get(item).canCraft()) {
				
				ItemStack nullItem = CustomItem.getFactory()
						.createItem(Material.DEAD_BRAIN_CORAL, 
								"Broken Thing", 
								"This is a placeholder item")
						.getItem();
				inv.setResult(nullItem);
				
			}
		}

		
	}

}
