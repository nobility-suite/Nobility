package net.civex4.nobility.development;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import net.civex4.nobility.estate.Estate;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;

public class Storehouse implements DevelopmentBehavior{

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

	@Override
	public List<Clickable> getClickables() {
		// OPEN INVENTORY
		
		// STORAGE INFO
		return null;
	}

}
