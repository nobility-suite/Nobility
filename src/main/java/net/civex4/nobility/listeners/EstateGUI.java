package net.civex4.nobility.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;

public class EstateGUI implements Listener {
	/* This class currently does nothing
	 * The plan is to add buttons here for all the estate features
	 * (e.g. developments, trade routes, etc.)
	 */
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) return;		
		
		InventoryView inventory = event.getView();
		String title = inventory.getTitle();
		Player player = (Player) event.getWhoClicked();
		
		ItemStack item = event.getCurrentItem();
		if(!item.hasItemMeta()) return;
		
		//String name = item.getItemMeta().getDisplayName();
		Estate estate = Nobility.estateMan.getEstateOfPlayer(player);
		
		if (title != estate.getGroup().name) return;

		event.setCancelled(true);
		
		
	}
	
}
