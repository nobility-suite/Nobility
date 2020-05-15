package net.civex4.nobility.estate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.Region;
import io.github.kingvictoria.RegionResource;
import io.github.kingvictoria.nodes.NodeType;
import net.civex4.nobility.development.Camp;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.group.Group;

public class Estate {
	private Group group;
	private Block block;
	private Region region;
	private List<Development> builtDevelopments = new ArrayList<>();
	private Map<Estate, Relationship> relationships = new HashMap<>();
	
	private int vulnerabilityHour = 0;
	
	public Estate(Block block, Group group) {
		this.setGroup(group);
		this.setBlock(block);
		region = NobilityRegions.getRegionMaster().getRegionByLocation(block.getLocation());
	}
	
	public void addDevelopment(Development d) {
		builtDevelopments.add(d);
	}
	
	public Inventory getInventory() {
		for (Development development : builtDevelopments) {
			
		}
		//Bukkit.getLogger().warning("You cannot get the inventory of an estate that does not have an inventory");
		return null;
	}
	

	
	public int getVulnerabilityHour() {
	  return this.vulnerabilityHour;
	}
	
	public void setVulnerabilityHour(int i) {
	  this.vulnerabilityHour = i;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Region getRegion() { 
		return region; 
	}

	public List<Development> getBuiltDevelopments() {
		return builtDevelopments;
	}
	
	
	
	public Development getDevelopment(DevelopmentType type) {
		for (Development development : builtDevelopments) {
			if (development.getType().equals(type)) {
				return development;
			}
		}
		
		Bukkit.getLogger().warning("This estate does not have this development type");
		return null;
	}
	
	public List<Development> getActiveDevelopments() {
		List<Development> activeDevelopments = new ArrayList<>();

		for(Development development : builtDevelopments) {
			if(development.isActive()) activeDevelopments.add(development);
		}

		return activeDevelopments;
	}
	
	public List<String> getActiveDevelopmentsToString() {
		List<String> activeDevelopments = new ArrayList<>();		
//		for(Development development : getActiveDevelopments()) {
//			String name = development.getType().getName();
//			activeDevelopments.add(name);		
//		}
		return activeDevelopments;
	}

	public List<Development> getInactiveDevelopments() {
		List<Development> inactiveDevelopments = new ArrayList<>();

		for(Development development : builtDevelopments) {
			if(!development.isActive()) inactiveDevelopments.add(development);
		}

		return inactiveDevelopments;
	}
	
	public void addRelationship(Estate estate, Relationship relationship) {
		if (estate.equals(this)) {
			Bukkit.getLogger().warning(this.getGroup().getName() + "\'s relationship to itself cannot be set");
		}		
		relationships.put(estate, relationship);
	}
	
	public void removeRelationship(Estate estate) {
		if (estate.equals(this)) {
			return;
		}
		relationships.remove(estate);
	}
	
	public Relationship getRelationship(Estate estate) {
		if (!relationships.containsKey(estate)) {
			return Relationship.NEUTRAL;
		} else {
		return relationships.get(estate);
		}
	}

	public int getCollectionPower(RegionResource resource) {
		int power = 0;
//		for (Development development : this.getActiveDevelopments()) {
//			if (development.getType().isCollector() && development.getType().getResource() == resource) {
//				power += development.getCollector().getCollectionPower();
//			}
//		}
		return power;
	}
	
	
	
	public ArrayList<Camp> getCamps(){
		ArrayList<Camp> ret = new ArrayList<Camp>();
		for(Development d : this.builtDevelopments) {
			if(d.getType() == DevelopmentType.CAMP) {
				ret.add((Camp) d);
			}
		}
		return ret;
	}
	
	public Camp getCamp(NodeType type) {
		for(Camp c : this.getCamps()) {
			if(c.nodeType == type) {
				return c;
			}
		}
		return null;
	}
	
}
