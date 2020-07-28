package net.civex4.nobility.cannons;

import java.util.ArrayList;

import org.bukkit.Location;

public class CannonManager {
	
	public ArrayList<Cannon> activeCannons;

	public CannonManager() {
		this.activeCannons = new ArrayList<Cannon>();
	}
	
	public void addCannon(Cannon c) {
		this.activeCannons.add(c);
	}
	
	public void removeCannon(Cannon c) {
		this.activeCannons.remove(c);
	}
	
	public void dismantleCannon(Location loc) {
		//TODO
	}
	
	public boolean isCannon(Location loc) {
		return false;
		//TODO
	}
	
	public Cannon getCannon(Location loc) {
		return null;
		//TODO
	}
	
	public void damageCannon(Cannon c, int amt) {
		//TODO
	}
}
