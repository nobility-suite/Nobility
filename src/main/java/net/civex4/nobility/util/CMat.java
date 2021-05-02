package net.civex4.nobility.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;


public class CMat {
	
	/*
	 * WIP,
	 * Does not use lore!
	 * Possibly use MythicItems instead?
	 */
	
	private Material mat;
	private String itemName; //includes chatcolor

	public CMat(Material m) {
		this.mat = m;
		this.itemName = "";
	}
	
	public CMat(Material m, String name) {
		this.mat = m;
		this.itemName = name;
	}
	
	public ItemStack getItem() {
		return getItem(1);
	}
	
	public ItemStack getItem(int amt) {
		ItemStack stack = new ItemStack(this.mat,1);
		if(itemName != "") {
			ItemUtils.setDisplayName(stack, itemName);
		}
		return stack;
	}
}
