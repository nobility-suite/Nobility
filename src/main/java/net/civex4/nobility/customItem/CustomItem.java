package net.civex4.nobility.customItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Creates Custom Items
 * Contains dynamic fields, and encapsulates factory and the manager methods
 */

public class CustomItem {
	
	private ItemStack item;
	private boolean canPlace = false;    // allow to be placed
	private boolean canCraft = false;    // allow use in vanilla recipes
	private boolean canInteract = false; // allow player interaction events

	
	// TODO move to main class
	private static CustomItemManager manager = new CustomItemManager();
	private static CustomItemFactory factory = new CustomItemFactory();
	private static ItemStack nullItem = CustomItem.getFactory()
			.createItem(Material.DEAD_BRAIN_CORAL, 
					"Broken Thing", 
					"This is a placeholder item")
			.getItem();
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
	
	public boolean canInteract() {
		return canInteract;
	}
	
	public void canInteract(boolean canInteract) {
		this.canInteract = canInteract;
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

	public static ItemStack getNullItem() {
		return nullItem;
	}
}
