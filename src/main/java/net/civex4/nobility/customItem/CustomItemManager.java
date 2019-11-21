package net.civex4.nobility.customItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

/**
 * Manages a map of all the item stacks designated as custom items
 */
class CustomItemManager {

	private Map<ItemStack, CustomItem> customItems = new HashMap<>();
	private static final Map<Integer, String> itemNames = new HashMap<>();
	
	public CustomItemManager() { }
	
	public void addCustomItem(ItemStack stack, CustomItem customItem) {
		ItemStack clone = stack.clone();
		clone.setAmount(1);
		customItems.put(clone, customItem);
	}
	
	public CustomItem getCustomItem(ItemStack stack) {
		ItemStack clone = stack.clone();
		clone.setAmount(1);
		
		return customItems.get(clone);
	}
	
	public Set<ItemStack> getItemStackSet() {
		return customItems.keySet();
	}
	
	public boolean isCustomItem(ItemStack stack) {
		ItemStack clone = stack.clone();
		
		clone.setAmount(1);
		return customItems.containsKey(clone);
	}
	
	

}
