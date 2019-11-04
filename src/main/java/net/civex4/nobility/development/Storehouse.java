package net.civex4.nobility.development;

import org.bukkit.Location;
import org.bukkit.Material;

import net.civex4.nobility.estate.Estate;

public class Storehouse implements Developer{

	Estate estate;
	Development development;
	
	public Storehouse(Estate estate, Development development) {
		this.estate = estate;
		this.development = development;
	}

	@Override
	public void build() {
		Location loc = estate.getBlock().getLocation().add(1, 0, 0);
		loc.getBlock().setType(Material.CHEST);
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
