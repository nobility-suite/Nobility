package net.civex4.nobility.research;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobilityitems.NobilityItem;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.api.ItemAPI;

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
	
	public boolean activate(Player activator) {
		if(checkCost(activator)) {
			payCosts(activator);
			execute();
			return true;
		}else {
			activator.sendMessage(ChatColor.RED + "You do not have the required items to use this action!");
			return false;
		}
	}
	
	public void execute() {
		
	}
	
	public boolean checkCost(Player p) {
		//TODO add checking costs function.
		return false;
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
		ItemAPI.setDisplayName(ret,devname);
		//TODO ADD CARD EFFECT TO LORE.
		return ret;
	}

	public ArrayList<Action> getActions() {
		// TODO Auto-generated method stub
		return this.actions;
	}
	


}
