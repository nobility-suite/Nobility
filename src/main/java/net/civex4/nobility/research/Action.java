package net.civex4.nobility.research;

import java.util.Random;

import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobilityitems.NobilityItem;
import net.md_5.bungee.api.ChatColor;

public class Action {
	
	/**
	 * An action is a specific change to applied to an AbstractBlueprint. It alters the AbstractBlueprint's possibility space in a specific way. 
	 */

	public ActionType type;
	public AbstractBlueprint bp;
	public int amount = 0;
	public int itemGroupIndex = -1;
	public NobilityItem affected;
	
	public Action(ActionType t, AbstractBlueprint b) {
		this.type = t;
		this.bp = b;
	}
	
	public Action(ActionType t, AbstractBlueprint b, NobilityItem item, int index) {
		this.type = t;
		this.bp = b;
		this.affected = item;
		this.itemGroupIndex = index;
	}
	
	public String formatLine() {
		String ret = "";
		ret += ChatColor.BLUE + this.type.identifier;
		
		String affectedName = "";
		if(affected != null) {
			affectedName = this.affected.getDisplayName();
		}
		switch(this.type) {
		case LOCK_IN:
			ret += ChatColor.GRAY + "Guarantee that <" + affectedName + ChatColor.GRAY + "> appears in the recipe. <" + itemGroupIndex + ">";
			break;
		case LOCK_OUT:
			ret += ChatColor.GRAY + "Prevent " + affectedName + ChatColor.GRAY + " from appearing in the recipe. <" + itemGroupIndex + ">";
			break;
		default:
			break;
			
		}
		return "";
	}
	
	public static Action createLockInAction(AbstractBlueprint abp, int itemGroupIndex, Random rand) {
		Action a = new Action(ActionType.LOCK_IN, abp);
		a.itemGroupIndex = itemGroupIndex;
		
		
		return a;
	}
	
	public static Action createLockOutAction(AbstractBlueprint abp, int itemGroupIndex, Random rand) {
		Action a = new Action(ActionType.LOCK_OUT, abp);
		a.itemGroupIndex = itemGroupIndex;
		
		return a;
	}
}
