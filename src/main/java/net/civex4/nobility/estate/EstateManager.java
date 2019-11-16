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

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.development.behaviors.Upgradable;
import net.civex4.nobility.group.Group;
import net.civex4.nobility.gui.ButtonLibrary;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.api.ItemNames;
import vg.civcraft.mc.civmodcore.chatDialog.Dialog;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;

public class EstateManager {
	
	private ArrayList<Estate> estates = new ArrayList<Estate>();
	private HashMap<Player, Estate> estateOfPlayer = new HashMap<Player, Estate>();
		
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
	
	/* Need to create a menu with the options players can take with a development
	 * The options are upgrade, enable, disable, see other developments in region,
	 * and destroy, plus any additional features. Storehouse needs a feature, "open
	 * storehouse inventory" 
	 */
	
	public void openEstateGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		final int rowLength = 9;
		ClickableInventory estateGUI = new ClickableInventory(rowLength * 3, estate.getGroup().getName());

		// BUTTONS:
		// BUILD GUI
		ItemStack buildGUIIcon = ButtonLibrary.createIcon(Material.CRAFTING_TABLE, "Build a Development");
		Clickable buildButton = new Clickable(buildGUIIcon) {
			@Override
			public void clicked(Player p) {
				openBuildGUI(p);
			}			
		};
		estateGUI.addSlot(buildButton);
		
		// RENAME ESTATE
		ItemStack renameIcon = ButtonLibrary.createIcon(Material.FEATHER, "Rename this Estate");
		Clickable estateNameButton = new Clickable(renameIcon) {

			@Override
			public void clicked(Player p) {
				p.closeInventory();
				new Dialog(player, Nobility.getNobility(), "Enter in a new name:") {
					
					@Override
					public List<String> onTabComplete(String wordCompleted, String[] fullMessage) {
						return null;
					}
					
					@Override
					public void onReply(String[] message) {
						// Set messages to one word
						String newName = "";
						for (String str : message) {newName = newName + str + " ";}
						
						estate.getGroup().setName(newName);
						
						player.sendMessage("This Estate is now called " + newName);
						this.end();
					}
				};
				
			}			
		};
		estateGUI.addSlot(estateNameButton);
		
		// SEE ESTATES IN REGION TODO
		
		// DECORATION STACKS
		for (int i = 0; i < rowLength * 2; i++) {
			if (!(estateGUI.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				estateGUI.setSlot(c, i);
			}
		}

		// BUILT DEVELOPMENTS:
		for(Development development: estate.getBuiltDevelopments()) {
			DevelopmentType type = development.getType();
			ItemStack icon;
			if (development.isActive()) {
				Material m = type.getIcon();
				icon = new ItemStack(m);
				nameItem(icon, type.getTitle());
				addLore(icon, ChatColor.GREEN + "Active");
			} else {
				Material m = Material.FIREWORK_STAR;
				icon = new ItemStack(m);
				nameItem(icon, development.getType().getTitle());
				addLore(icon, ChatColor.RED + "Inactive");
			}
			
			if (!type.getUpkeepCost().isEmpty()) {			
				addLore(icon, ChatColor.YELLOW + "Upkeep Cost:");
				for (ItemStack cost : type.getUpkeepCost()) {
					addLore(icon, ItemNames.getItemName(cost) + ": " + cost.getAmount());
				}
			}
			
			// This indicates that the level needs to be moved to the development class
			if (development.getCollector() instanceof Upgradable) {
				addLore(icon, ChatColor.YELLOW + "Level " + development.getCollector().getLevel());
			}

			// IF DEVELOPMENT IS CLICKED
			Clickable c = new Clickable(icon) {				
				@Override
				public void clicked(Player p) {
					development.openGUI(p);
				}
			};
			
			estateGUI.addSlot(c);
		}
		
		// OPEN
		estateGUI.showInventory(player);
	}
	
	public void openBuildGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		// TODO Estate name length can't be longer than 32
		ClickableInventory developmentGUI = new ClickableInventory(9, "Build");
		
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
						openEstateGUI(p);
					}
				};
				
				developmentGUI.addSlot(c);
			}			
		}

		developmentGUI.addSlot(ButtonLibrary.HOME.clickable());
		
		developmentGUI.showInventory(player);
	}
	
	public List<Estate> getEstates() {
		return estates;
	}
	
	// Player-Estate map
	public void setEstateOfPlayer(Player p, Estate e) {
		estateOfPlayer.put(p, e);
	}
	
	public Estate getEstateOfPlayer(Player p) {
		return estateOfPlayer.get(p);
	}
	
	public boolean playerHasEstate(Player p) {
		return estateOfPlayer.containsKey(p);
	}
	
	// Utilities
	private static void nameItem(ItemStack item, String name) {
		ItemAPI.setDisplayName(item, ChatColor.WHITE + name);
	}

	private static void addLore(ItemStack item, String text) {
		ItemAPI.addLore(item, text);
	}

	
}
