package net.civex4.nobility.customItem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener to cancel block placement if the item is a custom item and the item is set to cancel.
 *
 */

public class BlockPlaceDisable implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		ItemStack item = event.getItemInHand();
		
		if (!CustomItem.isCustomItem(item)) {
			return;
		}
		
		if (!CustomItem.get(item).canPlace()) {
			event.setCancelled(true);
		}

	}

}
