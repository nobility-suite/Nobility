package net.civex4.nobility.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;

public class DevelopmentOptions implements Listener {
	/* Need to create a menu with the options players can take with a development
	 * The options are upgrade, enable, disable, see other developments in region,
	 * and destroy, plus any additional features. Storehouse needs a feature, "open
	 * storehouse inventory" 
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) return;		

		InventoryView inventory = event.getView();
		String title = inventory.getTitle();
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();

		if(!item.hasItemMeta()) return;
		/*
		String name = item.getItemMeta().getDisplayName();
		Estate estate = Nobility.estateMan.getEstateOfPlayer(player);

		if (title != estate.getGroup().name) return;

		event.setCancelled(true); */
	}

}
