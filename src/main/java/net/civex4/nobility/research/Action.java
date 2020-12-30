package net.civex4.nobility.research;

import java.util.Random;

import org.bukkit.Bukkit;

import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.ItemGroup;
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
	public int lockedItemIndex = -1;
	
	private String affectedName = "";
	
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
		
		switch(this.type) {
		case LOCK_IN:
			ret += ChatColor.GRAY + "Guarantee that <" + affectedName + ChatColor.GRAY + "> appears in the recipe. <" + itemGroupIndex + ">";
			break;
		case LOCK_OUT:
			ret += ChatColor.GRAY + "Prevent " + affectedName + ChatColor.GRAY + " from appearing in the recipe. <" + itemGroupIndex + ">";
			break;
		case MOD_COSTS:
			ret += ChatColor.GRAY + "Increase blueprint costs by " + ChatColor.WHITE + amount + "%";
			break;
		case MOD_RESULT:
			ret += ChatColor.GRAY + "Increase blueprint output by " + ChatColor.WHITE + amount + "%";
			break;
		case MOD_TIME:
			if(amount > 0) {
				ret += ChatColor.GRAY + "Increase blueprint duration by " + ChatColor.WHITE + amount + "%";
			}else {
				ret += ChatColor.GRAY + "Reduce blueprint duration by " + ChatColor.WHITE + "-" + amount + "%";
			}
		case RATIO:
			if(amount > 0) {
				ret += ChatColor.GRAY + "Increase" + affectedName + ChatColor.GRAY + " requirement by " + ChatColor.WHITE + amount + "%";
			}else {
				ret += ChatColor.GRAY + "Reduce" + affectedName + ChatColor.GRAY + " requirement by " + ChatColor.WHITE + "-" + amount + "%";
			}
		default:
			break;		
		}
		return ret;
	}
	
	public static Action createRatiosAction(AbstractBlueprint abp, int pct, int itemGroupIndex, int itemIndex) {
		Action a = new Action(ActionType.MOD_COSTS,abp);
		a.amount = pct;
		a.itemGroupIndex = itemGroupIndex;
		a.lockedItemIndex = itemIndex;
		return a;
	}
	
	public static Action createModTimeAction(AbstractBlueprint abp, int pct) {
		Action a = new Action(ActionType.MOD_TIME,abp);
		a.amount = pct;
		return a;
	}
	
	public static Action createModCostsAction(AbstractBlueprint abp, int pct) {
		Action a = new Action(ActionType.MOD_COSTS,abp);
		a.amount = pct;
		return a;
	}
	
	public static Action createModResultAction(AbstractBlueprint abp, int pct) {
		Action a = new Action(ActionType.MOD_RESULT,abp);
		a.amount = pct;
		return a;
	}
	
	public static Action createAddRunsAction(AbstractBlueprint abp, int amount) {
		Action a = new Action(ActionType.ADD_RUNS,abp);
		a.amount = amount;
		return a;
	}
	
	public static Action createLockInAction(AbstractBlueprint abp, int itemGroupIndex, Random rand) {
		Action a = new Action(ActionType.LOCK_IN, abp);
		a.itemGroupIndex = itemGroupIndex;

		ItemGroup g = abp.getItemGroups().get(itemGroupIndex);
		Bukkit.getServer().getLogger().info("Accessing item group no: " + itemGroupIndex);
		int max = g.getDistinctTypes();
		int selected = rand.nextInt(max);
		a.lockedItemIndex = selected;
		Bukkit.getServer().getLogger().info("Accessing item no: " + selected +" max:" + max);

		
		a.affectedName = g.getItemName(selected);
		
		
		return a;
	}
	
	public static Action createLockOutAction(AbstractBlueprint abp, int itemGroupIndex, Random rand) {
		Action a = new Action(ActionType.LOCK_OUT, abp);
		a.itemGroupIndex = itemGroupIndex;
		
		ItemGroup g = abp.getItemGroups().get(itemGroupIndex);
		Bukkit.getServer().getLogger().info("Accessing item group no: " + itemGroupIndex);

		int max = g.getDistinctTypes();
		int selected = rand.nextInt(max);
		a.lockedItemIndex = selected;
		Bukkit.getServer().getLogger().info("Accessing item no: " + selected +" max:" + max);
		
		a.affectedName = g.getItemName(selected);
		
		return a;
	}
}
