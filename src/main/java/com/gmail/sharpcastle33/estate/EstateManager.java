package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.group.Group;

public class EstateManager {
	
	public ArrayList<Estate> estates = new ArrayList<Estate>();
	
	//hashmap player-estate
	public HashMap<Player, Estate> estateOfPlayer = new HashMap<Player, Estate>();
	
	/*public EstateManager() {
		ArrayList<Estate> estates = new ArrayList<Estate>();
	}*/
	
	public Estate createEstate(Block block, Player player) {
		Group group = Nobility.groupMan.getGroup(player);		
		block.setType(Material.ENDER_CHEST);		
		Estate estate = new Estate(block, group);		
		estates.add(estate);
		

		player.sendMessage("You have created an estate for " + group.name);
		setEstateOfPlayer(player, estate);
		
		return estate;
		
	}
	
	public void openDevelopmentGUI(Estate estate, Player player) {
		Inventory developmentIcons = Bukkit.createInventory(null, 9, estate.getGroup().name);
		
		int i = 0;
		for(Development development: estate.getDevelopments()) {
			if (estate.getDevelopments().containsAll(development.getPrerequisites())) {
				Material m = development.getIcon();
				ItemStack icon = new ItemStack(m);
				nameItem(icon, development.getName());
				developmentIcons.setItem(i, icon);
				i++;
			}
		}
		
		player.openInventory(developmentIcons);
	}
	
	//Utility method to rename an item
	public static ItemStack nameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);		
		item.setItemMeta(meta);
		return item;
	}
	
	
	//getters and setters for player-estate hashmap
	public void setEstateOfPlayer(Player p, Estate e) {
		estateOfPlayer.put(p, e);
	}
	
	public Estate getEstateOfPlayer(Player p) {
		return estateOfPlayer.get(p);
	}
	
	public boolean playerHasEstate(Player p) {
		return estateOfPlayer.containsKey(p);
	}

	
}
