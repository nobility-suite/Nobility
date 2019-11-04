package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
import com.gmail.sharpcastle33.development.DevelopmentType;
import com.gmail.sharpcastle33.group.Group;

public class EstateManager {
	
	public ArrayList<Estate> estates = new ArrayList<Estate>();
	
	//HashMap player-estate
	private HashMap<Player, Estate> estateOfPlayer = new HashMap<Player, Estate>();
		
	public boolean isVulnerable(Estate e) {
		int h = e.getVulnerabilityHour(); //should be between 0 and 23;
		Calendar rightNow = Calendar.getInstance();
		int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
		if(currentHour >= h && currentHour < (h+2) % 24) {
			return true;
		} else {
			return false;
		}
	}
	
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
			DevelopmentType type = development.getDevelopmentType();
			Material m = type.getIcon();
			ItemStack icon = new ItemStack(m);
			nameItem(icon, type.getTitle());
			addLore(icon, ChatColor.GREEN + "Active");
			if (!type.getUpkeepCost().isEmpty()) {			
				addLore(icon, ChatColor.YELLOW + "Upkeep Cost:");
				for (ItemStack cost : type.getUpkeepCost()) {
					addLore(icon, cost.getType().toString() + ": " + cost.getAmount());
				}
				
				/*
				 * For when the costs have been abstracted
				if (type.getUpkeepCost().containsKey("Food")) {
					addLore(icon,"Food: " + development.getRegister().getCost().get("Food"));
				}
				if (type.getUpkeepCost().containsKey("Hardware")) {
					addLore(icon,"Hardware: " + development.getRegister().getCost().get("Hardware"));
				} */
			}
			if (type.getResource() != null) {
				addLore(icon, "Collection Power (base): " + development.getCollectionPower() * development.getProductivity() + " (4)"); //register.getBasePower
				addLore(icon, "Region Total: " + estate.getRegion().getResource(type.getResource().toUpperCase()));
				//addLore(icon, "Percent: " + TODO: actualYield / regionTotal);
				//TODO: Actual Yield, Food Usage, if (foodUsage != maximum) "Click to increase food usage"
			}
			
			developmentIcons.setItem(i, icon);
			i++;
		}
		for(Development development: estate.getInactiveDevelopments()) {
			Material m = Material.FIREWORK_STAR;
			ItemStack icon = new ItemStack(m);
			nameItem(icon, development.getDevelopmentType().getTitle());
			addLore(icon, ChatColor.RED + "Inactive");
			addLore(icon, "Click to Activate");
			developmentIcons.setItem(i, icon);
			i++;
		}
		
		for(String name: estate.getUnbuiltDevelopments()) {
			DevelopmentType type = DevelopmentType.getDevelopmentType(name);
			if (estate.getActiveDevelopmentsToString().containsAll(type.getPrerequisites())) {
				Material m = type.getIcon();
				ItemStack icon = new ItemStack(m);
				nameItem(icon, type.getTitle());
				addLore(icon, ChatColor.YELLOW + "Not Yet Constructed");
				addLore(icon, "");
				if (!type.getPrerequisites().isEmpty()) {
					addLore(icon, ChatColor.YELLOW + "Prerequisites:");
					for(String prerequisite : type.getPrerequisites()) addLore(icon, prerequisite);
					addLore(icon, "");
				}
				
				if (!type.getUpkeepCost().isEmpty()) {
					addLore(icon, ChatColor.YELLOW + "Upkeep Cost:");
					for(ItemStack cost : type.getUpkeepCost()) {						
						addLore(icon, cost.getType().toString() + ": " + cost.getAmount());
					}
					addLore(icon, "");
				}
				
				if(!type.getInitialCost().isEmpty() ) {
					addLore(icon, ChatColor.YELLOW + "Initial Cost:");
					for(ItemStack item : type.getInitialCost()) {
						addLore(icon, item.getType().toString() +  ": " + item.getAmount());
					}
					if(!Nobility.getDevelopmentManager().checkCosts(type, estate)) {
						addLore(icon, ChatColor.RED + "Not enough to construct this estate");
					}
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
