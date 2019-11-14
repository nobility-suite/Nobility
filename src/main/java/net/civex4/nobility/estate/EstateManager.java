package net.civex4.nobility.estate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.group.Group;
import net.civex4.nobility.gui.TextInputButton;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.api.ItemNames;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

public class EstateManager {
	
	public ArrayList<Estate> estates = new ArrayList<Estate>();
	
	//HashMap player-estate
	private HashMap<Player, Estate> estateOfPlayer = new HashMap<Player, Estate>();
	
	/*public EstateManager() {
		ArrayList<Estate> estates = new ArrayList<Estate>();
	}*/
	
	public boolean isVulnerable(Estate e) {
		int h = e.getVulnerabilityHour(); //should be between 0 and 23;
		Calendar rightNow = Calendar.getInstance();
		int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
		return currentHour >= h && currentHour < ((h+2) % 24);
	}
	
	public Estate createEstate(Block block, Player player) {
		Group group = Nobility.getGroupManager().getGroup(player);	
		block.setType(Material.ENDER_CHEST);		
		Estate estate = new Estate(block, group);		
		estates.add(estate);
		
		player.sendMessage("You have created an estate for " + group.getName());
		setEstateOfPlayer(player, estate);
		
		return estate;
		
	}
	
	public void openEstateGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		ClickableInventory estateGUI = new ClickableInventory(9, estate.getGroup().getName());
		
		// BUTTONS:
		// DEVELOPMENT GUI
		ItemStack developmentGUIIcon = createIcon(Material.CRAFTING_TABLE, "Developments");
		Clickable developmentButton = new Clickable(developmentGUIIcon) {
			@Override
			public void clicked(Player p) {
				openDevelopmentGUI(p);
			}			
		};
		estateGUI.addSlot(developmentButton);
		
		// RENAME ESTATE
		ItemStack renameIcon = createIcon(Material.FEATHER, "Rename");
		TextInputButton estateNameButton = new TextInputButton(renameIcon, estate.getGroup().name);
		estateGUI.addSlot(estateNameButton);
		
		// OPEN
		estateGUI.showInventory(player);
	}
	
	
	// TODO Could use CivModCore for item renaming
	public void openDevelopmentGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		ClickableInventory developmentGUI = new ClickableInventory(9, "Developments of " + estate.getGroup().getName());
		// BUTTONS:
		// ACTIVE DEVELOPMENTS:
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

			// IF ACTIVE DEVELOPMENT IS CLICKED
			Clickable c = new Clickable(icon) {				
				@Override
				public void clicked(Player p) {
					// TODO open development options menu
					String developmentName = development.getDevelopmentType().getTitle();
					development.deactivate();
					development.setActive(false);
					player.sendMessage(developmentName + " is now inactive");
					player.closeInventory();
					openDevelopmentGUI(p);
				}
			};
			
			developmentGUI.addSlot(c);
		}
		
		// INACTIVE DEVELOPMENTS:
		for(Development development: estate.getInactiveDevelopments()) {
			Material m = Material.FIREWORK_STAR;
			ItemStack icon = new ItemStack(m);
			nameItem(icon, development.getDevelopmentType().getTitle());
			addLore(icon, ChatColor.RED + "Inactive");
			addLore(icon, "Click to Activate");

			
			// IF INACTIVE DEVELOPMENT IS CLICKED
			Clickable c = new Clickable(icon) {				
				@Override
				public void clicked(Player p) {
					// TODO if development has enough food...
					String developmentName = development.getDevelopmentType().getTitle();
					development.activate();
					development.setActive(true);
					player.sendMessage(developmentName + " is now active");
					player.closeInventory();
					openDevelopmentGUI(p);
				}
			};
			
			developmentGUI.addSlot(c);
		}
		
		// UNBUILT AVAILABLE DEVELOPMENTS
		for(DevelopmentType type: estate.getUnbuiltDevelopments()) {			
			if (estate.getActiveDevelopmentsToString().containsAll(type.getPrerequisites())) {
				Material m = type.getIcon();
				ItemStack icon = new ItemStack(m);
				nameItem(icon, type.getTitle());
				addLore(icon, ChatColor.YELLOW + "Not Yet Constructed");
				
				if (!type.getPrerequisites().isEmpty()) {
					addLore(icon, ChatColor.YELLOW + "Prerequisites:");
					for(String prerequisite : type.getPrerequisites()) {
						addLore(icon, DevelopmentType.getDevelopmentType(prerequisite).getTitle());
					}
					addLore(icon, "");
				}
				
				if (!type.getUpkeepCost().isEmpty()) {
					addLore(icon, ChatColor.YELLOW + "Upkeep Cost:");
					for(ItemStack cost : type.getUpkeepCost()) {						
						addLore(icon, ItemNames.getItemName(cost) + ": " + cost.getAmount());
					}
					addLore(icon, "");
				}
				
				if(!type.getInitialCost().isEmpty() ) {
					addLore(icon, ChatColor.YELLOW + "Initial Cost:");
					for(ItemStack cost : type.getInitialCost()) {
						addLore(icon, ItemNames.getItemName(cost) +  ": " + cost.getAmount());
					}
					if(!Nobility.getDevelopmentManager().checkCosts(type, estate)) {
						addLore(icon, ChatColor.RED + "Not enough to construct this estate");
					}
				}
				
				// IF UNBUILT DEVELOPMENT IS CLICKED:
				Clickable c = new Clickable(icon) {				
					@Override
					public void clicked(Player p) {
						if (!Nobility.getDevelopmentManager().checkCosts(type, estate)) {
							player.sendMessage("You don't have enough to construct this development");
							return;
						}
						estate.buildDevelopment(type);
						player.sendMessage("You constructed a " + type.getTitle());
						player.closeInventory();
						openDevelopmentGUI(p);
					}
				};
				
				developmentGUI.addSlot(c);
			}			
		}
		
		developmentGUI.showInventory(player);
	}
	
	//Utility method to rename an item. Returns bold.
	public static ItemStack nameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BOLD + name);		
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
	}
	
	public static ItemStack createIcon(Material mat, String name) {
		ItemStack icon = new ItemStack(mat);
		ItemAPI.setDisplayName(icon, ChatColor.WHITE + name);
		return icon;
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
