package com.gmail.sharpcastle33.estate;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gmail.sharpcastle33.Nobility;

public class EstateCreate implements Listener {
	
	@EventHandler
	public void chestClick (PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		Block b = e.getClickedBlock();
			if (b != null && b.getType() == Material.CHEST) {			
				Nobility.estateMan.createEstate(b, p);
			}
	}
	
}
