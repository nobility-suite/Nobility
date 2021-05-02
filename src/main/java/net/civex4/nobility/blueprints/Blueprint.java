package net.civex4.nobility.blueprints;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobilityitems.NobilityItem;
import net.civex4.nobilityitems.NobilityItems;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;

public class Blueprint {
	private static final String AMOUNT_DELIMITER = "x ";
	private static final String RESULT_AMOUNT_PREFIX = "";
	private static final String RESULT_PREFIX = ChatColor.BLUE + "Result: " + ChatColor.WHITE;
	private static final String INGREDIENT_AMOUNT_PREFIX = ChatColor.WHITE + "  ";
	private static final String RUNS_PREFIX = ChatColor.BLUE + "Uses: " + ChatColor.WHITE;
	private static final String INGREDIENTS_LINE_DELIMITER = ChatColor.BLUE + "Components:" + ChatColor.WHITE;
	
	private static final String TIME_INSTANT = "Instant";
	private static final String TIME_PREFIX = ChatColor.BLUE + "Duration: " + ChatColor.WHITE;
	
	private static final String DATE_PATTERN = "HH:mm:ss";
	
	public NobilityItem result;
	public int resultAmount;
	public HashMap<NobilityItem, Integer> ingredients;
	public int runs;
	public String name;
	public Date time;
	
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
		return NobilityItems.getItemByDisplayName(str);
	}
	
	public Blueprint(NobilityItem result, HashMap<NobilityItem, Integer> ingredients, 
			int runs, String name, int resultAmount) {
		
		this.result = result;
		this.ingredients = ingredients;
		this.runs = runs;
		this.name = name;
		this.resultAmount = resultAmount;
		this.time = null; //null = instant
	}
	
	public Blueprint(NobilityItem result, HashMap<NobilityItem, Integer> ingredients, 
			int runs, String name, int resultAmount, Date timeCost) {
		
		this.result = result;
		this.ingredients = ingredients;
		this.runs = runs;
		this.name = name;
		this.resultAmount = resultAmount;
		this.time = timeCost; //hh-mm-ss
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
				+ result.getDisplayName();
	 
		//TODO not needed for first test
		String parsedTime;
		
		if(this.time == null) { 
			parsedTime = TIME_PREFIX + TIME_INSTANT; 	
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			parsedTime = TIME_PREFIX + sdf.format(this.time);
		}
		//Parse all ingredients
		List<String> parsedIngredients = new ArrayList<>();

		for(NobilityItem item : ingredients.keySet()) {
			String parsedItem = INGREDIENT_AMOUNT_PREFIX 
					+ Integer.toString(ingredients.get(item)) 
					+ AMOUNT_DELIMITER 
					+ item.getDisplayName();
			parsedIngredients.add(parsedItem);
		}

		ItemStack returnItem = new ItemStack(Material.PAPER);
		ItemUtils.setDisplayName(returnItem, parsedName);
		ItemUtils.addLore(returnItem, parsedResult, parsedRuns, parsedTime, INGREDIENTS_LINE_DELIMITER);
		ItemUtils.addLore(returnItem, parsedIngredients);
		
		return returnItem;
	}
	
	/**
	 * Parse an ItemStack into an AbstractBlueprint.
	 * @param item, Blueprint Itemstack
	 * @return AbstractBlueprint with given information.
	 * @author Sharpcastle33
	 */
	
	public static Blueprint parseBlueprintFromItem(ItemStack item) {
		String itemName = ItemUtils.getDisplayName(item);
		
		
		
		List<String> lore = ItemUtils.getLore(item);
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
		for (int i = 4; i < lore.size(); i++) {
			ingredientStrings.add(lore.get(i));
		}
		
		//Parse strings into AbstractBlueprint information
		
		//Result Item
		parsedResult = parsedResult.replaceFirst(RESULT_PREFIX, "");
		String[] numberAndResult = parsedResult.split(AMOUNT_DELIMITER);
		int resultAmount = Integer.parseInt(numberAndResult[0]);
		parsedResult = numberAndResult[1];
		NobilityItem result = getItemStackFromName(parsedResult);
		
		//Runs Amount
		parsedRuns = parsedRuns.replaceFirst(RUNS_PREFIX, "");
		int runsAmount = Integer.parseInt(parsedRuns);
		
		//ParsedTime
		parsedTime = parsedTime.replaceFirst(TIME_PREFIX, "");
		

		HashMap<NobilityItem, Integer> ingredientsMap = new HashMap<>();
		
		for(String str : ingredientStrings) {
			str = str.replaceFirst(INGREDIENT_AMOUNT_PREFIX, "");
			String[] amountAndName = str.split(AMOUNT_DELIMITER);
			int amount = Integer.parseInt(amountAndName[0]);
			String name = amountAndName[1];
			ingredientsMap.put(getItemStackFromName(name), amount);
		}
		Blueprint bp = new Blueprint(result, ingredientsMap, runsAmount, itemName, resultAmount);
		
		if(parsedTime.equals(TIME_INSTANT)) {
			bp.time = null;
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			try {
				bp.time = sdf.parse(parsedTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		return bp;
	}
	
}
