package net.civex4.nobility.siege;

import net.civex4.nobility.estate.Estate;

public class Siege {
	Estate defender;
	int duration; //time in minutes;
	boolean active;
	
	public Siege(Estate e) {
		this.defender = e;
	}
	
	public int getHealth() {
		return defender.getCurrentHealth();
	}
	
	public int getMaxHealth() {
		return defender.getMaxHealth();
	}

	public Estate getDefender() {
		return defender;
	}

	public void setDefender(Estate defender) {
		this.defender = defender;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int mins) {
		this.duration = mins;
	}
}
