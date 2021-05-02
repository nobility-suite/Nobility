package net.civex4.nobility.research;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobilityitems.NobilityItem;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;

public class Card {
	
	/**
	 * A Card is a package of Actions meant to be applied to a specific AbstractBlueprint for a cost, in the Research minigame.
	 */
	private ArrayList<Action> actions;
	private AbstractBlueprint blueprint;
	private ArrayList<NobilityItem> cost;
	
	public Card(ArrayList<Action> actions, AbstractBlueprint b) {
		this.actions = actions;
		this.blueprint = b;
		cost = new ArrayList<NobilityItem>();
		this.init();
	}
	
	public boolean activate(Player activator, ItemStack unfinishedBlueprint) {
		if(checkCost(activator)) {
			payCosts(activator);
			execute(unfinishedBlueprint);
			return true;
		}else {
			activator.sendMessage(ChatColor.RED + "You do not have the required items to use this action!");
			return false;
		}
	}
	
	public void execute(ItemStack unfinishedBlueprint) {
		for(Action a : this.actions) {
			ItemUtils.addLore(unfinishedBlueprint, a.formatLine());
		}
		
		//Update round counter
		UnfinishedBlueprint ubp = UnfinishedBlueprint.parseFromItem(unfinishedBlueprint);
		List<String> lore = ItemAPI.getLore(unfinishedBlueprint);
		String roundString = UnfinishedBlueprint.ROUND_PREFIX + (ubp.getRounds()+1) + "/" + ubp.getMaxRounds() + UnfinishedBlueprint.ROUND_SUFFIX;
		
		lore.set(1, roundString);
		ItemAPI.setLore(unfinishedBlueprint, lore);
		
		if(ubp.getRounds()+1 >= ubp.getMaxRounds()) {
			//GENERATE BLUEPRINT TODO
		}
		
	}
	
	public boolean checkCost(Player p) {
		//TODO add checking costs function.
		return true;
	}
	
	public void payCosts(Player p) {
		//TODO pay costs when player activates.
	}
	
	public void init() {
		//TODO setup this card according to its cardtype.
	}
	
	public ItemStack getIcon() {
		ItemStack ret = new ItemStack(Material.PAPER);
		
		String devname = "";
		for(Action a : this.actions) {
			devname += a.type.name();
		}
		ItemUtils.setDisplayName(ret,devname);
		//TODO ADD CARD EFFECT TO LORE.
		return ret;
	}

	public ArrayList<Action> getActions() {
		// TODO Auto-generated method stub
		return this.actions;
	}
	


}
