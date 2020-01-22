package net.civex4.nobility.blueprints;

import java.util.Map;
import org.bukkit.inventory.ItemStack;

public abstract class ItemGroup {
  
  public abstract Map<ItemStack, Integer> generate();

}
