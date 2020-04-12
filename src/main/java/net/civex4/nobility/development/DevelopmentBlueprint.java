package net.civex4.nobility.development;

import java.util.ArrayList;
import java.util.HashMap;

public class DevelopmentBlueprint {
	
	public Development result;
	
	public boolean hasPrereqs;
	public ArrayList<Development> prereqs;
	
	public boolean isUpgrade;
	public Development base;
	
	//Needs Cost
	public HashMap<String, Integer> cost;
	
	public DevelopmentBlueprint() {
		isUpgrade = false;
		hasPrereqs = false;
	}
	

}
