package net.civex4.nobility.research;

public enum ActionType {
	

	
	LOCK_IN("[+]"),
	LOCK_OUT("[-]"),
	ADD_RUNS("[+r]"),
	REMOVE_RUNS("[-r]"),
	ADD_TIME("[+t]"),
	REMOVE_TIME("[-t]"),
	ADD_COST("[+c]"),
	REMOVE_COST("[-c]"),
	MOD_RESULT("[*r]"),
	MOD_COSTS("[*c]"),
	RATIO("[%c]"),
	REROLL("[rr]");
	

	public String identifier;
	
	ActionType(String s) {
		// TODO Auto-generated constructor stub
		this.identifier = s;
	}


	

}
