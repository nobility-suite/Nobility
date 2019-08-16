package com.gmail.sharpcastle33.developments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.estate.Estate;

public class Mine extends Development {
	
	private static String name = "Mine";
	private static ItemStack icon = new ItemStack(Material.IRON_PICKAXE);
	private static int price = 5;
	
	public Mine(Estate estate) {
		super(Mine.name, Mine.icon, Mine.price);	
	}
	
	static void buildMine(Estate estate) {
		Location loc = estate.getBlock().getLocation().add(1, 0, 0);
		loc.getBlock().setType(Material.STONECUTTER);
	}

}
