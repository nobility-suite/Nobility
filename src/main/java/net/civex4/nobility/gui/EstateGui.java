package net.civex4.nobility.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.kingvictoria.regions.Region;
import io.github.kingvictoria.regions.nodes.Node;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobility.development.AttributeManager;
import net.civex4.nobility.development.Camp;
import net.civex4.nobility.development.DevAttribute;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentBlueprint;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.developments.AbstractWorkshop;
import net.civex4.nobility.developments.Armory;
import net.civex4.nobility.developments.Inn;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.estate.Relationship;
import net.civex4.nobility.group.Group;
import net.civex4.nobility.group.GroupPermission;
import net.civex4.nobility.research.Card;
import net.civex4.nobility.research.CardManager;
import net.civex4.nobility.research.UnfinishedBlueprint;
import net.civex4.nobilityitems.NobilityItem;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;

public class EstateGui {

	private static final int rowLength = 9;

	public void openEstateGUI(Player player) {



		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);

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
		Clickable infoIcon = ButtonLibrary.createEstateInfo(estate);
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
				openCampSelectorGUI(p);
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

		// VIEW WORKSHOPS
		ItemStack stockIcon = ButtonLibrary.createIcon(Material.CHEST, "Workshops & Stockpiles");
		Clickable stockButton = new Clickable(stockIcon) {

			@Override
			public void clicked(Player p) {
				openWorkshopsGUI(p);
			}

		};
		estateGUI.addSlot(stockButton);


		// DEFENCE
		ItemStack defIcon = ButtonLibrary.createIcon(Material.STONE_BRICKS, "Siege");



		ItemAPI.addLore(defIcon, ChatColor.BLUE + DevAttribute.CANNON_LIMIT.name + ": " + ChatColor.WHITE + AttributeManager.getCannonLimit(estate),
				ChatColor.BLUE + DevAttribute.CANNON_STORED.name + ": " + ChatColor.WHITE + AttributeManager.getCannons(estate),
				ChatColor.BLUE + DevAttribute.CANNON_DISREPAIRED.name +": " + ChatColor.WHITE + AttributeManager.getDisrepairedCannons(estate));
		Clickable defButton = new Clickable(defIcon) {

			@Override
			public void clicked(Player p) {
				openCannonGUI(p);
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
	
	public void openBlueprintResearchGUI(Player p) {
		ItemStack i = p.getItemInHand();
		if(i.hasItemMeta()) {
			String name = ItemAPI.getDisplayName(i);
			if(name.startsWith(UnfinishedBlueprint.UNFINISHED_BLUEPRINT_PREFIX)) {
				if(i.getType() == Material.PAPER) {
					
					UnfinishedBlueprint ubp = UnfinishedBlueprint.parseFromItem(i);
					if(ubp != null && ubp.getBaseBlueprint() != null) {
						//If the player is holding a blueprint, we can open the gui.
						Estate estate = Nobility.getEstateManager().getEstateOfPlayer(p);
						ClickableInventory gui = new ClickableInventory(54, "Blueprint Research");

						int[] decoSlots = {0,1,2,3,4,5,6,7,8,9,10,12,13,15,16,17,18,26,27,35,36,44,45,46,47,48,50,51,52,53};

						// DECORATION STACKS
						for (int j : decoSlots) {
							if (!(gui.getSlot(j) instanceof Clickable)) {
								Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
								gui.setSlot(c, j);
							}
						}
						
						gui.setSlot(ButtonLibrary.HOME.clickable(),49);
						gui.setSlot(ButtonLibrary.createResearchTutorial(), 10);
						
						ItemStack clone = i.clone();
						Clickable c = new DecorationStack(clone);
						gui.setSlot(c, 13);
						
						int roundsRemaining = ubp.getMaxRounds() - ubp.getRounds();
						if(roundsRemaining > 0) {
							ItemStack rounds = new ItemStack(Material.REDSTONE,roundsRemaining);
							ItemAPI.setDisplayName(rounds, ChatColor.BLUE + "Rounds Remaining: " + ChatColor.WHITE + roundsRemaining);
							Clickable ci = new DecorationStack(rounds);
							gui.setSlot(ci, 16);
						}
						
						//29,31,33 for cards.
						int[] decoSlots2 = {19,20,21,22,23,24,25,28,30,32,34,37,38,39,40,41,42,43};

						// DECORATION STACKS
						for (int j : decoSlots2) {
							if (!(gui.getSlot(j) instanceof Clickable)) {
								Clickable cl = new DecorationStack(ButtonLibrary.createIcon(Material.IRON_BARS, " "));
								gui.setSlot(cl, j);
							}
						}
						
						//TODO card selection area
						ArrayList<Card> cards = new ArrayList<Card>();
						
						for(int j = 0; j < 3; j++) {
							Nobility.getCardManager().generateCard(ubp, p, null, new Random(ubp.getSeed()));
						}
						
						
						for(Card card : cards) {
							Clickable cl = CardManager.getCardIcon(card, ubp, p);
						}
						
						gui.showInventory(p);
						return;
					}else {
						p.sendMessage(ChatColor.RED + "The item in your hand is not a recognized blueprint, or, it's Blueprint recipe has been removed from the config.");
						return;
					}

				}
			}
		}
		
		p.closeInventory();
		p.sendMessage(ChatColor.RED + "You must be holding an Unfinished Blueprint in order to begin Researching.");
		p.sendMessage(ChatColor.RED + "You can craft an Unfinished Blueprint from the Workshops menu (top buttons).");

	}

	private void openWorkshopsGUI(Player p) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(p);
		ClickableInventory gui = new ClickableInventory(54, "Workshops and Research");

		int[] decoSlots = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,45,46,47,48,50,51,52,53};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		ItemStack info = ButtonLibrary.createIcon(Material.BOOK,ChatColor.WHITE + "Research Info");
		ItemAPI.addLore(info, ChatColor.WHITE + "Research" + ChatColor.GRAY + " allows you to spend materials to create",
				ChatColor.GRAY + "Blueprints, which you can use in Workshops.",
				ChatColor.GRAY + "Select a category to get started.");
		Clickable info2 = new DecorationStack(info);
		gui.setSlot(info2,1);



		ItemStack weapons = ButtonLibrary.createIcon(Material.DIAMOND_SWORD, "Weapons");
		Clickable wepbutton = new Clickable(weapons) {

			@Override
			public void clicked(Player p) {

			}

		};
		gui.setSlot(wepbutton,2);

		ItemStack armors = ButtonLibrary.createIcon(Material.DIAMOND_CHESTPLATE, "Armor");
		Clickable armorsbutton = new Clickable(armors) {

			@Override
			public void clicked(Player p) {

			}

		};
		gui.setSlot(armorsbutton,3);

		ItemStack tools = ButtonLibrary.createIcon(Material.DIAMOND_PICKAXE, "Tools");
		Clickable toolsbutton = new Clickable(tools) {

			@Override
			public void clicked(Player p) {

			}

		};
		gui.setSlot(toolsbutton,4);

		ItemStack comp = ButtonLibrary.createIcon(Material.ANVIL, "Components");
		Clickable compbutton = new Clickable(comp) {

			@Override
			public void clicked(Player p) {

			}

		};
		gui.setSlot(compbutton,5);

		ItemStack icon5 = ButtonLibrary.createIcon(Material.TNT, "Siege");
		Clickable button5 = new Clickable(icon5) {

			@Override
			public void clicked(Player p) {

			}

		};
		gui.setSlot(button5,6);

		ItemStack icon6 = ButtonLibrary.createIcon(Material.OBSIDIAN, "Block Protection");
		Clickable button6 = new Clickable(icon6) {

			@Override
			public void clicked(Player p) {

			}

		};
		gui.setSlot(button6,7);

		for(Development d : estate.getBuiltDevelopments()) {
			if(d.getType() == DevelopmentType.WORKSHOP) {
				ItemStack icon = ButtonLibrary.createIcon(d.icon, d.name);
				ItemAPI.addLore(icon, ChatColor.BLUE + "Type: " + ChatColor.WHITE + d.getType().toString(),
						ChatColor.BLUE + "Description: ",
						ChatColor.GRAY + d.useDescription,
						ChatColor.YELLOW + "",
						ChatColor.YELLOW + "Click to use this Workshop!");
				Clickable dicon = new Clickable(icon) {

					@Override
					public void clicked(Player p) {
						openWorkshopCraftGUI(p,(AbstractWorkshop) d);
					}

				};
				gui.addSlot(dicon);
			}
		}

		gui.setSlot(ButtonLibrary.HOME.clickable(),49);

		gui.showInventory(p);
	}

	private void openBlueprintSelector(Player p, AbstractWorkshop d) {
		if(d.inputChest == null) {
			p.sendMessage(ChatColor.DARK_RED + "No input chest");
		return;}

		ClickableInventory gui = new ClickableInventory(54, "Select a Blueprint");

		int[] decoSlots = {0,1,2,3,4,5,6,7,8};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		Block b = d.inputChest.getBlock();
		if(b!= null && b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
			Chest chest = (Chest) b.getState();
			Inventory inv = chest.getInventory();
			ArrayList<ItemStack> blueprints = Nobility.getBlueprintManager().listBlueprints(inv);

			//Display Blueprints
			for(ItemStack i : blueprints) {
				Clickable bp = new Clickable(i) {
						@Override
						public void clicked(Player p) {
							d.selectedRecipe = i;
							openWorkshopCraftGUI(p,d);
						}};
				gui.addSlot(bp);
			}
			gui.showInventory(p);


		}else {
			p.sendMessage(ChatColor.RED + "Workshop input chest location is invalid.");
		}

	}

	private void openDevelopmentsUpgradeGUI(Player p) {
		// TODO Auto-generated method stub

		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(p);
		// TODO Estate name length can't be longer than 32
		ClickableInventory gui = new ClickableInventory(54, "View Development Upgrades");

		//HashMap<String, DevelopmentBlueprint> blueprints = Nobility.getDevelopmentManager().getUpgrades();
		List<Development> built = estate.getBuiltDevelopments();

		int[] decoSlots = {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 45, 46, 47, 48, 50, 51, 52, 53};


		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		Clickable infoIcon = ButtonLibrary.createEstateInfo(estate);

		gui.addSlot(infoIcon);

		gui.setSlot(ButtonLibrary.HOME.clickable(), 49);


		// VIEW DEVELOPMENT UPGRADES
		ItemStack devUpgradeIcon = ButtonLibrary.createIcon(Material.FURNACE, "View Development Upgrades");
		Clickable devUpgradeButton = new Clickable(devUpgradeIcon) {
			@Override
			public void clicked(Player p) {
				openDevelopmentsUpgradeGUI(p);
			}
		};
		gui.addSlot(devUpgradeButton);

		for (DevelopmentBlueprint d : Nobility.getDevelopmentManager().getUpgrades().values()) {
			boolean hasPrereq = true;
			for (Development prereq : d.prereqs) {
				if (built.contains(prereq)) {
					hasPrereq = false;
					break;
				}
			}
			if (!hasPrereq)
				continue;

			ItemStack icon = ButtonLibrary.createIcon(d.result.icon, d.result.name);
			if (d.result.getType() == DevelopmentType.CAMP) {
				Camp camp = (Camp) d.result;
				if (camp != null) {
					ItemAPI.addLore(icon, ChatColor.BLUE + "Node Limit: " + ChatColor.WHITE + camp.getNodeLimit());
				}
			}
			ItemAPI.addLore(icon, ChatColor.BLUE + "Type: " + ChatColor.WHITE + d.result.getType().toString(),
					ChatColor.BLUE + "Description: ",
					ChatColor.GRAY + d.result.useDescription);
			if (d.result.attributes != null)
				for (DevAttribute attr : d.result.attributes.keySet()) {
					ItemAPI.addLore(icon, AttributeManager.getAttributeText(attr, d.result.attributes.get(attr)));
				}
			//Clickable upgradeIcon = new DecorationStack(icon);
			Clickable upgradeIcon = new Clickable(icon) {
				@Override
				public void clicked(Player player) {
					estate.removeDevelopment(d.base);
					estate.addDevelopment(d.result);
				}
			};
			gui.addSlot(upgradeIcon);
		}
		gui.showInventory(p);
	}
	
	private void openWorkshopCraftGUI(Player p, AbstractWorkshop d) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(p);
		ClickableInventory gui = new ClickableInventory(54, d.name);

		int[] decoSlots = {1,2,3,4,5,6,7,8,9,10,12,17,19,20,21,26,30,35,39,44,28,37,45,46,47,48,50,51,52,53};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		Clickable workshopInfo = ButtonLibrary.createWorkshopInfo(d);
		gui.setSlot(workshopInfo, 0);

		Clickable workerInfo = ButtonLibrary.createWorkerInfo(p);
		gui.setSlot(workerInfo, 29);

		ItemStack selected = null;
		if(d.selectedRecipe != null)
		selected = d.selectedRecipe.clone();
		if(selected != null  && Nobility.getBlueprintManager().recipeExists(selected, d)) {

		}else { selected = ButtonLibrary.createIcon(Material.BARRIER, ChatColor.WHITE + "Select Blueprint"); }
		ItemAPI.addLore(selected, true, ChatColor.YELLOW + "" + ChatColor.BOLD + "Click to change Blueprint!");
		Clickable rec = new Clickable(selected) {
			@Override
			public void clicked(Player p) {
				openBlueprintSelector(p,d);
			}};
		gui.setSlot(rec, 11);

		ItemStack fuel = ButtonLibrary.createIcon(Material.BARRIER, ChatColor.WHITE + "Fuel");
		Clickable fue = new DecorationStack(fuel);
		gui.setSlot(fue, 38);

		ItemStack startRecipe = ButtonLibrary.createIcon(Material.PAPER, ChatColor.GREEN + "Start Recipe");
		Clickable start = new Clickable(startRecipe) {
				@Override
				public void clicked(Player p) {

				}};
		gui.setSlot(start, 18);

		ItemStack assignOutput = ButtonLibrary.createIcon(Material.CHEST, ChatColor.GREEN + "Output Chest");
		if(d.outputChest != null) { ItemAPI.addLore(assignOutput, ChatColor.BLUE + "Output Chest: " + ChatColor.WHITE + "[" + d.outputChest.getBlockX() + "x," + d.outputChest.getBlockY() + "y," + d.outputChest.getBlockZ() + "z]", "", ChatColor.YELLOW + "Click to reassign an output chest!"); }
		else {ItemAPI.addLore(assignOutput, ChatColor.YELLOW + "" + ChatColor.BOLD + "Click to assign an output chest!"); }
		Clickable output = new Clickable(assignOutput) {
				@Override
				public void clicked(Player p) {
					p.closeInventory();
					Nobility.getChestSelector().outputQueue.put(p.getUniqueId(),d);
					p.sendMessage(ChatColor.BLUE + "Punch a chest to select it as an output chest.");

					Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
					    @Override
					    public void run() {
					    	if(Nobility.getChestSelector().outputQueue.containsKey(p.getUniqueId())) {
						    	Nobility.getChestSelector().outputQueue.remove(p.getUniqueId());
								p.sendMessage(ChatColor.RED + "Chest selection cancelled.");
					    	}
					    }
					}, 20*10L); //20 Tick (1 Second) delay before run() is called
				}};
		gui.setSlot(output, 27);

		ItemStack assignInput = ButtonLibrary.createIcon(Material.HOPPER, ChatColor.WHITE + "Input Chest");
		if(d.inputChest != null) { ItemAPI.addLore(assignInput, ChatColor.BLUE + "Input Chest: " + ChatColor.WHITE + "[" + d.inputChest.getBlockX() + "x," + d.inputChest.getBlockY() + "y," + d.inputChest.getBlockZ() + "z]", "", ChatColor.YELLOW + "Click to reassign an output chest!"); }
		else {ItemAPI.addLore(assignInput, ChatColor.YELLOW + "" + ChatColor.BOLD + "Click to assign an input chest!"); }
		Clickable input = new Clickable(assignInput) {
				@Override
				public void clicked(Player p) {
					p.closeInventory();

					Nobility.getChestSelector().inputQueue.put(p.getUniqueId(),d);
					p.sendMessage(ChatColor.BLUE + "Punch a chest to select it as an input chest.");

					Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
					    @Override
					    public void run() {
					    	if(Nobility.getChestSelector().inputQueue.containsKey(p.getUniqueId())) {
						    	Nobility.getChestSelector().inputQueue.remove(p.getUniqueId());
								p.sendMessage(ChatColor.RED + "Chest selection cancelled.");
					    	}
					    }
					}, 20*10L); //20 Tick (1 Second) delay before run() is called
				}};
		gui.setSlot(input, 36);

		gui.setSlot(ButtonLibrary.HOME.clickable(),49);
		
		if(selected != null && selected.getType() != Material.BARRIER &&Nobility.getBlueprintManager().recipeExists(d.selectedRecipe, d)) {
			Blueprint bp = Blueprint.parseBlueprintFromItem(d.selectedRecipe);
			if(bp == null) { 
				p.sendMessage(ChatColor.RED + "Error parsing blueprint."); 
			}else {
				for(NobilityItem it : bp.ingredients.keySet()) {
					ItemStack i = it.getItemStack(1);
					i.setAmount(Math.min(bp.ingredients.get(it), 64));
					
					if(!d.outputContains(it, bp.ingredients.get(it))) {
						i.setType(Material.RED_STAINED_GLASS_PANE);
					}
					
					Clickable icon = new DecorationStack(i);
					gui.addSlot(icon);
				}
			}
		}


		gui.showInventory(p);
	}

	private void openMembersGUI(Player p) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(p);
		ClickableInventory gui = new ClickableInventory(54, "View Citizens");
		int[] decoSlots = {0,1,2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,45,46,47,48,50,51,52,53};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		Clickable infoIcon = ButtonLibrary.createEstateInfo(estate);
		gui.addSlot(infoIcon);

		gui.setSlot(ButtonLibrary.HOME.clickable(),49);

		Set<UUID> members = estate.getGroup().getMembers();

		for(UUID u : members) {
			Player pl = Bukkit.getPlayer(u);
			ItemStack playerIcon = ButtonLibrary.createIcon(Material.PLAYER_HEAD, pl.getName());
			ItemAPI.addLore(playerIcon, ChatColor.BLUE + "Rank: " + ChatColor.WHITE + estate.getGroup().getPermission(pl));
			ItemAPI.addLore(playerIcon, ChatColor.BLUE + "Workers: " + ChatColor.WHITE + Nobility.getWorkerManager().getWorkers(pl),
					ChatColor.BLUE + "Activity Level: " + ChatColor.WHITE + "" + Nobility.getWorkerManager().getActivityLevel(pl));
			SkullMeta im = (SkullMeta) ItemAPI.getItemMeta(playerIcon);
			im.setOwningPlayer(Bukkit.getOfflinePlayer(u));
			playerIcon.setItemMeta(im);
			Clickable pcon = new DecorationStack(playerIcon);
			gui.addSlot(pcon);
		}

		gui.showInventory(p);
	}

	private void openDevelopmentsGUI(Player p) {
		// TODO Auto-generated method stub

		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(p);
		// TODO Estate name length can't be longer than 32
		ClickableInventory gui = new ClickableInventory(54, "View Developments");

		HashMap<String, DevelopmentBlueprint> blueprints = Nobility.getDevelopmentManager().getBlueprints();
		List<Development> built = estate.getBuiltDevelopments();

		int[] decoSlots = {0,1,2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,45,46,47,48,50,51,52,53};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		Clickable infoIcon = ButtonLibrary.createEstateInfo(estate);

		gui.addSlot(infoIcon);

		gui.setSlot(ButtonLibrary.HOME.clickable(),49);

		for(Development d : built) {
			ItemStack icon = ButtonLibrary.createIcon(d.icon, d.name);
			if(d.getType() == DevelopmentType.CAMP) {
				Camp camp = (Camp) d;
				if(camp != null) { ItemAPI.addLore(icon, ChatColor.BLUE + "Node Limit: " + ChatColor.WHITE + camp.getNodeLimit()); }
			}
			ItemAPI.addLore(icon, ChatColor.BLUE + "Type: " + ChatColor.WHITE + d.getType().toString(),
					ChatColor.BLUE + "Description: ",
					ChatColor.GRAY + d.useDescription);
			if(d.attributes != null)
				for(DevAttribute attr : d.attributes.keySet()) {
					ItemAPI.addLore(icon, AttributeManager.getAttributeText(attr,d.attributes.get(attr)));
				}
			if(d.getType() == DevelopmentType.ARMORY) {
				Armory armory = (Armory) d;
				if(!(armory.upgradeItem == null)) {
					ItemAPI.addLore(icon, ChatColor.BLUE + "Upgrade Cost: " + armory.upgradeItem.getDisplayName());
					ItemAPI.addLore(icon, ChatColor.GREEN + "RIGHT CLICK to upgrade Armory");
				}
			}
			if(d.getType() == DevelopmentType.INN) {
				Inn inn = (Inn) d;
				if(inn.defaultSpawn != null) {
					ItemAPI.addLore(icon, ChatColor.BLUE + "Spawn: " + ChatColor.WHITE + inn.defaultSpawn.getBlockX() + " " + inn.defaultSpawn.getBlockY() + " " + inn.defaultSpawn.getBlockZ());
				}
				ItemAPI.addLore(icon, ChatColor.GREEN + "LEFT CLICK to set spawn");
			}
			Clickable dicon = new Clickable(icon) {
				@Override
				protected void clicked(Player player) {
					if(d.name == "Town Inn") {
						Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
						GroupPermission perm = estate.getGroup().getPermission(player);
						if(perm == GroupPermission.LEADER || perm == GroupPermission.OFFICER) {
							Nobility.getChestSelector().defaultSpawnQueue.put(player.getUniqueId(), (Inn) d);

							player.sendMessage(ChatColor.YELLOW + "Punch a bed within 10 seconds to set the default Estate spawn.");
							Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
								@Override
								public void run() {
									if(Nobility.getChestSelector().defaultSpawnQueue.containsKey(p.getUniqueId())) {
										Nobility.getChestSelector().defaultSpawnQueue.remove(p.getUniqueId());
										p.sendMessage(ChatColor.RED + "Spawn selection cancelled.");
									}
								}
							}, 20*10L); //20 Tick (1 Second) delay before run() is called
						}
					}
					if(d.getType() == DevelopmentType.ARMORY) {
						Armory armory = (Armory) d;
						Integer level = d.attributes.get(DevAttribute.ARMORY_LEVEL);
						if(level == 3) { return; }
						PlayerInventory inventory = player.getInventory();

						ItemStack upgradeItem = armory.upgradeItem.getItemStack(1);

						if(inventory.containsAtLeast(upgradeItem, upgradeItem.getAmount())) {
							inventory.remove(upgradeItem);
							player.sendMessage("Successfully upgraded Armory.");
							int newlevel = level + 1;
							armory.attributes.replace(DevAttribute.ARMORY_LEVEL, newlevel);
						} else {
							player.sendMessage(ChatColor.RED + "You don't have enough materials to upgrade this.");
						}
					}
				}
			};
			gui.addSlot(dicon);
		}

		gui.showInventory(p);

	}


	protected void openRegionInfoGUI(Player player) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
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
			Clickable infoIcon = ButtonLibrary.createEstateInfo(e);

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

		List<Node> nodes = region.getNodes();

		for(Node n : nodes) {
			Estate owner = Nobility.getClaimManager().claims.get(n);

			String ownerName;
			if(owner == null) { ownerName = ChatColor.GRAY + "None";
			}else if(owner == estate) { ownerName = ChatColor.GREEN + estate.getGroup().getName();
			}else ownerName = ChatColor.RED + owner.getGroup().getName();

			String name = ChatColor.YELLOW + n.getName() + ChatColor.WHITE + " (" + ownerName + ChatColor.WHITE + ")";
			Map<NobilityItem, Integer> output = n.getOutput();
			ItemStack resourceIcon = ButtonLibrary.createIcon(Material.STONE, name);
			Clickable resourceButton = new DecorationStack(resourceIcon);
			ItemAPI.addLore(resourceIcon,
					ChatColor.BLUE + "Slots: (" + n.getUsedSlots() +"/" + n.getSlots() + ")",
					ChatColor.BLUE + "Type: " + ChatColor.WHITE + n.getType(),
					ChatColor.BLUE + "Output:");

			if(output != null && output.size() > 0) {
				for(NobilityItem i : output.keySet()) {

					String iname = i.getDisplayName();
					int amount = output.get(i);

					ItemAPI.addLore(resourceIcon, ChatColor.GRAY + "  " + amount + "x " + ChatColor.WHITE + iname );
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

	private void openCampSelectorGUI(Player player) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
		Region region = estate.getRegion();
		ClickableInventory gui = new ClickableInventory(9, "Select a Camp");

		gui.setSlot(ButtonLibrary.HOME.clickable(), 7);

		int[] decoSlots = {0,6,8};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		for(Camp c : estate.getCamps()) {
			ItemStack info = ButtonLibrary.createIcon(c.icon, c.name);
			ItemAPI.addLore(info, ChatColor.BLUE + "Node Limit: " + ChatColor.WHITE + c.getNodeLimit());
			Clickable click = new Clickable(info) {

				@Override
				public void clicked(Player p) {
					openCampGUI(p,c);
				}

			};
			gui.addSlot(click);
		}
		gui.showInventory(player);
	}

	private void openCampGUI(Player player, Camp camp) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
		Region region = estate.getRegion();
		ClickableInventory gui = new ClickableInventory(54, "Assign Workers");

		int workers = Nobility.getWorkerManager().getWorkers(player);

		int[] decoSlots = {0,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,26,27,35,36,44,45,46,47,48,50,51,52,53};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		gui.setSlot(ButtonLibrary.HOME.clickable(), 49);

		Clickable pcon = ButtonLibrary.createWorkerInfo(player);
		gui.addSlot(pcon);


		ArrayList<Node> nodes = estate.getNodes();

		for(Node n : nodes) {
			//Populate worker list with nodes
			if(n.getType() == camp.nodeType) {
				String name = ChatColor.YELLOW + n.getName() + ChatColor.WHITE + " (" + ChatColor.GREEN + estate.getGroup().getName() + ChatColor.WHITE + ")";
				Map<NobilityItem, Integer> output = n.getOutput();
				ItemStack resourceIcon = ButtonLibrary.createIcon(Material.STONE, name);
				Clickable resourceButton = new DecorationStack(resourceIcon);
				ItemAPI.addLore(resourceIcon, ChatColor.BLUE + "Slots: (" + n.getUsedSlots() + "/" + n.getSlots() + ")",
						ChatColor.BLUE + "Type: " + ChatColor.WHITE + n.getType(),
						ChatColor.BLUE + "Output:",
						ChatColor.GREEN + "LEFT CLICK to add",
				ChatColor.RED + "RIGHT CLICK to remove",
				ChatColor.YELLOW + "SHIFT CLICK to edit node");
				//Node output lore
				if(output != null && output.size() > 0) {
					for(NobilityItem i : output.keySet()) {

						String iname = i.getDisplayName();
						int amount = output.get(i);

						ItemAPI.addLore(resourceIcon, ChatColor.GRAY + "  " + amount + "x " + ChatColor.WHITE + iname );
					}
				}

				Clickable workerNode = new Clickable(resourceIcon) {

					@Override
					public void clicked(Player player) {
					}

					@Override
					public void onLeftClick(Player p) {

						if(Nobility.getWorkerManager().getWorkers(p) > 0)
							if(n.addWorker(p)) {
								Nobility.getWorkerManager().spendWorker(p);
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME,1,(float) (1 + 0.1*n.getUsedSlots()));
								openCampGUI(p,camp);
							}else {
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE,1,1);
							}
					}

					@Override
					public void onShiftLeftClick(Player p) {
						openNodeGUI(n, p, camp);
					}

					@Override
					public void onRightClick(Player p) {

						try {
							if(n.getUsedSlots() == 0) {
								openCampGUI(player, camp);
								return;
							}
							Nobility.getWorkerManager().removeWorker(n, player);
							player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, (float) (n.getUsedSlots()-1));
							openCampGUI(player, camp);
						} catch (Exception e) {
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
							player.sendMessage(ChatColor.DARK_RED + "A serious error has occurred.");
							player.closeInventory();
							e.printStackTrace();
						}

					}
				};
				gui.addSlot(workerNode);

			}
		}
		gui.showInventory(player);
	}

	private void openCannonGUI(Player player) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
		Region region = estate.getRegion();
		int cannons = AttributeManager.getCannons(estate);
		ClickableInventory gui = new ClickableInventory(9, "Cannon Selection");

		gui.setSlot(ButtonLibrary.HOME.clickable(), 7);

		int[] decoSlots = {0, 6, 8};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}
		for (Development d : estate.getBuiltDevelopments()) {

			if (d.isActive) {
				if (d.attributes != null) {
					if (d.attributes.containsKey(DevAttribute.CANNON_STORED)) {
						Integer stored = d.attributes.get(DevAttribute.CANNON_STORED);
						for (int i = 0; i < stored; i++) {
							ItemStack info = ButtonLibrary.createIcon(Material.STONE_BRICKS, "Cannon");
							ItemAPI.addLore(info, ChatColor.BLUE + "Cannon is fully repaired.");
							Clickable click = new Clickable(info) {

								@Override
								public void clicked(Player p) {
								}
							};
							gui.addSlot(click);

						}
					}
					if (d.attributes.containsKey(DevAttribute.CANNON_DISREPAIRED)) {
						Integer disrepaired = d.attributes.get(DevAttribute.CANNON_DISREPAIRED);
						for (int i = 0; i < disrepaired; i++) {
							ItemStack dis = ButtonLibrary.createIcon(Material.CRACKED_STONE_BRICKS, "Broken Cannon");
							ItemAPI.addLore(dis, ChatColor.RED + "Cannon is in disrepair.");
							ItemAPI.addLore(dis, ChatColor.YELLOW + "COST: 128 IRON");
							ItemAPI.addLore(dis, ChatColor.GREEN + "CLICK to repair");
							Clickable click = new Clickable(dis) {
								@Override
								public void clicked(Player player) {
									Inventory inv = player.getInventory();
									ItemStack cost = new ItemStack(Material.IRON_INGOT);
									cost.setAmount(128);
									if (inv.containsAtLeast(cost, 128)) {
										if(disrepaired == 0) {
											player.closeInventory();
											player.sendMessage(ChatColor.RED + "You have no cannons in disrepair.");
											return;
										}
										inv.removeItem(cost);
										int disrepaired = d.attributes.get(DevAttribute.CANNON_DISREPAIRED);
										int newdisrepaired = disrepaired - 1;
										d.attributes.put(DevAttribute.CANNON_DISREPAIRED, newdisrepaired);
										int stored = d.attributes.get(DevAttribute.CANNON_STORED);
										int newstored = stored + 1;
										d.attributes.put(DevAttribute.CANNON_STORED, newstored);
										openCannonGUI(player);
									} else {
										player.sendMessage(ChatColor.RED + "You don't have enough materials to repair this!");
										player.closeInventory();
									}
								}
							};
							gui.addSlot(click);

						}
					}
					gui.showInventory(player);
				}
			}
		}
	}

	private void openNodeGUI(Node node, Player player, Camp camp) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
		Region region = estate.getRegion();

		List<UUID> workers = node.getWorkers();

		ClickableInventory gui = new ClickableInventory(54, "Node Workers");

		gui.setSlot(ButtonLibrary.HOME.clickable(), 49);

		int[] decoSlots = {0,2,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,26,27,35,36,44,45,46,47,48,50,51,52,53};

		// DECORATION STACKS
		for (int i : decoSlots) {
			if (!(gui.getSlot(i) instanceof Clickable)) {
				Clickable c = new DecorationStack(ButtonLibrary.createIcon(Material.BLACK_STAINED_GLASS_PANE, " "));
				gui.setSlot(c, i);
			}
		}

		ItemStack nodeIcon = ButtonLibrary.createIcon(node.getType().icon, node.getName());
		ItemAPI.addLore(nodeIcon, ChatColor.BLUE + "Region: " + region.getName());
		ItemAPI.addLore(nodeIcon, ChatColor.BLUE + "Workers: " + node.getUsedSlots());
		Clickable info = new DecorationStack(nodeIcon);
		gui.setSlot(info, 1);

		ItemStack assignOutput = ButtonLibrary.createIcon(Material.CHEST, ChatColor.GREEN + "Output Chest");
		if(camp.outputChest != null) { ItemAPI.addLore(assignOutput, ChatColor.BLUE + "Output Chest: " + ChatColor.WHITE + "[" + camp.outputChest.getBlockX() + "x," + camp.outputChest.getBlockY() + "y," + camp.outputChest.getBlockZ() + "z]", "", ChatColor.YELLOW + "Click to reassign an output chest!"); }
		else {ItemAPI.addLore(assignOutput, ChatColor.YELLOW + "" + ChatColor.BOLD + "Click to assign an output chest!"); }
		Clickable output = new Clickable(assignOutput) {
			@Override
			public void clicked(Player p) {
				Group g = Nobility.getGroupManager().getGroup(p);
				GroupPermission perm = g.getPermission(player);

				if(!(perm == GroupPermission.OFFICER || perm == GroupPermission.LEADER)) {
					player.sendMessage(ChatColor.RED + "You don't have permission to re-assign output chests!");
					p.closeInventory();
					return;
				}

				p.closeInventory();
				Nobility.getChestSelector().outputQueueCamp.put(p.getUniqueId(), camp);
				p.sendMessage(ChatColor.BLUE + "Punch a chest to select it as an output chest.");

				Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
					@Override
					public void run() {
						if(Nobility.getChestSelector().outputQueueCamp.containsKey(p.getUniqueId())) {
							Nobility.getChestSelector().outputQueueCamp.remove(p.getUniqueId());
							p.sendMessage(ChatColor.RED + "Chest selection cancelled.");
						}
					}
				}, 20*10L); //20 Tick (1 Second) delay before run() is called
			}};
		gui.setSlot(output, 3);

		for (UUID u : workers) {

			if(workers.size() == 0) {
				player.sendMessage(ChatColor.RED + "There are no workers on that node!");
				player.closeInventory();
				return;
			}

			OfflinePlayer pl = Bukkit.getOfflinePlayer(u);
			ItemStack playerIcon = ButtonLibrary.createIcon(Material.PLAYER_HEAD, pl.getName());
			ItemAPI.addLore(playerIcon, ChatColor.BLUE + "Username: " + pl.getName());
			ItemAPI.addLore(playerIcon, ChatColor.RED + "RIGHT CLICK to remove this worker");
			SkullMeta im = (SkullMeta) ItemAPI.getItemMeta(playerIcon);
			im.setOwningPlayer(Bukkit.getOfflinePlayer(u));
			playerIcon.setItemMeta(im);
			Clickable pcon = new Clickable(playerIcon) {
				@Override
				public void clicked(Player player) {

				}

				@Override
				public void onRightClick(Player player) {
					Group g = Nobility.getGroupManager().getGroup(player);
					GroupPermission perm = g.getPermission(player);

					if(!(perm == GroupPermission.OFFICER || perm == GroupPermission.LEADER)) {
						player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
						player.closeInventory();
						return;
					}
					try {
						if(node.getUsedSlots() == 0) {
							openNodeGUI(node, player, camp);
							return;
						}

						Nobility.getWorkerManager().removeWorker(node, pl);
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, (float) (node.getUsedSlots()-1));
						openNodeGUI(node, player, camp);
					} catch (Exception e) {
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
						player.sendMessage(ChatColor.DARK_RED + "A serious error has occurred.");
						player.closeInventory();
						e.printStackTrace();
						}

					}
			};
			gui.addSlot(pcon);
		}
		gui.showInventory(player);

	}

	private void openClaimGUI(Player player) {
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
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
					ChatColor.BLUE + "Leader: " + ChatColor.WHITE + "" + e.getGroup().getLocalization(GroupPermission.LEADER) + " " + estate.getGroup().getLeader().getName(),
					ChatColor.BLUE + "Region: " + ChatColor.WHITE + e.getRegion().getName(),
					ChatColor.BLUE + "Location: " + ChatColor.WHITE + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z",
					ChatColor.BLUE + "Vulnerability Hour: " + ChatColor.WHITE + e.getVulnerabilityHour(),
					"");

			if(e == estate)
				for(Camp c : e.getCamps()) {
					ItemAPI.addLore(info, ChatColor.BLUE + "Node Limit (" + c.nodeType + ") " + ChatColor.WHITE + c.getNodeLimit());
				}
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

		List<Node> nodes = region.getNodes();

		for(Node n : nodes) {
			Estate owner = Nobility.getClaimManager().claims.get(n);

			String ownerName;
			if(owner == null) { ownerName = ChatColor.GRAY + "None";
			}else if(owner == estate) { ownerName = ChatColor.GREEN + estate.getGroup().getName();
			}else ownerName = ChatColor.RED + owner.getGroup().getName();

			String name = ChatColor.YELLOW + n.getName() + ChatColor.WHITE + " (" + ownerName + ChatColor.WHITE + ")";
			Map<NobilityItem, Integer> output = n.getOutput();
			ItemStack resourceIcon = ButtonLibrary.createIcon(Material.STONE, name);
			Clickable resourceButton = new DecorationStack(resourceIcon);
			ItemAPI.addLore(resourceIcon, ChatColor.BLUE + "Slots: (" + n.getUsedSlots() + "/" + n.getSlots() + ")",
					ChatColor.BLUE + "Type: " + ChatColor.WHITE + n.getType(),
					ChatColor.BLUE + "Output:");

			if(output != null && output.size() > 0) {
				for(NobilityItem i : output.keySet()) {

					String iname = i.getDisplayName();
					int amount = output.get(i);

					ItemAPI.addLore(resourceIcon, ChatColor.GRAY + "  " + amount + "x " + ChatColor.WHITE + iname );
				}
			}

			if(owner == null) {
				ItemAPI.addLore(resourceIcon, " ",
						ChatColor.YELLOW + "" + ChatColor.BOLD + "Left click to claim!");
				Clickable claimButton = new Clickable(resourceIcon) {

					@Override
					public void clicked(Player p) {
						Group g = Nobility.getGroupManager().getGroup(p);
						GroupPermission perm = g.getPermission(p);

						if(!(perm == GroupPermission.OFFICER || perm == GroupPermission.LEADER)) {
							p.sendMessage(ChatColor.RED + "You don't have permission to claim nodes!");
							p.closeInventory();
							return;
						}

						if(!Nobility.getClaimManager().underNodeLimit(n, estate)) {
							p.sendMessage(ChatColor.RED + "You cannot claim any more nodes of type " + ChatColor.WHITE + n.getType() + ChatColor.RED + ", you must upgrade your camps first.");
							p.closeInventory();
							return;
						}
						p.sendMessage(ChatColor.GREEN + "Claimed " + ChatColor.WHITE + n.getName() + " for " + ChatColor.WHITE + estate.getGroup().getName());
						p.closeInventory();
						Nobility.getClaimManager().claim(n, estate);
						Camp camp = estate.getCamp(n.getType());
						Nobility.getChestSelector().outputQueueCamp.put(p.getUniqueId(), camp);
						p.sendMessage(ChatColor.BLUE + "Punch a chest to select it as an output chest.");

						Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
							@Override
							public void run() {
								if(Nobility.getChestSelector().outputQueueCamp.containsKey(p.getUniqueId())) {
									Nobility.getChestSelector().outputQueueCamp.remove(p.getUniqueId());
									p.sendMessage(ChatColor.RED + "Chest selection cancelled.");
								}
							}
						}, 20*10L); //20 Tick (1 Second) delay before run() is called
						p.closeInventory();
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
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
		List<Estate> estates = Nobility.getEstateManager().getEstates();
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
			ItemAPI.addLore(icon, "Relationship: " + estate.getRelationship(otherEstate).title());
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
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
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


		Clickable infoIcon = ButtonLibrary.createEstateInfo(estate);

		gui.addSlot(infoIcon);

		gui.setSlot(ButtonLibrary.HOME.clickable(),49);
		
		// VIEW DEVELOPMENT UPGRADES
				ItemStack devUpgradeIcon = ButtonLibrary.createIcon(Material.FURNACE, "View Development Upgrades");
				Clickable devUpgradeButton = new Clickable(devUpgradeIcon) {
					@Override
					public void clicked(Player p) {
						openDevelopmentsUpgradeGUI(p);
					}
				};
				gui.setSlot(devUpgradeButton,7);

		ItemStack tips = ButtonLibrary.createIcon(Material.PAPER, ChatColor.BLUE + "Tips");
		Clickable tipsIcon = new DecorationStack(tips);
		gui.setSlot(tipsIcon,4);

		HashMap<String, DevelopmentBlueprint> blueprints_safe = (HashMap<String, DevelopmentBlueprint>) blueprints.clone();

		ArrayList<DevelopmentBlueprint> prerequisite_blueprints = new ArrayList<>();

		//Remove built developments
		for(Development d : built) {

			if(blueprints_safe.containsKey(d.name)) {
				blueprints_safe.remove(d.name);
			}
		}


		for(DevelopmentBlueprint b : blueprints_safe.values()) {
			if(!b.hasPrereqs) {
				String formattedName = b.result.name;
				ItemStack icon = ButtonLibrary.createIcon(b.result.icon, formattedName);
				ItemAPI.addLore(icon, ChatColor.BLUE + "Type: " + ChatColor.WHITE + b.result.getType().toString());

				if(b.result.getType() == DevelopmentType.CAMP) {
					Camp camp = (Camp) b.result;
					if(camp != null) { ItemAPI.addLore(icon, ChatColor.BLUE + "Node Limit: " + ChatColor.WHITE + camp.getNodeLimit());}

				}

				ItemAPI.addLore(icon, ChatColor.BLUE + "Cost:");

				for(String s : b.cost.keySet()) {
					ItemAPI.addLore(icon, ChatColor.GRAY + "  " + b.cost.get(s) + "x" + ChatColor.WHITE + " " + s);
				}
				ItemAPI.addLore(icon, ChatColor.BLUE + "Description: ");
				ItemAPI.addLore(icon, ChatColor.GRAY + b.result.buildDescription);

				if(b.result.attributes != null)
					for(DevAttribute attr : b.result.attributes.keySet()) {
						ItemAPI.addLore(icon, AttributeManager.getAttributeText(attr,b.result.attributes.get(attr)));
					}


				Clickable button = new Clickable(icon) {

					@Override
					public void clicked(Player p) {
						Group g = Nobility.getGroupManager().getGroup(p);
						if(g == null) {
							return;
						}
						GroupPermission perm = g.getPermission(p);

						if(!(perm == GroupPermission.OFFICER || perm == GroupPermission.LEADER)) {
							p.sendMessage(ChatColor.RED + "You don't have enough permission to build developments.");
							p.closeInventory();
							return;
						}

						Nobility.getDevelopmentManager().build(b, estate, player);
					}
				};
				gui.addSlot(button);
			}
			else {
				prerequisite_blueprints.add(b);
			}
		}

		for(DevelopmentBlueprint b: prerequisite_blueprints) {
			String formattedName = b.result.name;
			ItemStack icon = ButtonLibrary.createIcon(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + formattedName);
			ArrayList<Development> prereqs = b.prereqs;
			StringBuilder prereqList = new StringBuilder();
			for(Development d: prereqs) {
				prereqList.append(d);
				prereqList.append(" ");
			}
			ItemAPI.addLore(icon, ChatColor.RED + "Has Prerequisites: " + prereqList);
			ItemAPI.addLore(icon, ChatColor.BLUE + "Type: " + ChatColor.WHITE + b.result.getType().toString());

			if(b.result.getType() == DevelopmentType.CAMP) {
				Camp camp = (Camp) b.result;
				if(camp != null) { ItemAPI.addLore(icon, ChatColor.BLUE + "Node Limit: " + ChatColor.WHITE + camp.getNodeLimit());}

			}

			ItemAPI.addLore(icon, ChatColor.BLUE + "Cost:");

			for(String s : b.cost.keySet()) {
				ItemAPI.addLore(icon, ChatColor.GRAY + "  " + b.cost.get(s) + "x" + ChatColor.WHITE + " " + s);
			}
			ItemAPI.addLore(icon, ChatColor.BLUE + "Description: ");
			ItemAPI.addLore(icon, ChatColor.GRAY + b.result.buildDescription);

			if(b.result.attributes != null)
				for(DevAttribute attr : b.result.attributes.keySet()) {
					ItemAPI.addLore(icon, AttributeManager.getAttributeText(attr,b.result.attributes.get(attr)));
				}

			Clickable button = new DecorationStack(icon);

			gui.addSlot(button);
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


// Utilities
	private static int roundUpToNine(int number) {
		return rowLength * ((number / 9) + 1);
	}

}
