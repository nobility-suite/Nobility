package net.civex4.nobility.blueprints;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.inventory.ItemStack;

public class AbstractBlueprint {
  private final String AMOUNT_DELIMITER = "x ";
  private final String RESULT_AMOUNT_PREFIX = "";
  private final String INGREDIENT_AMOUNT_PREFIX = "";
  
  
  private ItemStack result;
  private int resultAmount;
  private HashMap<ItemStack, Integer> ingredients;
  private int runs;
  private String name;
  
  public AbstractBlueprint(ItemStack result, HashMap<ItemStack, Integer> ingredients, int runs,
      String name, int resultAmount) {
    
    this.result = result;
    this.ingredients = ingredients;
    this.runs = runs;
    this.name = name;
    this.resultAmount = resultAmount;
  }
  
  public ItemStack parseBlueprintToItem() {
    String parsed_name = this.name;
    String parsed_runs = Integer.toString(this.runs);
    String parsed_result = RESULT_AMOUNT_PREFIX + Integer.toString(this.resultAmount)+ AMOUNT_DELIMITER + this.result.getItemMeta().getDisplayName();
    
    ArrayList<String> parsed_ingredients = new ArrayList<String>();
    
    for(ItemStack i : ingredients.keySet()) {
      String parsed_i = INGREDIENT_AMOUNT_PREFIX + Integer.toString(ingredients.get(i)) + AMOUNT_DELIMITER + i.getItemMeta().getDisplayName();
      parsed_ingredients.add(parsed_i);
    }
    
    //TODO Incomplete
    
    return null;
    
  }
  
  public static AbstractBlueprint parseBlueprintFromItem(ItemStack i) {
    java.util.List<String> lore = i.getItemMeta().getLore();
    String itemName = i.getItemMeta().getDisplayName();
    
    //TODO incomplete

    return null;
  }
  
}
