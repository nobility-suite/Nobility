package net.civex4.nobility.estate;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.gui.ButtonLibrary;

public enum Relationship {
	
	NATION (Material.BLACK_BANNER, "In Your Nation"), 
	ALLY (Material.BLUE_BANNER, "Ally"), 
	FRIEND (Material.PLAYER_HEAD, "Friend"), 
	NEUTRAL (Material.ZOMBIE_HEAD, "Neutral"),
	DISTRUST (Material.CREEPER_HEAD, "Distrust"), 
	ENEMY (Material.SKELETON_SKULL, "Enemy");
	
	private final ItemStack icon;
	private final String title;
	
	Relationship(Material mat, String title) {
		this.icon = ButtonLibrary.createIcon(mat, title);
		this.title = title;
	}
	
	public ItemStack icon() {
		return icon;
	}
	
	public String title() {
		return title;
	}
	
}
