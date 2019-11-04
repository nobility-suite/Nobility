package net.civex4.nobility.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.civex4.nobility.Nobility;

public class EstateCreate implements Listener {
	
	@EventHandler
	public void chestClick (PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (Nobility.getGroupManager().getGroup(p) == null) return;
		Block b = e.getClickedBlock();
			if (!(Nobility.estateMan.playerHasEstate(p))) {
				if (b != null && b.getType() == Material.CHEST) {				
					Nobility.estateMan.createEstate(b, p);
				}
			}
	}
	
}
