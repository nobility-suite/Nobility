package net.civex4.nobility.research;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobilityitems.NobilityItem;
import net.civex4.nobilityitems.NobilityItems;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.api.ItemAPI;

public class UnfinishedBlueprint {
	
	private AbstractBlueprint baseBlueprint;
	private ArrayList<Card> actions;
	private long seed;
	private int rounds;
	private int maxRounds;
	
	final static String UNFINISHED_BLUEPRINT_PREFIX = ChatColor.WHITE + "Unifinished ";
	final static String UNFINISHED_BLUEPRINT_SUFFIX = ChatColor.WHITE + " Blueprint";
	
	final static String SEED_PREFIX = ChatColor.BLUE + "  Seed: " + ChatColor.WHITE;
	
	final static String ROUND_PREFIX = ChatColor.BLUE + "Cards Chosen: " + ChatColor.WHITE + "[";
	final static String ROUND_SUFFIX = "]";
	
	public UnfinishedBlueprint(AbstractBlueprint recipe) {
		this.seed = new Random().nextLong();
		this.baseBlueprint = recipe;
	}
	
	public UnfinishedBlueprint parseFromItem(ItemStack i) {
		String name = i.getItemMeta().getDisplayName();
		name.replace(UNFINISHED_BLUEPRINT_PREFIX, "");
		name.replace(UNFINISHED_BLUEPRINT_SUFFIX,"");
		
		NobilityItem ni = NobilityItems.getItemByDisplayName(name);
		AbstractBlueprint abp = Nobility.getBlueprintManager().getAbstractBlueprintFromItem(ni);
		//get abstractblueprint from item
		
		UnfinishedBlueprint ubp = new UnfinishedBlueprint(abp);
		
		//TODO get UnfinishedBlueprint attributes and parse from item
		
		return null;
	}
	
	public ItemStack createNewUnfinishedBlueprint(AbstractBlueprint baseRecipe) {
		UnfinishedBlueprint bp = new UnfinishedBlueprint(baseRecipe);
		bp.init();
		return bp.parseToItem();
	}
	
	public void init() {
		
	}
	
	public ItemStack parseToItem() {
		ItemStack ret = new ItemStack(Material.PAPER);
		NobilityItem ni = this.baseBlueprint.result;
		String name = UNFINISHED_BLUEPRINT_PREFIX + ni.getDisplayName() + UNFINISHED_BLUEPRINT_SUFFIX;
		ItemAPI.setDisplayName(ret, name);
		
		String seedString =  SEED_PREFIX + seed;  
		ItemAPI.addLore(ret, seedString);
	
		String roundString = this.ROUND_PREFIX + this.rounds + "/" + this.maxRounds + this.ROUND_SUFFIX;
		ItemAPI.addLore(ret, roundString);
		
		for(Card c : this.actions) {
			for(Action a : c.getActions()) {
				ItemAPI.addLore(ret,a.formatLine());
			}
		}
		
		return ret;
		
	}
	
	public Blueprint generate() {
		return null;
	}
	
	public ArrayList<Card> getActions(){
		return this.actions;
	}
	


}
