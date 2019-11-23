package net.civex4.nobility.customItem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.customItem.CustomItem;

public class InteractDisable implements Listener {

	@EventHandler()
	public void onInteract(PlayerInteractEvent event) {
		if(!event.hasItem()) {
			return;
		}
		
		ItemStack item = event.getItem();
		
		if (!CustomItem.isCustomItem(item)) {
			return;
		}
		
		if (!CustomItem.get(item).canInteract()) {
			event.setUseItemInHand(Result.DENY);
		}
	}
}
