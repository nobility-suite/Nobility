package net.civex4.nobility.customItem.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.civex4.nobility.customItem.CustomItem;

public class OpenSmithingTable implements Listener {

	@EventHandler()
	public void onClick(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block block = event.getClickedBlock();
		if (block.getType() != Material.SMITHING_TABLE) return;
		event.setCancelled(true);
		CustomItem.openGUI(event.getPlayer());
	}
}
