package net.civex4.nobility.cannons;

import org.bukkit.block.Block;

import net.civex4.nobility.estate.Estate;

public class Cannon {
	
	public Block block;
	public int health;
	public int maxHealth;
	public Estate owner;
	
	public Cannon (Block b, Estate e) {
		this.block = b;
		this.owner = e;
		this.health = 100;
		this.maxHealth = 100;
	}
	
}
