package net.civex4.nobility.customItem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.customItem.CustomItem;

public class AnvilDisable implements Listener {

	@EventHandler
	public void onCraft(PrepareAnvilEvent event) {
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
