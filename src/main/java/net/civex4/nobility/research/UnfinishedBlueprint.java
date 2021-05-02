package net.civex4.nobility.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobilityitems.NobilityItem;
import net.civex4.nobilityitems.NobilityItems;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;

public class UnfinishedBlueprint {
	
	private AbstractBlueprint baseBlueprint;
	private ArrayList<Action> actions;
	private long seed;
	private int rounds;
	private int maxRounds;
	
	public final static String UNFINISHED_BLUEPRINT_PREFIX = "Unfinished ";
	public final static String UNFINISHED_BLUEPRINT_SUFFIX = ChatColor.WHITE + " Blueprint";
	
	final static String SEED_PREFIX = ChatColor.BLUE + "Seed: " + ChatColor.WHITE;
	
	final static String ROUND_PREFIX = ChatColor.BLUE + "Cards Chosen: " + ChatColor.WHITE + "[";
	final static String ROUND_SUFFIX = "]";
	
	public UnfinishedBlueprint(AbstractBlueprint recipe) {
		this.seed = new Random().nextLong();
		this.baseBlueprint = recipe;
		this.actions = new ArrayList<Action>();
		this.rounds = 0;
		this.maxRounds = 5;
	}
	
	public AbstractBlueprint getBaseBlueprint() {
		return this.baseBlueprint;
	}
	
	public int getRounds() {
		return this.rounds;
	}
	
	public int getMaxRounds() {
		return this.maxRounds;
	}
	
	public long getSeed() {
		return this.seed;
	}
	
	public void resetSeed() {
		this.seed = new Random().nextLong();
	}
	
	public static UnfinishedBlueprint parseFromItem(ItemStack i) {
		String name = i.getItemMeta().getDisplayName();
		Bukkit.getServer().getLogger().info("To parse name = " + name);
		name = name.replace(UNFINISHED_BLUEPRINT_PREFIX,"");
		name = name.replace(UNFINISHED_BLUEPRINT_SUFFIX,"");
		Bukkit.getServer().getLogger().info("Abp blueprint name (SEARCHING FOR THIS NOBILITY ITEM) : " + name);
		
		NobilityItem ni = NobilityItems.getItemByDisplayName(name);
		AbstractBlueprint abp = Nobility.getBlueprintManager().getAbstractBlueprintFromItem(ni);
		//get abstractblueprint from item
		
		UnfinishedBlueprint ubp = new UnfinishedBlueprint(abp);
		List<String> lore = ItemUtils.getLore(i);
		
		String seedString = lore.get(0);
		seedString = seedString.replaceFirst(SEED_PREFIX, "");
		Bukkit.getServer().getLogger().info("Seed string is: " + seedString);
		ubp.seed = Long.parseLong(seedString);
		
		String rounds = lore.get(1);
		String[] arr = rounds.split("/");
		arr[0] = arr[0].replace(ROUND_PREFIX,"");
		arr[1] = arr[1].replace(ROUND_SUFFIX, "");
		int spentRounds = Integer.parseInt(arr[0]);
		int maxRounds = Integer.parseInt(arr[1]);
		ubp.maxRounds = maxRounds;
		ubp.rounds = spentRounds;
		
		for(int j = 2; j < lore.size(); j++) {
			String actionString = lore.get(j);
			Action action = Nobility.getCardManager().parseToAction(actionString, abp);
			if(action == null) { 
				Bukkit.getServer().getLogger().info("Could not parse Action!: " + actionString);
				continue; }
			ubp.addAction(action);
			ubp.applyAction(action);
		}
				
		return ubp;
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
		ItemUtils.setDisplayName(ret, name);
		
		String seedString =  SEED_PREFIX + seed;
		ItemUtils.addLore(ret, seedString);
	
		String roundString = this.ROUND_PREFIX + this.rounds + "/" + this.maxRounds + this.ROUND_SUFFIX;
		ItemUtils.addLore(ret, roundString);
		
	
		for(Action a :this.getActions()) {
			ItemUtils.addLore(ret,a.formatLine());
		}
	
		
		return ret;
		
	}
	
	public Blueprint generate() {
		AbstractBlueprint abp = this.baseBlueprint.clone();
		
		//TODO apply actions
		
		return abp.generate();
	}
	
	public ArrayList<Action> getActions(){
		return this.actions;
	}
	
	public void addAction(Action a) {
		this.actions.add(a);
	}
	
	public void applyAction(Action a) {
		CardManager.apply(this,a);
	}
	


}
