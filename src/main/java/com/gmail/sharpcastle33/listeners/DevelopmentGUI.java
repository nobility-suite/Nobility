package com.gmail.sharpcastle33.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.estate.Estate;

public class DevelopmentGUI implements Listener {
	public void onInventoryClick(InventoryClickEvent event) {
		
		InventoryView inventory = event.getView();
		String title = inventory.getTitle();
		Player player = (Player) event.getWhoClicked();
		player.sendMessage("1");
		if(event.isShiftClick()) {
			event.setCancelled(true);
		}
		player.sendMessage("2");
		ItemStack item = event.getCurrentItem();
		Estate estate = Nobility.estateMan.getEstateOfPlayer(player);

		if (title != estate.getGroup().name) return;
		player.sendMessage("3");
		
		for(Development development: estate.getDevelopments()) {
			player.sendMessage(development.getName());
			if (item.getItemMeta().getDisplayName() == development.getName()) {
				development.activate();
				player.sendMessage("You created a " + development.getName());
			}		
		}		
	}
	
	
}
