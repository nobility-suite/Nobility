package net.civex4.nobility.estate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.kingvictoria.Region;
import io.github.kingvictoria.nodes.Node;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentBlueprint;
import net.civex4.nobility.group.Group;
import net.civex4.nobility.group.GroupPermission;
import net.civex4.nobility.gui.ButtonLibrary;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
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
	

/* ============================================================
 * MENU CODE
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * GUI CODE AHEAD
 * ============================================================
 */

	public void openEstateGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);

		int[] decoSlots = {0,2,3,4,5,6,8,9,10,11,12,13,14,15,16,17,18,20,22,24,26,27,28,29,30,31,32,33,34,35,36,38,40,42,44,45,46,47,48,49,50,51,52,53};
		ClickableInventory estateGUI = new ClickableInventory(rowLength * 6, "Nobility Menu (" + estate.getGroup().getName() + ")");
		
		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(estateGUI.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				estateGUI.setSlot(c, i);
			}
		}
		
		//INFO BOOK
		ItemStack info = ButtonLibrary.createIcon(Material.BOOK, ChatColor.GOLD + estate.getGroup().getName());
		ItemAPI.addLore(info, ChatColor.BLUE + "Members: " + ChatColor.WHITE + "" + estate.getGroup().getMembers().size(),
				ChatColor.BLUE + "Leader: " + ChatColor.WHITE + "" + estate.getGroup().getLocalization(GroupPermission.LEADER) + " " + estate.getGroup().getLeader().getDisplayName(),
				ChatColor.BLUE + "Region: " + ChatColor.WHITE + estate.getRegion().getName(),
				ChatColor.BLUE + "Location: " + ChatColor.WHITE + estate.getBlock().getX() + "X, " + estate.getBlock().getZ() + "Z",
				ChatColor.BLUE + "Vulnerability Hour: " + ChatColor.WHITE + estate.getVulnerabilityHour());
		Clickable infoIcon = new Clickable(info) {

			@Override
			public void clicked(Player p) {

			}
		};
		estateGUI.addSlot(infoIcon);
		
		// REGION INFO
		
				ItemStack regionInfoIcon = ButtonLibrary.createIcon(Material.IRON_ORE, "Region Information");
				Clickable regionInfoButton = new Clickable(regionInfoIcon) {

					@Override
					public void clicked(Player p) {
						openRegionInfoGUI(p);
						
					}
					
				};
				estateGUI.addSlot(regionInfoButton);
		
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

		//CLAIMS
		ItemStack claimIcon = ButtonLibrary.createIcon(Material.FILLED_MAP, "Claim a Node");
		Clickable claimButton = new Clickable(claimIcon) {

			@Override
			public void clicked(Player p) {
				openClaimGUI(p);
			}
			
		};
		estateGUI.addSlot(claimButton);
		
		//WORKERS
		ItemStack workerIcon = ButtonLibrary.createIcon(Material.IRON_PICKAXE, "Assign Workers");
		Clickable workerButton = new Clickable(workerIcon) {

			@Override
			public void clicked(Player p) {

			}
			
		};
		estateGUI.addSlot(workerButton);

		// RELATIONSHIPS
		ItemStack relationshipIcon = ButtonLibrary.createIcon(Material.SKELETON_SKULL, "Relationships");
		Clickable relationshipButton = new Clickable(relationshipIcon) {

			@Override
			public void clicked(Player p) {
				openEstateRelationshipGUI(p);
			}
			
		};
		estateGUI.addSlot(relationshipButton);
		
		// VIEW DEVELOPMENTS
		ItemStack devIcon = ButtonLibrary.createIcon(Material.FURNACE, "View Developments");
		Clickable devButton = new Clickable(devIcon) {

			@Override
			public void clicked(Player p) {
				openDevelopmentsGUI(p);
			}
			
		};
		estateGUI.addSlot(devButton);
		
		// VIEW STOCKPILES
		ItemStack stockIcon = ButtonLibrary.createIcon(Material.CHEST, "View Stockpiles");
		Clickable stockButton = new Clickable(stockIcon) {

			@Override
			public void clicked(Player p) {
				
			}
			
		};
		estateGUI.addSlot(stockButton);
		
		
		// DEFENCE
		ItemStack defIcon = ButtonLibrary.createIcon(Material.STONE_BRICKS, "Defence");
		Clickable defButton = new Clickable(defIcon) {

			@Override
			public void clicked(Player p) {
				
			}
			
		};
		estateGUI.addSlot(defButton);
		
		// MEMBERS
		ItemStack membersIcon = ButtonLibrary.createIcon(Material.PLAYER_HEAD, "View Citizens");
		Clickable membersButton = new Clickable(membersIcon) {

			@Override
			public void clicked(Player p) {
				openMembersGUI(p);
			}
			
		};
		estateGUI.addSlot(membersButton);
		
		// OPEN
		estateGUI.showInventory(player);
		
		
	}
	
	
	private void openMembersGUI(Player p) {
		Estate estate = getEstateOfPlayer(p);
		ClickableInventory gui = new ClickableInventory(54, "View Citizens");
		  int[] decoSlots = {0,1,2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,45,46,47,48,50,51,52,53};
			
			// DECORATION STACKS
			for (int i : decoSlots) {
				if (!(gui.getSlot(i) instanceof Clickable)) {
					Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
					gui.setSlot(c, i);
				}
			}
			
			Estate e = estate;
			
			ItemStack info = ButtonLibrary.createIcon(Material.BOOK, ChatColor.GOLD + e.getGroup().getName());
			ItemAPI.addLore(info, ChatColor.BLUE + "Members: " + ChatColor.WHITE + "" + e.getGroup().getMembers().size(),
					ChatColor.BLUE + "Leader: " + ChatColor.WHITE + "" + e.getGroup().getLocalization(GroupPermission.LEADER) + " " + estate.getGroup().getLeader().getDisplayName(),
					ChatColor.BLUE + "Region: " + ChatColor.WHITE + e.getRegion().getName(),
					ChatColor.BLUE + "Location: " + ChatColor.WHITE + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z",
					ChatColor.BLUE + "Vulnerability Hour: " + ChatColor.WHITE + e.getVulnerabilityHour());
			Clickable infoIcon = new DecorationStack(info);
			gui.addSlot(infoIcon);
			
			gui.setSlot(ButtonLibrary.HOME.clickable(),49);
			
			Set<UUID> members = estate.getGroup().getMembers();
			
			for(UUID u : members) {
				Player pl = Bukkit.getPlayer(u);
				ItemStack playerIcon = ButtonLibrary.createIcon(Material.PLAYER_HEAD, pl.getName());
				ItemAPI.addLore(playerIcon, ChatColor.BLUE + "Rank: " + ChatColor.WHITE + estate.getGroup().getPermission(pl));
				SkullMeta im = (SkullMeta) ItemAPI.getItemMeta(playerIcon);
				im.setOwningPlayer(Bukkit.getOfflinePlayer(u));
				playerIcon.setItemMeta(im);
				Clickable pcon = new DecorationStack(playerIcon);
				gui.addSlot(pcon);
			}
	}
	
	private void openDevelopmentsGUI(Player p) {
	// TODO Auto-generated method stub
		
		Estate estate = getEstateOfPlayer(p);
		// TODO Estate name length can't be longer than 32
		ClickableInventory gui = new ClickableInventory(54, "View Developments");
		
		HashMap<String,DevelopmentBlueprint> blueprints = Nobility.getDevelopmentManager().getBlueprints();
		List<Development> built = estate.getBuiltDevelopments();
		
       int[] decoSlots = {0,1,2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,45,46,47,48,50,51,52,53};
		
		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}
		
		Estate e = estate;
		
		ItemStack info = ButtonLibrary.createIcon(Material.BOOK, ChatColor.GOLD + e.getGroup().getName());
		ItemAPI.addLore(info, ChatColor.BLUE + "Members: " + ChatColor.WHITE + "" + e.getGroup().getMembers().size(),
				ChatColor.BLUE + "Leader: " + ChatColor.WHITE + "" + e.getGroup().getLocalization(GroupPermission.LEADER) + " " + estate.getGroup().getLeader().getDisplayName(),
				ChatColor.BLUE + "Region: " + ChatColor.WHITE + e.getRegion().getName(),
				ChatColor.BLUE + "Location: " + ChatColor.WHITE + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z",
				ChatColor.BLUE + "Vulnerability Hour: " + ChatColor.WHITE + e.getVulnerabilityHour());
		Clickable infoIcon = new DecorationStack(info);
		gui.addSlot(infoIcon);
		
		gui.setSlot(ButtonLibrary.HOME.clickable(),49);
		
		for(Development d : built) {
			ItemStack icon = ButtonLibrary.createIcon(Material.STONE, d.name);
			
			Clickable dicon = new DecorationStack(icon);
			gui.addSlot(dicon);
		}
		
		gui.showInventory(p);
	
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
		ClickableInventory gui = new ClickableInventory(54, region.getName());
		
		int[] decoSlots = {0,8,9,10,11,12,13,14,15,16,17,18,26,27,35,36,44,45,46,47,48,50,51,52,53};
		
		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}
		
		List<Estate> estates = Nobility.getEstateManager().getEstatesInRegion(region);
		int count = 0;
		for(Estate e : estates) {
			//TODO refactor estate info button into its own method for reusability
			ItemStack info = ButtonLibrary.createIcon(Material.BOOK, ChatColor.GOLD + e.getGroup().getName());
			ItemAPI.addLore(info, ChatColor.BLUE + "Members: " + ChatColor.WHITE + "" + e.getGroup().getMembers().size(),
					ChatColor.BLUE + "Leader: " + ChatColor.WHITE + "" + e.getGroup().getLocalization(GroupPermission.LEADER) + " " + estate.getGroup().getLeader().getDisplayName(),
					ChatColor.BLUE + "Region: " + ChatColor.WHITE + e.getRegion().getName(),
					ChatColor.BLUE + "Location: " + ChatColor.WHITE + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z",
					ChatColor.BLUE + "Vulnerability Hour: " + ChatColor.WHITE + e.getVulnerabilityHour());
			Clickable infoIcon = new Clickable(info) {

				@Override
				public void clicked(Player p) {

				}
			};
			gui.addSlot(infoIcon);
			count++;
		}
		
		if(count < 7) {
			int fill = 7-count;
			int offset = count;
			for(int i = offset; i <= fill+count; i++) {
				if (!(gui.getSlot(i) instanceof Clickable)) {
					Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
					gui.setSlot(c, i);
				}
			}
		}
		
		ArrayList<Node> nodes = Nobility.getNobilityRegions().getNodeManager().getNodes(region);
		
		for(Node n : nodes) {
			Estate owner = Nobility.getClaimManager().claims.get(n); 
			
			String ownerName;
			if(owner == null) { ownerName = ChatColor.GRAY + "None"; 
			}else if(owner == estate) { ownerName = ChatColor.GREEN + estate.getGroup().getName();
			}else ownerName = ChatColor.RED + owner.getGroup().getName();
			
			String name = ChatColor.YELLOW + n.name + ChatColor.WHITE + " (" + ownerName + ChatColor.WHITE + ")";
			ArrayList<ItemStack> output = n.output;
			ItemStack resourceIcon = ButtonLibrary.createIcon(Material.STONE, name);
			Clickable resourceButton = new DecorationStack(resourceIcon);
			ItemAPI.addLore(resourceIcon, ChatColor.BLUE + "Slots: (0/" + n.slots + ")",
					ChatColor.BLUE + "Output:");
			
			if(output != null && output.size() > 0) {
				for(ItemStack i : output) {
					
					String iname = "";
					
					if(i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
						iname = i.getItemMeta().getDisplayName();
					}else iname = i.getType().name();
					
					ItemAPI.addLore(resourceIcon, ChatColor.GRAY + "  " + i.getAmount() + "x " + ChatColor.WHITE + iname );
				}
			}
			
			gui.addSlot(resourceButton);

			
		}
//		for (RegionResource resource : region.getResources().keySet()) {
//			// TODO Need Nice Capitalization For The Resource
//			ItemStack resourceIcon = ButtonLibrary.createIcon(resource.resource().getType(), resource.name().toLowerCase());
//			resourceIcon.setAmount((int) region.getResource(resource));
//			ItemAPI.addLore(resourceIcon, 
//					ChatColor.GRAY + "Total Amount: " + ChatColor.WHITE + (int) region.getResource(resource),
//					ChatColor.GOLD + "Collection Power: ");
//			for (Estate estateInRegion : getEstatesInRegion(region)) {
//				ItemAPI.addLore(resourceIcon, 
//						ChatColor.GRAY + estateInRegion.getGroup().getName() + ": " + ChatColor.WHITE + estateInRegion.getCollectionPower(resource));
//			}
//			Clickable resourceButton = new DecorationStack(resourceIcon);
//			gui.addSlot(resourceButton);
//		}
		gui.setSlot(ButtonLibrary.HOME.clickable(), 49);
		
		gui.showInventory(player);		
		
		
	}
	
	private void openClaimGUI(Player player) {
		Estate estate = getEstateOfPlayer(player);
		Region region = estate.getRegion();
		ClickableInventory gui = new ClickableInventory(54, "Claim Menu: " + region.getName());
		
		int[] decoSlots = {0,8,9,10,11,12,13,14,15,16,17,18,26,27,35,36,44,45,46,47,48,50,51,52,53};
		
		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}
		
		List<Estate> estates = Nobility.getEstateManager().getEstatesInRegion(region);
		int count = 0;
		for(Estate e : estates) {
			//TODO refactor estate info button into its own method for reusability
			ItemStack info = ButtonLibrary.createIcon(Material.BOOK, ChatColor.GOLD + e.getGroup().getName());
			ItemAPI.addLore(info, ChatColor.BLUE + "Members: " + ChatColor.WHITE + "" + e.getGroup().getMembers().size(),
					ChatColor.BLUE + "Leader: " + ChatColor.WHITE + "" + e.getGroup().getLocalization(GroupPermission.LEADER) + " " + estate.getGroup().getLeader().getDisplayName(),
					ChatColor.BLUE + "Region: " + ChatColor.WHITE + e.getRegion().getName(),
					ChatColor.BLUE + "Location: " + ChatColor.WHITE + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z",
					ChatColor.BLUE + "Vulnerability Hour: " + ChatColor.WHITE + e.getVulnerabilityHour());
			Clickable infoIcon = new Clickable(info) {

				@Override
				public void clicked(Player p) {

				}
			};
			gui.addSlot(infoIcon);
			count++;
		}
		
		if(count < 7) {
			int fill = 7-count;
			int offset = count;
			for(int i = offset; i <= fill+count; i++) {
				if (!(gui.getSlot(i) instanceof Clickable)) {
					Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
					gui.setSlot(c, i);
				}
			}
		}
		
		ArrayList<Node> nodes = Nobility.getNobilityRegions().getNodeManager().getNodes(region);
		
		for(Node n : nodes) {
			Estate owner = Nobility.getClaimManager().claims.get(n); 
			
			String ownerName;
			if(owner == null) { ownerName = ChatColor.GRAY + "None"; 
			}else if(owner == estate) { ownerName = ChatColor.GREEN + estate.getGroup().getName();
			}else ownerName = ChatColor.RED + owner.getGroup().getName();
			
			String name = ChatColor.YELLOW + n.name + ChatColor.WHITE + " (" + ownerName + ChatColor.WHITE + ")";
			ArrayList<ItemStack> output = n.output;
			ItemStack resourceIcon = ButtonLibrary.createIcon(Material.STONE, name);
			Clickable resourceButton = new DecorationStack(resourceIcon);
			ItemAPI.addLore(resourceIcon, ChatColor.BLUE + "Slots: (0/" + n.slots + ")",
					ChatColor.BLUE + "Output:");
			
			if(output != null && output.size() > 0) {
				for(ItemStack i : output) {
					
					String iname = "";
					
					if(i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
						iname = i.getItemMeta().getDisplayName();
					}else iname = i.getType().name();
					
					ItemAPI.addLore(resourceIcon, ChatColor.GRAY + "  " + i.getAmount() + "x " + ChatColor.WHITE + iname );
				}
			}
			
			if(owner == null) {
				ItemAPI.addLore(resourceIcon, " ",
						ChatColor.YELLOW + "" + ChatColor.BOLD + "Left click to claim!");
				Clickable claimButton = new Clickable(resourceIcon) {

					@Override
					public void clicked(Player p) {
						p.sendMessage(ChatColor.GREEN + "Claimed " + n.name + " for" + estate.getGroup().getName());
						p.closeInventory();
						Nobility.getClaimManager().claim(n, estate);
					}
				};
				gui.addSlot(claimButton);
			}else {
				gui.addSlot(resourceButton);
			}
			

			
		}
		gui.setSlot(ButtonLibrary.HOME.clickable(), 49);
		
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
		ClickableInventory gui = new ClickableInventory(54, "Build");
		
		HashMap<String,DevelopmentBlueprint> blueprints = Nobility.getDevelopmentManager().getBlueprints();
		List<Development> built = estate.getBuiltDevelopments();
		
       int[] decoSlots = {0,2,3,4,5,6,8,9,10,11,12,13,14,15,16,17,45,46,47,48,50,51,52,53};
		
		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}
		
		Estate e = estate;
		
		ItemStack info = ButtonLibrary.createIcon(Material.BOOK, ChatColor.GOLD + e.getGroup().getName());
		ItemAPI.addLore(info, ChatColor.BLUE + "Members: " + ChatColor.WHITE + "" + e.getGroup().getMembers().size(),
				ChatColor.BLUE + "Leader: " + ChatColor.WHITE + "" + e.getGroup().getLocalization(GroupPermission.LEADER) + " " + estate.getGroup().getLeader().getDisplayName(),
				ChatColor.BLUE + "Region: " + ChatColor.WHITE + e.getRegion().getName(),
				ChatColor.BLUE + "Location: " + ChatColor.WHITE + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z",
				ChatColor.BLUE + "Vulnerability Hour: " + ChatColor.WHITE + e.getVulnerabilityHour());
		Clickable infoIcon = new DecorationStack(info);
		gui.addSlot(infoIcon);
		
		gui.setSlot(ButtonLibrary.HOME.clickable(),49);
		
		ItemStack tips = ButtonLibrary.createIcon(Material.PAPER, ChatColor.BLUE + "Tips");
		Clickable tipsIcon = new DecorationStack(tips);
		gui.addSlot(tipsIcon);
		
		//Remove built developments
		for(Development d : built) {

			if(blueprints.containsKey(d.name)) {
				blueprints.remove(d.name);
			}
		}
		
		for(DevelopmentBlueprint b : blueprints.values()) {
			if(!b.hasPrereqs) {
				String formattedName = b.result.name;
				ItemStack icon = ButtonLibrary.createIcon(b.result.icon, formattedName);
				ItemAPI.addLore(icon, ChatColor.BLUE + "Cost:");
				
				for(String s : b.cost.keySet()) {
					ItemAPI.addLore(icon, ChatColor.GRAY + "  " + b.cost.get(s) + "x" + ChatColor.WHITE + " " + s);
				}
				ItemAPI.addLore(icon, ChatColor.BLUE + "Description: ");
				ItemAPI.addLore(icon, ChatColor.GRAY + b.result.buildDescription);

				
				Clickable button = new Clickable(icon) {

					@Override
					public void clicked(Player p) {
						Nobility.getDevelopmentManager().build(b, e, player);
					}
				};
				gui.addSlot(button);
			}
		}
		
		gui.showInventory(player);
	}
	
	// RENAME ESTATE
//			ItemStack renameIcon = ButtonLibrary.createIcon(Material.FEATHER, "Rename This Estate");
//			Clickable estateNameButton = new Clickable(renameIcon) {
//
//				@Override
//				public void clicked(Player p) {
//					
//					new BukkitRunnable() {
//						@Override
//						public void run() {
//							ClickableInventory.forceCloseInventory(p);
//							new Dialog(player, Nobility.getNobility(), "Enter in a new name:") {							
//								@Override
//								public List<String> onTabComplete(String wordCompleted, String[] fullMessage) {
//									return null;
//								}
//								
//								@Override
//								public void onReply(String[] message) {
//									// Set messages to one word
//									String newName = "";
//									for (String str : message) {newName = newName + str + " ";}
//									
//									estate.getGroup().setName(newName);
//									
//									player.sendMessage("This Estate is now called " + newName);
//									this.end();
//								}
//							};
//							
//						}
//					}.runTaskLater(Nobility.getNobility(), 1);
//					
//				}			
//			};
//			estateGUI.addSlot(estateNameButton);
	
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
