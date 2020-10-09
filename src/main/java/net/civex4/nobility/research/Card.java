package net.civex4.nobility.research;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobilityitems.NobilityItem;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.api.ItemAPI;

public class Card {
	
	private CardType type;
	private Blueprint blueprint;
	private ArrayList<NobilityItem> cost;
	
	public Card(CardType t, Blueprint b) {
		this.type = t;
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
		ItemAPI.setDisplayName(ret,type.toString());
		//TODO ADD CARD EFFECT TO LORE.
		return ret;
	}

}
