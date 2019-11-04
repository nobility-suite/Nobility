package com.gmail.sharpcastle33.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.development.DevelopmentType;
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

		for(String development: estate.getUnbuiltDevelopments()) {
			if(development.contentEquals(name)) {
				DevelopmentType type = DevelopmentType.getDevelopmentType(development);
				//Check costs
				if (!Nobility.getDevelopmentManager().checkCosts(type, estate)) {
					player.sendMessage("You don't have enough to construct this development");
					return;
				}
				estate.buildDevelopment(type);
				player.sendMessage("You constructed a " + type.getName());
				player.closeInventory();
				Nobility.estateMan.openDevelopmentGUI(player);
			}
		}
		
		for(Development development: estate.getBuiltDevelopments()) {
			String developmentName = development.getDevelopmentType().getName();
			if (developmentName.contentEquals(name)) {
				if (development.isActive() == false) {
					//TODO: if development has enough food...
					development.activate();
					development.setActive(true);
					player.sendMessage(developmentName + " is now active");
					player.closeInventory();
					Nobility.estateMan.openDevelopmentGUI(player);
				}  else {
					development.deactivate();
					development.setActive(false);
					player.sendMessage(developmentName + " is now inactive");
					player.closeInventory();
					Nobility.estateMan.openDevelopmentGUI(player);
				}
			}
		}		
	}
	
	
}
