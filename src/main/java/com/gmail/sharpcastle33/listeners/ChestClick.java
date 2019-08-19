package com.gmail.sharpcastle33.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.estate.Estate;

public class ChestClick implements Listener {
	
	@EventHandler
	public void onChestClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block b = e.getClickedBlock();
		if(b != null && b.getType() == Material.ENDER_CHEST) {
			if (e.getHand() == EquipmentSlot.HAND) {			
				Estate estate = Nobility.estateMan.getEstateOfPlayer(player);
				if(estate.getBlock() == b) {
					Nobility.estateMan.openDevelopmentGUI(estate, player);
					e.setCancelled(true);
				}

			}			
		}
	}
	
		
}
