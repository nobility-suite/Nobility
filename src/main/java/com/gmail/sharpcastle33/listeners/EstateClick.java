package com.gmail.sharpcastle33.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.gmail.sharpcastle33.Nobility;

public class EstateClick implements Listener {
	
	@EventHandler
	public void onChestClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block b = e.getClickedBlock();
		
		//if block is not in list of estates, return
		
		if(b != null && b.getType() == Material.ENDER_CHEST) {		
			if (e.getHand() == EquipmentSlot.HAND) {
				Nobility.getDevelopmentManager().openDevelopmentMenu(player, b);
				e.setCancelled(true);
			}			
		}
	}
	


	/*private void openDevelopmentInventory(Player player) {
		Estate estate = Nobility.estateMan.getPlayersEstate(player);
		Inventory inv = Bukkit.createInventory(null, 9, estate.getGroup().name);
		inv.setItem(0, Grain.icon);
		inv.setItem(1, Meat.icon);
		inv.setItem(2, Mine.icon);
		inv.setItem(3, Storehouse.icon);
		player.openInventory(inv);
	} */
		
}
