package com.gmail.sharpcastle33.developments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.estate.Estate;

public class Grain extends Development {

	private static String name = "Farm";
	private static ItemStack icon = new ItemStack(Material.HAY_BLOCK);
	private static int price = 3;
	
	public Grain(Estate estate) {
		super(Grain.name, Grain.icon, Grain.price);
		buildFarm(estate);
	}

	private void buildFarm(Estate estate) {
		Location loc = estate.getBlock().getLocation().add(0, 0, 1);
		loc.getBlock().setType(Material.HAY_BLOCK);
	}
	
}
