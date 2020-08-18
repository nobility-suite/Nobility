package net.civex4.nobility.blueprints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobilityitems.NobilityItem;
import net.civex4.nobilityitems.NobilityItems;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
	
public class Blueprint {
	private static final String AMOUNT_DELIMITER = "x ";
	private static final String RESULT_AMOUNT_PREFIX = "";
	private static final String RESULT_PREFIX = ChatColor.YELLOW + "Result: ";
	private static final String INGREDIENT_AMOUNT_PREFIX = ChatColor.GOLD + "  ";
	private static final String RUNS_PREFIX = "Uses: ";
	private static final String INGREDIENTS_LINE_DELIMITER = "Components:";
	
	private static final String TIME_INSTANT = "Time: Instant";
	
	private NobilityItem result;
	private int resultAmount;
	private HashMap<NobilityItem, Integer> ingredients;
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
	
	public static NobilityItem getItemStackFromName(String str) {
		return NobilityItems.getItemByName(str);
	}
	
	public Blueprint(NobilityItem result, HashMap<NobilityItem, Integer> ingredients, 
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
				+ result.getInternalName();
	 
		//TODO not needed for first test
		String parsedTime = TIME_INSTANT;
		
		//Parse all ingredients
		List<String> parsedIngredients = new ArrayList<>();

		for(NobilityItem item : ingredients.keySet()) {
			String parsedItem = INGREDIENT_AMOUNT_PREFIX 
					+ Integer.toString(ingredients.get(item)) 
					+ AMOUNT_DELIMITER 
					+ item.getInternalName();
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
	
	public static Blueprint parseBlueprintFromItem(ItemStack item) {
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
		NobilityItem result = getItemStackFromName(parsedResult);
		
		//Runs Amount
		parsedRuns.replaceFirst(RUNS_PREFIX, "");
		int runsAmount = Integer.parseInt(parsedRuns);
		
		//ParsedTime is currently unused
		
		HashMap<NobilityItem, Integer> ingredientsMap = new HashMap<>();
		
		for(String str : ingredientStrings) {
			str.replaceFirst(INGREDIENT_AMOUNT_PREFIX, "");
			String[] amountAndName = str.split(AMOUNT_DELIMITER);
			int amount = Integer.parseInt(amountAndName[0]);
			String name = amountAndName[1];
			ingredientsMap.put(getItemStackFromName(name), amount);
		}
		
		return new Blueprint(result, ingredientsMap, runsAmount, itemName, resultAmount);
	}
	
}
