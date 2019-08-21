package com.gmail.sharpcastle33.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.estate.Estate;

public class DevelopmentGUI implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) return;		

		
		InventoryView inventory = event.getView();
		String title = inventory.getTitle();
		Player player = (Player) event.getWhoClicked();


		ItemStack item = event.getCurrentItem();

		if(!item.hasItemMeta()) return;

		String name = item.getItemMeta().getDisplayName();
		Estate estate = Nobility.estateMan.getEstateOfPlayer(player);

		if (title != estate.getGroup().name) return;

		event.setCancelled(true);
		
		for(Development development: estate.getDevelopments()) {
			String developmentName = development.getName();
			player.sendMessage(developmentName);
			player.sendMessage(item.getItemMeta().getDisplayName());
			if (developmentName.contentEquals(name)) {
				development.activate();
				development.setActive(true);
				player.sendMessage("You created a " + developmentName);
			}		
		}		
	}
	
	
}
