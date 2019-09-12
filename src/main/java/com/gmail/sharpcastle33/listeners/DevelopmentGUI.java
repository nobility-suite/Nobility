package com.gmail.sharpcastle33.listeners;

import com.gmail.sharpcastle33.development.DevelopmentRegister;
import com.gmail.sharpcastle33.estate.EstateManager;
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

		for(DevelopmentRegister register: estate.getUninitializedRegisteredDevelopments()) {
			String developmentName = register.getName();
			if(developmentName.contentEquals(name)) {
				//Check costs
				if (!Nobility.getDevelopmentManager().checkCosts(register, estate)) {
					player.sendMessage("You don't have enough to construct this estate");
					return;
				}
				estate.initializeRegister(register);
				player.sendMessage("You constructed a " + developmentName);
				player.closeInventory();
				Nobility.estateMan.openDevelopmentGUI(player);
			}
		}
		
		for(Development development: estate.getDevelopments()) {
			String developmentName = development.getName();
			if (developmentName.contentEquals(name)) {
				development.activate();
				development.setActive(true);
				player.sendMessage(developmentName + "is now active");
				player.closeInventory();
				Nobility.estateMan.openDevelopmentGUI(player);
			}
		}		
	}
	
	
}
