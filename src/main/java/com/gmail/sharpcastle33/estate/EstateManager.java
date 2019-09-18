package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.gmail.sharpcastle33.development.DevelopmentRegister;
import com.gmail.sharpcastle33.group.Group;

public class EstateManager {
	
	public ArrayList<Estate> estates = new ArrayList<Estate>();
	
	//hashmap player-estate
	private HashMap<Player, Estate> estateOfPlayer = new HashMap<Player, Estate>();
	
	/*public EstateManager() {
		ArrayList<Estate> estates = new ArrayList<Estate>();
	}*/
	
	public boolean isVulnerable(Estate e) {
	  int h = e.getVulnerabilityHour(); //should be between 0 and 23;
	  Date dt = new Date();
	  int currentHour = dt.getHours();
	  if(currentHour >= h && currentHour < h+2) {
	    return true;
	  }
	  return false;
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
			Material m = development.getRegister().getIcon();
			ItemStack icon = new ItemStack(m);
			nameItem(icon, development.getName());
			addLore(icon, ChatColor.GREEN + "Active");
			if (!development.getRegister().getCost().isEmpty()) {			
				addLore(icon, ChatColor.YELLOW + "Upkeep Cost:");
				if (development.getRegister().getCost().containsKey("Food")) {
					addLore(icon,"Food: " + development.getRegister().getCost().get("Food"));
				}
				if (development.getRegister().getCost().containsKey("Hardware")) {
					addLore(icon,"Hardware: " + development.getRegister().getCost().get("Hardware"));
				}
			}
			if (development.getRegister().getResource() != null) {
				addLore(icon, "Collection Power (base): " + development.getRegister().getCollectionPower() * development.getRegister().getProductivity() + " (4)"); //register.getBasePower
				addLore(icon, "Region Total: " + estate.getRegion().getResource(development.getRegister().getResource()));
				//addLore(icon, "Percent: " + TODO: actualYield / regionTotal);
				//TODO: Actual Yield, Food Usage, if (foodUsage != maximum) "Click to increase food usage"
			}
			
			developmentIcons.setItem(i, icon);
			i++;
		}
		for(Development development: estate.getInactiveDevelopments()) {
			Material m = Material.FIREWORK_STAR;
			ItemStack icon = new ItemStack(m);
			nameItem(icon, development.getName());
			addLore(icon, ChatColor.RED + "Inactive");
			addLore(icon, "Click to Activate");
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
				if (!register.getPrerequisites().isEmpty()) {
					addLore(icon, ChatColor.YELLOW + "Prerequisites:");
					for(String prerequisite: register.getPrerequisites()) addLore(icon, prerequisite);
					addLore(icon, "");
				}
				
				if (!register.getCost().isEmpty()) {
					addLore(icon, ChatColor.YELLOW + "Upkeep Cost:");
					for(String material: register.getCost().keySet()) addLore(icon, material + ": " + register.getCost().get(material));
					addLore(icon, "");
				}
				
				if(!register.getInitialCost().isEmpty()) {
					addLore(icon, ChatColor.YELLOW + "Initial Cost:");
					for(ItemStack item : register.getInitialCost()) {
						addLore(icon, item.getType().toString() +  ": " + item.getAmount());
					}
					if(!Nobility.getDevelopmentManager().checkCosts(register, estate)) {
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
