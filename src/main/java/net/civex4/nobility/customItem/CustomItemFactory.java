package net.civex4.nobility.customItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.api.ItemAPI;

/**
 * Controls custom item creation
 *
 */

class CustomItemFactory {

	private static CustomItemManager manager = CustomItem.getManager();
	
	public CustomItemFactory() { }
	
		
	public CustomItem createItem(ItemStack item) {		
		CustomItem customItem = new CustomItem(item);
		if (!manager.isCustomItem(item)) {
			manager.addCustomItem(item, customItem);
		}
		return customItem;
	}
	
	public CustomItem createItem(Material mat, String name, String... lore) {
		ItemStack item = new ItemStack(mat);
		ItemAPI.setDisplayName(item, ChatColor.WHITE + name);
		ItemAPI.addLore(item, lore);
		return createItem(item);
	}

}
