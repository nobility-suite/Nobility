package net.civex4.nobility.customItem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class AnvilDisable implements Listener {

	@EventHandler
	public void onCraft(PrepareAnvilEvent event) {
		InventoryView view = event.getView();
		AnvilInventory aInv = event.getInventory();
		ItemStack[] items = aInv.getContents();
		
		for (ItemStack item : items) {
			if (item == null) continue;
			if (CustomItem.isCustomItem(item)) {				
				event.setResult(item);
			}
		}
		
	}
}
