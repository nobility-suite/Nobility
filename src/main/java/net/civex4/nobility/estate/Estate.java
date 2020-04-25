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
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.group.Group;

public class Estate {
	private Group group;
	private Block block;
	private Region region;
	private List<Development> builtDevelopments = new ArrayList<>();
	private Map<Estate, Relationship> relationships = new HashMap<>();
	private Map<RegionResource, Integer> mines = new HashMap<>(); //Local mines only
	private int freeProductivity = 10;
	
	private int vulnerabilityHour = 0;
	
	public Estate(Block block, Group group) {
		this.setGroup(group);
		this.setBlock(block);
		region = NobilityRegions.getRegionMaster().getRegionByLocation(block.getLocation());
	}
	
	public void buildDevelopment(DevelopmentType type) {
		//DevelopmentFactory.buildDevelopment(type, this);
	}
	
	public Inventory getInventory() {
		for (Development development : builtDevelopments) {
			
		}
		//Bukkit.getLogger().warning("You cannot get the inventory of an estate that does not have an inventory");
		return null;
	}
	
	public void addMineSudo(RegionResource r) {
	  if(mines.containsKey(r)) {
	    mines.put(r, mines.get(r)+1);
	  }else {
	    mines.put(r, 1);
	  }
	}
	
	public int getMines(RegionResource r) {
	  return mines.get(r);
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
	
	
	//Returns nice looking titles (used to compare with items in GUI)
	public List<DevelopmentType> getUnbuiltDevelopments() {
		List<DevelopmentType> unbuiltDevelopments = new ArrayList<>();
//		for (String name : DevelopmentType.getTypes().keySet()) {
//			unbuiltDevelopments.add(DevelopmentType.getDevelopmentType(name));
//		}
//		for (Development development : builtDevelopments) {
//			unbuiltDevelopments.remove(development.getType());
//		}
		
		return unbuiltDevelopments;
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
	
	public int getFreeProductivity() {
		return freeProductivity;
	}
	
	public void setFreeProductivity(int productivity) {
		this.freeProductivity = productivity;
	}
	
	public void addFreeProductivity() {
		freeProductivity++;
	}
	
	public void subtractFreeProductivity() {
		if (freeProductivity > 0) {
			freeProductivity--;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
}
