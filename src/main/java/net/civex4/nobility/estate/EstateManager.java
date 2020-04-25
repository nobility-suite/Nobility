package net.civex4.nobility.estate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.kingvictoria.Region;
import io.github.kingvictoria.RegionResource;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.development.behaviors.Collector;
import net.civex4.nobility.group.Group;
import net.civex4.nobility.group.GroupPermission;
import net.civex4.nobility.gui.BannerLetter;
import net.civex4.nobility.gui.ButtonLibrary;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.api.ItemNames;
import vg.civcraft.mc.civmodcore.chatDialog.Dialog;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;

public class EstateManager {
  	
	private ArrayList<Estate> estates = new ArrayList<>();
	private HashMap<UUID, Estate> estateOfPlayer = new HashMap<>();
	
	private static final int rowLength = 9;
	
	public boolean isVulnerable(Estate e) {
		int h = e.getVulnerabilityHour(); //should be between 0 and 23;
		Calendar rightNow = Calendar.getInstance();
		int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
		return currentHour >= h && currentHour < ((h+2) % 24);
	}
	
	public Estate createEstate(Block block, Player player) {
		Group group = Nobility.getGroupManager().getGroup(player);
		group.setHasEstate(true);
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

		ClickableInventory estateGUI = new ClickableInventory(rowLength * 5, estate.getGroup().getName());
		
		// OPTIONS HEADER
		for (int i = 0; i < 1; i++) {
			if (!(estateGUI.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				estateGUI.setSlot(c, i);
			}
		}		

		ItemStack[] optionsLetters = BannerLetter.createBanners("options", DyeColor.GRAY, DyeColor.WHITE);
		for (ItemStack item : optionsLetters) {
			Clickable c = new DecorationStack(item);
			estateGUI.addSlot(c);
		}
		
		// DECORATION STACKS
		for (int i = 0; i < rowLength * 1; i++) {
			if (!(estateGUI.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				estateGUI.setSlot(c, i);
			}
		}
		
		
		
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
		ItemStack renameIcon = ButtonLibrary.createIcon(Material.FEATHER, "Rename This Estate");
		Clickable estateNameButton = new Clickable(renameIcon) {

			@Override
			public void clicked(Player p) {
				
				new BukkitRunnable() {
					@Override
					public void run() {
						ClickableInventory.forceCloseInventory(p);
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
				}.runTaskLater(Nobility.getNobility(), 1);
				
			}			
		};
		estateGUI.addSlot(estateNameButton);
		
		// RELATIONSHIPS
		ItemStack relationshipIcon = ButtonLibrary.createIcon(Material.SKELETON_SKULL, "Relationships");
		Clickable relationshipButton = new Clickable(relationshipIcon) {

			@Override
			public void clicked(Player p) {
				openEstateRelationshipGUI(p);
			}
			
		};
		estateGUI.addSlot(relationshipButton);
		
		// OPEN INVENTORY		
		Inventory inv = estate.getInventory();
		if (inv != null) {
			Clickable openInventory = new Clickable(new ItemStack(Material.CHEST)) {
				@Override
				public void clicked(Player player) {
					player.openInventory(inv);
				}			
			};		
			estateGUI.addSlot(openInventory);
		}
		
		// REGION INFO
		
		ItemStack regionInfoIcon = ButtonLibrary.createIcon(Material.IRON_ORE, "Region Information");
		Clickable regionInfoButton = new Clickable(regionInfoIcon) {

			@Override
			public void clicked(Player p) {
				openRegionInfoGUI(p);
				
			}
			
		};
		estateGUI.addSlot(regionInfoButton);
		
		// SET PRODUCTIVITY
		
		ItemStack productivityIcon = ButtonLibrary.createIcon(Material.WOODEN_PICKAXE, "Set Productivity");
		Clickable productivityButton = new Clickable(productivityIcon) {

			@Override
			public void clicked(Player p) {
				openProductivityMenu(p);
				
			}
			
		};
		estateGUI.addSlot(productivityButton);
				
		// DECORATION STACKS
		for (int i = 0; i < rowLength * 3; i++) {
			if (!(estateGUI.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				estateGUI.setSlot(c, i);
			}
		}
		
		// BUILT DEVELOPMENT HEADER
		ItemStack[] builtLetters = BannerLetter.createBanners("built", DyeColor.GRAY, DyeColor.WHITE);
		for (ItemStack item : builtLetters) {
			Clickable c = new DecorationStack(item);
			estateGUI.addSlot(c);
		}
		
		// DECORATION STACKS
		
		for (int i = rowLength * 3; i < rowLength * 4; i++) {
			if (!(estateGUI.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				estateGUI.setSlot(c, i);
			}
		}
		
		// BUILT DEVELOPMENTS
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
			
//			if (!type.getUpkeepCost().isEmpty()) {			
//				addLore(icon, ChatColor.GOLD + "Upkeep Cost:");
//				for (ItemStack cost : type.getUpkeepCost()) {
//					addLore(icon, ChatColor.GRAY + ItemNames.getItemName(cost) + ": " + ChatColor.WHITE + cost.getAmount());
//				}
//			}
			
			// This indicates that the level needs to be moved to the development class


			// IF DEVELOPMENT IS CLICKED
			Clickable c = new Clickable(icon) {				
				@Override
				public void clicked(Player p) {
					//development.openGUI(p);
				}
			};
			
			estateGUI.addSlot(c);
		}
		
		// OPEN
		estateGUI.showInventory(player);
	}
	
	protected void openProductivityMenu(Player player) {
		//XXX going to break with more than 2 developments
		Estate estate = getEstateOfPlayer(player);
		ClickableInventory inv = new ClickableInventory(9, "Productivity");
		
		ItemStack availableIcon = ButtonLibrary.createIcon(Material.WOODEN_PICKAXE, "Available Productivity");
		availableIcon.setAmount(estate.getFreeProductivity());
		
		Clickable availableButton = new DecorationStack(availableIcon);
		inv.addSlot(availableButton);
		
		Clickable decor = new DecorationStack(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		inv.addSlot(decor);		
		

		inv.addSlot(ButtonLibrary.HOME.clickable());
		
		inv.showInventory(player);
	}

	protected void openRegionInfoGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		Region region = estate.getRegion();
		ClickableInventory gui = new ClickableInventory(9, region.getName());
		
		for (RegionResource resource : region.getResources().keySet()) {
			// TODO Need Nice Capitalization For The Resource
			ItemStack resourceIcon = ButtonLibrary.createIcon(resource.resource().getType(), resource.name().toLowerCase());
			resourceIcon.setAmount((int) region.getResource(resource));
			ItemAPI.addLore(resourceIcon, 
					ChatColor.GRAY + "Total Amount: " + ChatColor.WHITE + (int) region.getResource(resource),
					ChatColor.GOLD + "Collection Power: ");
			for (Estate estateInRegion : getEstatesInRegion(region)) {
				ItemAPI.addLore(resourceIcon, 
						ChatColor.GRAY + estateInRegion.getGroup().getName() + ": " + ChatColor.WHITE + estateInRegion.getCollectionPower(resource));
			}
			Clickable resourceButton = new DecorationStack(resourceIcon);
			gui.addSlot(resourceButton);
		}
		gui.setSlot(ButtonLibrary.HOME.clickable(), 8);
		
		gui.showInventory(player);		
		
		
	}

	public void openEstateRelationshipGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		if (estates.size() > (rowLength * 6)) {
			// TODO Create MultiPageView
			Bukkit.getLogger().warning("The number of estates being greater than 54 has not been handled yet "
					+ "(EstateManager.openEstateRelationshipGUI(player))");
			return;
		}
		ClickableInventory gui = new ClickableInventory(roundUpToNine(estates.size()), "Estates");
		for (Estate otherEstate : estates) {
			if (otherEstate.equals(estate)) continue;
			String name = otherEstate.getGroup().getName();
			Material mat = Material.WHITE_BANNER; // TODO add icon creation;
			ItemStack icon = ButtonLibrary.createIcon(mat, name);
			addLore(icon, "Relationship: " + estate.getRelationship(otherEstate).title());
			Clickable c = new Clickable(icon) {

				@Override
				public void clicked(Player p) {
					openSetRelationshipGUI(p, estate, otherEstate);					
				}
				
			};
			gui.addSlot(c);
		}
		gui.addSlot(ButtonLibrary.HOME.clickable());
		gui.showInventory(player);
	}
	
	public void openSetRelationshipGUI(Player player, Estate estate, Estate otherEstate) {
		ClickableInventory gui = new ClickableInventory(9, "Set Relationship");
		for (Relationship r : Relationship.values()) {
			Clickable c = new Clickable(r.icon()) {

				@Override
				public void clicked(Player p) {
					estate.addRelationship(otherEstate, r);
					p.sendMessage("Your relationship with " + estate.getGroup().getName() 
							+ " has been set to " + r.title().toLowerCase());
					openEstateRelationshipGUI(p);
				}
				
			};
			gui.addSlot(c);
		}
		gui.addSlot(ButtonLibrary.HOME.clickable());
		gui.showInventory(player);
	}

	public void openBuildGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		// TODO Estate name length can't be longer than 32
		ClickableInventory developmentGUI = new ClickableInventory(9, "Build");
		
		// UNBUILT AVAILABLE DEVELOPMENTS
//		for(DevelopmentType type: estate.getUnbuiltDevelopments()) {			
//			if (estate.getActiveDevelopmentsToString().containsAll(type.getPrerequisites())) {
//				Material m = type.getIcon();
//				ItemStack icon = new ItemStack(m);
//				nameItem(icon, type.getTitle());
//				addLore(icon, ChatColor.BLUE + "Click to build");
//				
//				if (!type.getPrerequisites().isEmpty()) {
//					addLore(icon, ChatColor.GOLD + "Prerequisites:");
//					for(String prerequisite : type.getPrerequisites()) {
//						addLore(icon, ChatColor.GRAY + DevelopmentType.getDevelopmentType(prerequisite).getTitle());
//					}
//				}
//				
//				if (!type.getUpkeepCost().isEmpty()) {
//					addLore(icon, ChatColor.GOLD + "Upkeep Cost:");
//					for(ItemStack cost : type.getUpkeepCost()) {						
//						addLore(icon, ChatColor.GRAY + ItemNames.getItemName(cost) + ": " + ChatColor.WHITE + cost.getAmount());
//					}
//				}
//				
//				if(!type.getInitialCost().isEmpty() ) {
//					addLore(icon, ChatColor.GOLD + "Initial Cost:");
//					for(ItemStack cost : type.getInitialCost()) {
//						addLore(icon, ChatColor.GRAY + ItemNames.getItemName(cost) +  ": " + ChatColor.WHITE + cost.getAmount());
//					}
//					if(!Nobility.getDevelopmentManager().checkCosts(type, estate)) {
//						addLore(icon, ChatColor.RED + "Not enough to construct this estate");
//					}
//				}
//				
//				// IF UNBUILT DEVELOPMENT IS CLICKED:
//				Clickable c = new Clickable(icon) {				
//					@Override
//					public void clicked(Player p) {
//						if (!Nobility.getDevelopmentManager().checkCosts(type, estate)) {
//							player.sendMessage("You don't have enough to construct this development");
//							return;
//						}
//						estate.buildDevelopment(type);
//						player.sendMessage("You constructed a " + type.getTitle());
//						openEstateGUI(p);
//					}
//				};
//				
//				developmentGUI.addSlot(c);
//			}			
//		}

		developmentGUI.addSlot(ButtonLibrary.HOME.clickable());
		
		developmentGUI.showInventory(player);
	}
	
	public List<Estate> getEstates() {
		return estates;
	}
	
	// Player-Estate map
	public void setEstateOfPlayer(Player p, Estate e) {
		estateOfPlayer.put(p.getUniqueId(), e);
	}
	
	public Estate getEstateOfPlayer(Player p) {
		return estateOfPlayer.get(p.getUniqueId());
	}
	
	public boolean playerHasEstate(Player p) {
		return estateOfPlayer.containsKey(p.getUniqueId());
	}
	
	// Region Estate
	public List<Estate> getEstatesInRegion(Region region) {
		List<Estate> estatesInRegion = new ArrayList<>();
		for (Estate estate : estates) {
			if (estate.getRegion().equals(region)) {
				estatesInRegion.add(estate);
			}
		}
		return estatesInRegion;
	}
	
	// Utilities
	private static void nameItem(ItemStack item, String name) {
		ItemAPI.setDisplayName(item, ChatColor.WHITE + name);
	}

	private static void addLore(ItemStack item, String text) {
		ItemAPI.addLore(item, text);
	}

	private static int roundUpToNine(int number) {
		return rowLength * ((number / 9) + 1);
	}
	
	public void sendInfoMessage(Estate e, Player p) {
	  
	  Calendar rightNow = Calendar.getInstance();
      int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
      int minutes = rightNow.get(Calendar.MINUTE);
      
	  Group g = e.getGroup();
	  p.sendMessage(ChatColor.GOLD + "-=- " + g.getName() + " -=-");
	  p.sendMessage(ChatColor.GOLD + "Region: " + ChatColor.YELLOW + e.getRegion().getName());
	  p.sendMessage(ChatColor.GOLD + "Leader: " + ChatColor.YELLOW 
	      + g.getLocalization(GroupPermission.LEADER)
	      + " " + g.getLeader().getName());
	  
	  sendOfficialsMessage(e,p);
	  
	  p.sendMessage(ChatColor.GOLD + "Total Members: " + ChatColor.YELLOW + g.getMembers().size());
	  p.sendMessage(ChatColor.GOLD + "Location: " + ChatColor.YELLOW + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z");
	  p.sendMessage(ChatColor.GOLD + "Siege Window: " + ChatColor.YELLOW + e.getVulnerabilityHour() + " to " + ((e.getVulnerabilityHour() + 2) % 24) + ChatColor.GOLD + " | Current Time: " + ChatColor.YELLOW + currentHour + ":" + minutes + ".");
	  return; //TODO
	}
	
	public void sendOfficialsMessage(Estate e, Player p) {
	   Group g = e.getGroup();
	   ArrayList<String> list = g.getOfficials();
	   
	   int extras = 0;
	   
	   if(list.size() > 4) {
	      extras = list.size()-4;
	   }
	   
	   String message = ChatColor.GOLD + "Officials: " + ChatColor.YELLOW;
	   
	   if(list.size() == 0) {
	     message += "None. ";
	     return;
	   }
	   
	   if(extras > 0) {
	     for(int i = 0; i < 4; i++) {
	       message += g.getLocalization(GroupPermission.OFFICER) + " " + list.get(i) + ", ";
	       if(i == 3) {
	         message += "and " + extras + " more...";
	       }
	       
	     }
	   }else {
	     for(String name : list) {
           message += g.getLocalization(GroupPermission.OFFICER) + " " + name + ", ";
	     }
	   }
	   
	   p.sendMessage(message);
	  
	}
	
	public void sendMembersMessage(Estate e, Player p) {
	  
	}

	
	
}
