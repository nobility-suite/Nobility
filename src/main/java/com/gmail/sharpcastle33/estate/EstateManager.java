package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gmail.sharpcastle33.development.DevelopmentRegister;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	private HashMap<Player, Estate> estateOfPlayer = new HashMap<Player, Estate>();
	
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
	
	public void openDevelopmentGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		Inventory developmentIcons = Bukkit.createInventory(null, 9, estate.getGroup().name);
		
		int i = 0;
		for(Development development: estate.getActiveDevelopments()) {
			Material m = development.getRegister().getIcon();
			ItemStack icon = new ItemStack(m);
			nameItem(icon, development.getName());
			addLore(icon, ChatColor.GREEN + "Active");
			developmentIcons.setItem(i, icon);
			i++;
		}
		for(Development development: estate.getInactiveDevelopments()) {
			Material m = Material.FIREWORK_STAR;
			ItemStack icon = new ItemStack(m);
			nameItem(icon, development.getName());
			addLore(icon, ChatColor.RED + "Inactive");
			developmentIcons.setItem(i, icon);
			i++;
		}
		
		for(DevelopmentRegister register: estate.getUninitializedRegisteredDevelopments()) {
			if (estate.getActiveDevelopmentsToString().containsAll(register.getPrerequisites())) {
				Material m = register.getIcon();
				ItemStack icon = new ItemStack(m);
				nameItem(icon, register.getName());
				addLore(icon, ChatColor.YELLOW + "Not Yet Constructed");
				addLore(icon, "");
				addLore(icon, ChatColor.YELLOW + "Prerequisites:");
				for(String prerequisite: register.getPrerequisites()) addLore(icon, prerequisite);
				addLore(icon, "");
				addLore(icon, ChatColor.YELLOW + "Cost:");
				for(String material: register.getCost().keySet()) addLore(icon, material + ": " + register.getCost().get(material));
				//
				addLore(icon, "");
				addLore(icon, ChatColor.YELLOW + "Initial Cost:");
				for(ItemStack item : register.getInitialCost()) {
					addLore(icon, item.getType().toString() +  ": " + item.getAmount());
				}
				if(Nobility.getDevelopmentManager().checkCosts(register, estate)) {
					addLore(icon, "");
					addLore(icon, ChatColor.RED + "Not enough to construct this estate");
				}				
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

	/**
	 * Utility method to add a single line of lore text to an item
	 * @param item ItemStack item to add lore text to
	 * @param text String text to add
	 */
	public static void addLore(ItemStack item, String text) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if(lore == null) lore = new ArrayList<>();
		lore.add(text);
		meta.setLore(lore);
		item.setItemMeta(meta);
	} // addLore
	
	
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
