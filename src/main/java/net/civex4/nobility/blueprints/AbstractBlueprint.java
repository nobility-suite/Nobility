package net.civex4.nobility.blueprints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.MidiDevice.Info;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Warning;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
	
public class AbstractBlueprint {
	private static final String AMOUNT_DELIMITER = "x ";
	private static final String RESULT_AMOUNT_PREFIX = "";
	private static final String RESULT_PREFIX = ChatColor.YELLOW + "Result: ";
	private static final String INGREDIENT_AMOUNT_PREFIX = ChatColor.GOLD + "  ";
	private static final String RUNS_PREFIX = "Uses: ";
	private static final String INGREDIENTS_LINE_DELIMITER = "Components:";
	
	private static final String TIME_INSTANT = "Time: Instant";
	
	private ItemStack result;
	private int resultAmount;
	private HashMap<ItemStack, Integer> ingredients;
	private int runs;
	private String name;
	
	/* Example Blueprint
	 * 
	 * Result: 4x Vigorine Elixir
	 * Uses: 15
	 * Time: Instant
	 * Components:
	 *   15x Machine Plate
	 *   12x Bronze Gear
	 *   24x Life Powder
	 *   36x Liferod
	 */
	
	public static ItemStack getItemStackFromName(String str) {
		//TODO, this should be a lookup from the yaml doc.
		ItemStack item = new ItemStack(Material.PAPER);
		ItemAPI.setDisplayName(item, str);
		return item;
	}
	// TODO Custom Items need
	
	public AbstractBlueprint(ItemStack result, HashMap<ItemStack, Integer> ingredients, 
			int runs, String name, int resultAmount) {
		
		this.result = result;
		this.ingredients = ingredients;
		this.runs = runs;
		this.name = name;
		this.resultAmount = resultAmount;
	}

	/**
	 * Parse an AbstractBlueprint into item form.
	 * @return an ItemStack containing parsed Blueprint information
	 * @author Sharpcastle33
	 */
	
	public ItemStack parseBlueprintToItem() {
		
		//Parse name, runs, and result.
		String parsedName = this.name;
		String parsedRuns = RUNS_PREFIX + Integer.toString(this.runs);
		String parsedResult = RESULT_PREFIX 
				+ RESULT_AMOUNT_PREFIX 
				+ Integer.toString(this.resultAmount)
				+ AMOUNT_DELIMITER 
				+ ItemAPI.getDisplayName(this.result);
	 
		//TODO not needed for first test
		String parsedTime = TIME_INSTANT;
		
		//Parse all ingredients
		List<String> parsedIngredients = new ArrayList<>();

		for(ItemStack item : ingredients.keySet()) {
			String parsedItem = INGREDIENT_AMOUNT_PREFIX 
					+ Integer.toString(ingredients.get(item)) 
					+ AMOUNT_DELIMITER 
					+ ItemAPI.getDisplayName(item);
			parsedIngredients.add(parsedItem);
		}

		ItemStack returnItem = new ItemStack(Material.PAPER);
		ItemAPI.setDisplayName(returnItem, parsedName);
		ItemAPI.addLore(returnItem, parsedResult, parsedRuns, parsedTime, INGREDIENTS_LINE_DELIMITER);
		ItemAPI.addLore(returnItem, parsedIngredients);
		
		return returnItem;
	}
	
	/**
	 * Parse an ItemStack into an AbstractBlueprint.
	 * @param item, Blueprint Itemstack
	 * @return AbstractBlueprint with given information.
	 * @author Sharpcastle33
	 */
	
	public static AbstractBlueprint parseBlueprintFromItem(ItemStack item) {
		String itemName = ItemAPI.getDisplayName(item);
		
		List<String> lore = ItemAPI.getLore(item);
		if (lore.size() < 4) {
			Bukkit.getLogger().warning("Only Blueprint Items can be parsed");
			return null;
		}

		//Parse list into equivalent strings
		String parsedResult = lore.get(0);
		String parsedRuns = lore.get(1);
		String parsedTime = lore.get(2);
		//Ingredient Delimiter = Line 3
		
		List<String> ingredientStrings = new ArrayList<>();
		ingredientStrings.addAll(4, lore);
		
		//Parse strings into AbstractBlueprint information
		
		//Result Item
		parsedResult.replaceFirst(RESULT_PREFIX, "");
		String[] numberAndResult = parsedResult.split(RESULT_AMOUNT_PREFIX);
		int resultAmount = Integer.parseInt(numberAndResult[0]);
		parsedResult = numberAndResult[1];
		ItemStack result = getItemStackFromName(parsedResult);
		
		//Runs Amount
		parsedRuns.replaceFirst(RUNS_PREFIX, "");
		int runsAmount = Integer.parseInt(parsedRuns);
		
		//ParsedTime is currently unused
		
		HashMap<ItemStack, Integer> ingredientsMap = new HashMap<ItemStack, Integer>();
		
		for(String str : ingredientStrings) {
			str.replaceFirst(INGREDIENT_AMOUNT_PREFIX, "");
			String[] amountAndName = str.split(AMOUNT_DELIMITER);
			int amount = Integer.parseInt(amountAndName[0]);
			String name = amountAndName[1];
			ingredientsMap.put(getItemStackFromName(name), amount);
		}
		
		return new AbstractBlueprint(result, ingredientsMap, runsAmount, itemName, resultAmount);
	}
	
}
