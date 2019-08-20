package com.gmail.sharpcastle33.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.gmail.sharpcastle33.Nobility;

public class ChestClick implements Listener {
	
	@EventHandler
	public void onChestClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block b = e.getClickedBlock();
		if(b != null && b.getType() == Material.ENDER_CHEST) {
			if (e.getHand() == EquipmentSlot.HAND) {			
					Nobility.estateMan.openDevelopmentGUI(player);
					e.setCancelled(true);
			}			
		}
	}
	
		
}
