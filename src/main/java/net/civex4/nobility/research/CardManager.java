package net.civex4.nobility.research;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.ItemGroup;
import net.civex4.nobility.blueprints.RatioItemGroup;
import net.civex4.nobility.blueprints.SimpleItemGroup;
import net.civex4.nobility.developments.AbstractWorkshop;
import net.civex4.nobility.gui.ButtonLibrary;
import net.civex4.nobilityitems.NobilityItems;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;

public class CardManager {
	
	HashMap<String,ActionType> actionIdentifiers;
	/**
	 * CardManager manages what Cards appear in the Research minigame, and controls how both Cards and Actions are parsed.
	 */
	public CardManager() {
		actionIdentifiers = new HashMap<String,ActionType>();
		
		for(ActionType t : ActionType.values()) {
			actionIdentifiers.put(t.identifier,t);
		}
	}
	
	public static Clickable getCardIcon(Card c, UnfinishedBlueprint ubp, Player p) {
		ItemStack icon = ButtonLibrary.createIcon(Material.PAPER, ChatColor.BLUE + "Card: " + c.getActions().get(0).type);
		for(Action a : c.getActions()) {
			ItemUtils.addLore(icon, a.formatLine());
			Bukkit.getServer().getLogger().info("action: " + a.type.identifier + ", " + a.formatLine());
		}
		
		
		Clickable cl = new Clickable(icon) {

			@Override
			public void clicked(Player p) {
				c.activate(p,p.getItemInHand());
				p.closeInventory();
				
				ItemStack i = p.getItemInHand();
				List<String> lore = ItemUtils.getLore(i);
				Long sd = new Random().nextLong();
				String seed = UnfinishedBlueprint.SEED_PREFIX + sd;
				lore.set(0, seed);
			}

		};
		return cl;
	}

	//TODO
	/**
	 * This function contains the weighting tables for different categories of Research cards. 
	 * It calls generateSpecificCard to generate an actual card from within a category.
	 * @param ubp
	 * @param p
	 * @param w
	 * @param rand | Seed given by parent Random/long on item
	 * @return
	 */
	public Card generateCard(UnfinishedBlueprint ubp, Player p, AbstractWorkshop w, Random rand) {
		HashMap<ActionType,Integer> actionWeightings = new HashMap<ActionType,Integer>();
		
		if(isLockInAvailable(ubp)) {
			actionWeightings.put(ActionType.LOCK_IN, 10);
		}
		
		if(isLockOutAvailable(ubp)) {
			actionWeightings.put(ActionType.LOCK_OUT, 20);
		}
		
		//Bulk craft card
		actionWeightings.put(ActionType.MOD_RESULT,10);
		
		if(isRatioAvailable(ubp)) {
			actionWeightings.put(ActionType.RATIO,10);
		}
		
		
		
		
		
		
		//actionWeightings.put(ActionType.ADD_RUNS, 20);
		//actionWeightings.put(ActionType.REMOVE_COST,10);
		//actionWeightings.put(ActionType.REROLL,5);
		
		int totalWeight = 0;
		
		for(ActionType t : actionWeightings.keySet()) {
			totalWeight += actionWeightings.get(t);
		}
		
		int selection = rand.nextInt(totalWeight);
		ActionType selected = null;
		
		for(ActionType t : actionWeightings.keySet()) {
			selection -= actionWeightings.get(t);
			if(selection <= 0) {
				selected = t;
				break;
			}
		}
		
		Bukkit.getServer().getLogger().info("generating card: " + selected.identifier);
		return generateSpecificCard(ubp, selected, p, w, rand);
	}
	
	public boolean isRatioAvailable(UnfinishedBlueprint ubp) {
		for(int i = 0; i < ubp.getBaseBlueprint().getItemGroups().size(); i++) {
			ItemGroup g = ubp.getBaseBlueprint().getItemGroups().get(i);
			if(g instanceof RatioItemGroup) {
				for(Action a : ubp.getActions()) {
					if(a.type == ActionType.RATIO && a.itemGroupIndex == i) {
						continue;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public Card generateSpecificCard(UnfinishedBlueprint ubp, ActionType at, Player p, AbstractWorkshop w, Random rand) {
		Card ret = null;
		ArrayList<Action> actions = new ArrayList<Action>();
		
		switch(at) {
			case LOCK_IN: //LOCK IN CARDS
			int[] options = getLockInAvailable(ubp);
			ArrayList<Integer> itemGroupIndexes = parseLockArray(options);
			int index = rand.nextInt(itemGroupIndexes.size()-1);
			if(index < 0) { break; }
			actions.add(Action.createLockInAction(ubp.getBaseBlueprint(), itemGroupIndexes.get(index), rand));
			break;
			case LOCK_OUT: //LOCK OUT CARDS
			int[] options2 = getLockOutAvailable(ubp);
			ArrayList<Integer> itemGroupIndexes2 = parseLockArray(options2);
			int index2 = rand.nextInt(itemGroupIndexes2.size()-1);
			if(index2 < 0) { break; }
			actions.add(Action.createLockOutAction(ubp.getBaseBlueprint(), itemGroupIndexes2.get(index2), rand));	
			break;
			case MOD_RESULT: //BULK INGREDIENT CARDS
				int bulkCap = 300;
				double percentResultMod = rand.nextInt(bulkCap-20)+20;
				double percentCostMod = percentResultMod - ((percentResultMod/bulkCap) * ((percentResultMod/bulkCap)) * (90 + rand.nextInt(10)));
				int prm = (int) percentResultMod + 100; // between 120 and 400
				int pcm = (int) percentCostMod + 100; //lags behind prm (between like 114 and 300)

				actions.add(Action.createModCostsAction(ubp.getBaseBlueprint(), pcm));
				actions.add(Action.createModResultAction(ubp.getBaseBlueprint(), prm));
			break;
			case RATIO: //RATIO CARDS
				ArrayList<Integer> itemGroupIndexes3 = parseRatioArray(getRatiosAvailable(ubp));
				int index3 = rand.nextInt(itemGroupIndexes3.size());
				index3 = itemGroupIndexes3.get(index3);
				//TODO
			break;
		}

		ret = new Card(actions, ubp.getBaseBlueprint());
		
		
		if(ret == null) { Bukkit.getServer().getLogger().info("Card = null!!!"); }
		if(ret != null) { Bukkit.getServer().getLogger().info(ret.getIcon().getItemMeta().getDisplayName() + "||| " + ret.getActions().size()); }
		
		return ret;
	}
	
	/**
	 * Returns arrayList of itemGroupIndicies that are valid for corresponding lock array
	 * @param toParse
	 * @return
	 */
	private ArrayList<Integer> parseLockArray(int[] toParse){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		for(int i = 0; i < toParse.length; i++) {
			if(toParse[i] > 0) {
				ret.add(i);
			}
		}
		return ret;
	}
	
	private ArrayList<Integer> parseRatioArray(int[] toParse){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		for(int i = 0; i < toParse.length; i++) {
			if(toParse[i] > 0) {
				ret.add(i);
			}
		}
		return ret;
	}
	
	private boolean isLockInAvailable(UnfinishedBlueprint ubp) {
		int[] test = getLockInAvailable(ubp);
		
		for(int i : test) {
			if(i > 0) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isLockOutAvailable(UnfinishedBlueprint ubp) {
		int[] test = getLockOutAvailable(ubp);
		
		for(int i : test) {
			if(i > 0) {
				return true;
			}
		}
		return false;
	}
	
	//Bit array, 0 for an index if that itemgroup index cannot have a ratio card applied to it, 1 otherwise.
	public int[] getRatiosAvailable(UnfinishedBlueprint ubp) {
		int[] ratios = new int[ubp.getBaseBlueprint().getItemGroups().size()];
		for(int i = 0; i < ubp.getBaseBlueprint().getItemGroups().size(); i++) {
			ItemGroup g = ubp.getBaseBlueprint().getItemGroups().get(i);
			if(g instanceof RatioItemGroup) {
				for(Action a : ubp.getActions()) {
					if(a.type == ActionType.RATIO && a.itemGroupIndex == i) {
						continue;
					}
				}
				ratios[i] = 1;
			}
			ratios[i] = 0;
		}
		return ratios;
	}
	
	/**
	 * Finds the amount of valid lock-ins remaining for each ItemGroup in an Unfinished Blueprint.
	 * An itemgroup defined as "Pick 2 of 5" has 1 remaining lock in.
	 * 
	 * Index of array = Itemgroup index
	 * @param ubp
	 * @return
	 */
	public int[] getLockInAvailable(UnfinishedBlueprint ubp) {
		AbstractBlueprint abp = ubp.getBaseBlueprint();
		ArrayList<ItemGroup> itemGroups = abp.getItemGroups();
		int[] ret = new int[itemGroups.size()];
		
		int lockins = 0;
		for(Action a : ubp.getActions()) {
			if(a.type == ActionType.LOCK_IN)
				lockins++;
		}
		
		int counter = 0;
		for(ItemGroup g : itemGroups) {
			if(g instanceof SimpleItemGroup) {
				SimpleItemGroup group = (SimpleItemGroup) g;
				int selection = group.selectionCount;
				int availables = group.items.size();
				if(selection >= 2) {
					ret[counter] = Math.max(selection-lockins-1,0);
				}else ret[counter] = 0;
			}else ret[counter] = 0;
			counter++;
		}
		return ret;
		
	}
	
	
	/**
	 * Finds the amount of valid lock-outs remaining for each ItemGroup in an Unfinished Blueprint.
	 * An itemgroup defined as "Pick 2 of 5" has 3 remaining lock outs.
	 * 
	 * Index of array = Itemgroup index
	 * @param ubp
	 * @return
	 */
	public int[] getLockOutAvailable(UnfinishedBlueprint ubp) {
		AbstractBlueprint abp = ubp.getBaseBlueprint();
		ArrayList<ItemGroup> itemGroups = abp.getItemGroups();
		int[] ret = new int[itemGroups.size()];
		
		int lockouts = 0;
		for(Action a : ubp.getActions()) {
			if(a.type == ActionType.LOCK_OUT)
				lockouts++;
		}
		
		
		int counter = 0;
		for(ItemGroup g : itemGroups) {
			if(g instanceof SimpleItemGroup) {
				SimpleItemGroup group = (SimpleItemGroup) g;
				int selection = group.selectionCount;
				int availables = group.items.size();
				if(availables-selection >= 1) {
					ret[counter] = Math.max(availables-selection-lockouts,0);
				}else ret[counter] = 0;
			}else ret[counter] = 0;
			counter++;
		}
		return ret;
	}
	
	public Action parseToAction(String line,AbstractBlueprint bp) {
		 //TODO
		line.replaceAll(ChatColor.BLUE + "", "");
		line.replaceAll(ChatColor.GRAY + "", "");
		line.replaceAll(ChatColor.WHITE + "", "");
		String[] split = line.split("]");
		
		if(split.length != 2) {
			Bukkit.getServer().getLogger().warning("Invalid Action parsed from Blueprint. Likely contains multiple ']' regex " + bp.name + ", " + bp.result.getDisplayName());
			return null; }
		
		String ident = split[0] + "]";
		ActionType t = this.actionIdentifiers.get(ident);
		if(t == null) {
			Bukkit.getServer().getLogger().warning("Invalid Action parsed from Blueprint. Action: <" + ident + " > does not exist"); 
			return null;
		}
		
		String toParse = split[1]; //TODO
		String[] parse;
		String itemName;
		String itemGroup;
		Action action = null;
		
		switch(t) {
		case LOCK_IN:
			toParse.replace("<", "");
			parse = toParse.split(">");
			itemName = parse[0];
			itemGroup = parse[1];
			action = new Action(ActionType.LOCK_IN,bp,NobilityItems.getItemByDisplayName(itemName),Integer.parseInt(itemGroup));
			break;
		case LOCK_OUT:
			toParse.replace("<", "");
			parse = toParse.split(">");
			itemName = parse[0];
			itemGroup = parse[1];
			action = new Action(ActionType.LOCK_IN,bp,NobilityItems.getItemByDisplayName(itemName),Integer.parseInt(itemGroup));
			break;
		}
		
		
		return action;
	}
	
	public ArrayList<String> parseActionsToString(Card c, UnfinishedBlueprint bp){
		ArrayList<String> ret = new ArrayList<String>();
		for(Action a : c.getActions()) {
			ret.add(a.formatLine());
		}
		return ret;
	}



	
	
	public static void apply(UnfinishedBlueprint ubp, Action a) {
		// TODO Auto-generated method stub
		switch(a.type) {
		case LOCK_IN:
		break;
		case LOCK_OUT:
		break;
		}
		
		
		
	}
	
}
