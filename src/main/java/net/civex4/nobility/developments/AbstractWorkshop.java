package net.civex4.nobility.developments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class AbstractWorkshop extends Development {
	public Location inputChest;
	public Location outputChest;
	public ItemStack selectedRecipe;

	
	public AbstractWorkshop(DevelopmentType type) {
		super(type);

	}
	
	public boolean outputContains(ItemStack i, int amt) {

		Block b = this.inputChest.getBlock();
		if(b!= null && b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
			Chest chest = (Chest) b.getState();
			Inventory inv = chest.getInventory();
			if(inv.contains(i,amt)) {
				return true;
			}
		}
		return false;
	}

}
