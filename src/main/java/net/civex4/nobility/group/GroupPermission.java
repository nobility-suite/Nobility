package net.civex4.nobility.group;

import java.util.HashMap;

public enum GroupPermission {

	DEFAULT("Peasant"),
	TRUSTED("Citizen"),
	OFFICER("Official"),
	LEADER("Lord");
  
	private final String title;

	GroupPermission(String title){
		this.title = title;
	}
	
	public String defaultTitle() {
		return title;
	}
	
	public static HashMap<GroupPermission, String> getDefaultRanks() {	
		HashMap<GroupPermission, String> defaultRanks = new HashMap<>();
		for (GroupPermission rank : GroupPermission.values()) {
			defaultRanks.put(rank, rank.defaultTitle());
		}
		return defaultRanks;
	}
}
