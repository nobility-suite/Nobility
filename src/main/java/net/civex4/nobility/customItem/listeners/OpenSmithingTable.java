package net.civex4.nobility.customItem.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.civex4.nobility.customItem.CustomItem;

public class OpenSmithingTable implements Listener {

	@EventHandler()
	public void onClick(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null) return;
		Block block = event.getClickedBlock();
		if (block.getType() != Material.SMITHING_TABLE) return;
		CustomItem.openGUI(event.getPlayer());
	}
}
