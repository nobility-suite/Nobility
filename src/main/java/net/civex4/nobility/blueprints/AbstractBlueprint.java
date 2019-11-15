package net.civex4.nobility.blueprints;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
	
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
	
	public static ItemStack getItemStackFromName(String s) {
		//TODO, this should be a lookup from the yaml doc.
		ItemStack i = new ItemStack(Material.PAPER);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(s);
		i.setItemMeta(m);
		return i;
	}
	
	public AbstractBlueprint(ItemStack result, HashMap<ItemStack, Integer> ingredients, int runs, 
			String name, int resultAmount) {
		
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
		String parsed_name = this.name;
		String parsed_runs = RUNS_PREFIX + Integer.toString(this.runs);
		String parsed_result = RESULT_PREFIX + RESULT_AMOUNT_PREFIX + Integer.toString(this.resultAmount)+ AMOUNT_DELIMITER + this.result.getItemMeta().getDisplayName();
	 
		//TODO not needed for first test
		String parsed_time = TIME_INSTANT;
		
		
		//Parse all ingredients
		ArrayList<String> parsed_ingredients = new ArrayList<String>();
		String ingredient_delimiter = INGREDIENTS_LINE_DELIMITER;
		
		for(ItemStack i : ingredients.keySet()) {
			String parsed_i = INGREDIENT_AMOUNT_PREFIX + Integer.toString(ingredients.get(i)) + AMOUNT_DELIMITER + i.getItemMeta().getDisplayName();
			parsed_ingredients.add(parsed_i);
		}
		
		ItemStack ret = new ItemStack(Material.PAPER, 1);
		ItemMeta meta = ret.getItemMeta();
		meta.setDisplayName(parsed_name);
		
		ArrayList<String> bplore = new ArrayList<String>();
		
		//Add all parsed information to item meta.
		
		bplore.add(parsed_result);
		bplore.add(parsed_runs);
		bplore.add(parsed_time);
		bplore.add(ingredient_delimiter);
		
		for(String s : parsed_ingredients) {
			bplore.add(s);
		}
		
		meta.setLore(bplore);
		ret.setItemMeta(meta);
				
		return ret;
		
	}
	
	/**
	 * Parse an ItemStack into an AbstractBlueprint.
	 * @param i, Blueprint Itemstack
	 * @return AbstractBlueprint with given information.
	 * @author Sharpcastle33
	 */
	
	public static AbstractBlueprint parseBlueprintFromItem(ItemStack item) {
		java.util.List<String> lore = item.getItemMeta().getLore();
		String itemName = item.getItemMeta().getDisplayName();
		
		//Parse list into equivalent strings
		String parsed_result = lore.get(0);
		String parsed_runs = lore.get(1);
		String parsed_time = lore.get(2);
		//Ingredient Delimiter = Line 3
		
		ArrayList<String> ingredient_strings = new ArrayList<String>();
		
		for(int i = 4; i < lore.size(); i++) {
			ingredient_strings.add(lore.get(i));
		}
		
		//Parse strings into AbstractBlueprint information
		
		//Result Item
		parsed_result.replaceFirst(RESULT_PREFIX, "");
		String[] result_arr = parsed_result.split(RESULT_AMOUNT_PREFIX);
		int result_amount = Integer.parseInt(result_arr[0]);
		parsed_result = result_arr[1];
		ItemStack result = getItemStackFromName(parsed_result);
		
		//Runs Amount
		parsed_runs.replaceFirst(RUNS_PREFIX, "");
		int runs_amount = Integer.parseInt(parsed_runs);
		
		//ParsedTime is currently unused
		
		//Ingredients Map
		HashMap<ItemStack, Integer> ing = new HashMap<ItemStack, Integer>();
		
		for(String s : ingredient_strings) {
			s.replaceFirst(INGREDIENT_AMOUNT_PREFIX, "");
			String[] s_arr = s.split(AMOUNT_DELIMITER);
			int i_amt = Integer.parseInt(s_arr[0]);
			String i_name = s_arr[1];
			ing.put(getItemStackFromName(i_name), i_amt);
		}
		
		return new AbstractBlueprint(result, ing, runs_amount, itemName, result_amount);
	}
	
}
