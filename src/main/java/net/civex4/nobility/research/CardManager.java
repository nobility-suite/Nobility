package net.civex4.nobility.research;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobility.developments.AbstractWorkshop;
import net.civex4.nobilityitems.NobilityItems;
import net.md_5.bungee.api.ChatColor;

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

	//TODO
	public Card generateCard(Blueprint bp, Player p, AbstractWorkshop w) {
		return null;
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
	
}
