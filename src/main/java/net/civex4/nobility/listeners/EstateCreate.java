package net.civex4.nobility.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.civex4.nobility.Nobility;
import org.bukkit.inventory.Inventory;

public class EstateCreate implements Listener {

	@EventHandler
	public void chestClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (Nobility.getGroupManager().getGroup(p) == null) return;
		Block b = e.getClickedBlock();
		if (!(Nobility.getEstateManager().playerHasEstate(p))) {
			if (b != null && b.getType() == Material.CHEST) {
				Chest c = (Chest) b.getState();
				Inventory inv = c.getBlockInventory();
					if (inv.contains(Material.IRON_INGOT, 10)) {
						//So above method checks to see if there is equal to, or greater than 10,
						//we need to add below check to make sure the the EXACT amount in the chest is 10.
						if (inv.contains(Material.IRON_INGOT, 11)) {
							return;
						}
						Nobility.getEstateManager().createEstate(b, p);
				}
			}
		}
	}
}
