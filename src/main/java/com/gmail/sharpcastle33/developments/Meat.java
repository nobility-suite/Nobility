package com.gmail.sharpcastle33.developments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.estate.Estate;

public class Meat extends Development {

	private static String name = "Pasture";
	private static ItemStack icon = new ItemStack(Material.BEEF);
	private static int price = 4;
	
	public Meat(Estate estate) {
		super(Meat.name, Meat.icon, Meat.price);
		buildPasture(estate);
	}

	private void buildPasture(Estate estate) {
		Location loc = estate.getBlock().getLocation().add(-1, 0, 0);
		loc.getBlock().setType(Material.SMOKER);
	}
	
}
