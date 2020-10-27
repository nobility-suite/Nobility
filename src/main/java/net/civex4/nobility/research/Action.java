package net.civex4.nobility.research;

import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobilityitems.NobilityItem;

public class Action {
	
	/**
	 * An action is a specific change to applied to an AbstractBlueprint. It alters the AbstractBlueprint's possibility space in a specific way. 
	 */

	public ActionType type;
	public AbstractBlueprint bp;
	public int amount = 0;
	public NobilityItem affected;
	
	public Action(ActionType t, AbstractBlueprint b) {
		this.type = t;
		this.bp = b;
	}
	
	public Action(ActionType t, AbstractBlueprint b, NobilityItem item) {
		this.type = t;
		this.bp = b;
		this.affected = item;
	}
	
	public String formatLine() {
		switch(this.type) {
		
		}
		return "";
	}
}
