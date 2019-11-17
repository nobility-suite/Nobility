package net.civex4.nobility.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;

public enum ButtonLibrary {
	HOME (
		new Clickable(createIcon(Material.PAPER, "Home")) {
			@Override
			public void clicked(Player p) {
				Nobility.getEstateManager().openEstateGUI(p);
			}			
		}),
	;
	
	private final Clickable clickable;
	
	ButtonLibrary(Clickable clickable) {
		this.clickable = clickable;
	}
	
	public Clickable clickable() { return clickable; }
	public ItemStack icon() { return clickable.getItemStack(); }
	
	
	public static ItemStack createIcon(Material mat, String name) {
		ItemStack icon = new ItemStack(mat);
		ItemAPI.setDisplayName(icon, ChatColor.WHITE + name);
		return icon;
	}
}
