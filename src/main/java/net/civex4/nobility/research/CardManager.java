package net.civex4.nobility.research;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobility.developments.AbstractWorkshop;

public class CardManager {
	
	HashMap<String,ActionType> actionIdentifiers;
	
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
	
	public Action parseToAction(String line,Blueprint bp) {
		 //TODO
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
		
		
		
		
		return null;
	}
	
	public ArrayList<String> parseActionsToString(UnfinishedBlueprint bp){
		ArrayList<String> ret = new ArrayList<String>();
		for(Card c : bp.getActions()) {
			ret.add(parseActionToString(c));
		}
		return ret;
	}
	
	public String parseActionToString(Card c) {
		return "";
	}
}
