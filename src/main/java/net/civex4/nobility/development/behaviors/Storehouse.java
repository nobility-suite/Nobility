package net.civex4.nobility.development.behaviors;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.estate.Estate;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;

public class Storehouse implements DevelopmentBehavior{

	Estate estate;
	Development development;
	Inventory inventory;
	
	public Storehouse(Estate estate, Development development) {
		this.estate = estate;
		this.development = development;
		this.inventory = Bukkit.createInventory(null, 27);
	}

	@Override
	public void build() {
		inventory.addItem(
				new ItemStack(Material.IRON_INGOT, 10),
				new ItemStack(Material.OAK_LOG, 10),
				new ItemStack(Material.SMOOTH_STONE, 10),
				new ItemStack(Material.WHEAT, 10));
	}

	@Override
	public void tick() {
		
	}

	@Override
	public List<Clickable> getClickables() {
		List<Clickable> clickables = new ArrayList<>();
		
		// OPEN INVENTORY
		Inventory inv = this.inventory;
		Clickable openInventory = new Clickable(new ItemStack(Material.CHEST)) {
			@Override
			public void clicked(Player player) {
				player.openInventory(inv);
			}			
		};		
		clickables.add(openInventory);
		
		// STORAGE INFO
		Clickable info = new DecorationStack(new ItemStack(Material.PAPER));
		clickables.add(info);
		
		return clickables;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}

}
