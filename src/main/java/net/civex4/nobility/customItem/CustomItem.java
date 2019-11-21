package net.civex4.nobility.customItem;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

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
	private static ItemStack nullItem = CustomItem
			.create(Material.DEAD_BRAIN_CORAL, 
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
	
	public static CustomItem create(Material mat, String name, String... lore) {
		return factory.createItem(mat, name, lore);
	}
	
	public static MerchantRecipe createRecipe(ItemStack result, ItemStack... ingredients) {
		MerchantRecipe recipe = new MerchantRecipe(result, 100000);
		for (ItemStack item : ingredients) {
			recipe.addIngredient(item);
		}
		manager.addRecipe(recipe);
		return recipe;
	}

	public static List<MerchantRecipe> getRecipes() {
		return manager.recipes;
	}
	
	public static void openGUI(Player player) {
		Merchant merchant = Bukkit.createMerchant("Recipes");
		//MerchantInventory inventory = (MerchantInventory) Bukkit.createInventory(null, InventoryType.MERCHANT, "Recipes");
		merchant.setRecipes(getRecipes());
		player.openMerchant(merchant, true);
	}
	
	public static ItemStack getNullItem() {
		return nullItem;
	}
	


}
