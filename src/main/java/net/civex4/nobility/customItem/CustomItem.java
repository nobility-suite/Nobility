package net.civex4.nobility.customItem;

import org.bukkit.inventory.ItemStack;

/**
 * Creates Custom Items
 * Contains dynamic fields, and encapsulates factory and the manager methods
 */

public class CustomItem {
	
	private ItemStack item;
	private Boolean canPlace = false; // allow to be placed
	private Boolean canCraft = false; // allow use in vanilla recipes
	
	// TODO move to main class
	private static CustomItemManager manager = new CustomItemManager();
	private static CustomItemFactory factory = new CustomItemFactory();
	
	// Use CustomItem.getFactory().createItem(item) to create a custom item
	protected CustomItem(ItemStack item) {
		this.item = item;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public boolean canPlace() {
		return canPlace;
	}
	
	public void canPlace(boolean canPlace) {
		this.canPlace = canPlace;
	}
	
	public boolean canCraft() {
		return canCraft;
	}
	
	public void canCraft(boolean canCraft) {
		this.canCraft = canCraft;
	}
	
	public static CustomItem get(ItemStack item) {
		return manager.getCustomItem(item);		
	}
	
	public static boolean isCustomItem(ItemStack item) {
		return manager.isCustomItem(item);
	}
	
	public static CustomItemManager getManager() {
		return manager;
	}
	
	public static CustomItemFactory getFactory() {
		return factory;
	}

}
