package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.development.DevelopmentType;
import com.gmail.sharpcastle33.group.Group;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.Region;

public class Estate {
	private Group group;
	private Block block;
	private Region region;
	private List<Development> builtDevelopments;
	
	private int vulnerabilityHour;
	
	public Estate(Block block, Group group) {
		this.setGroup(group);
		this.setBlock(block);
		this.builtDevelopments = new ArrayList<>();
		this.vulnerabilityHour = 0;

		region = NobilityRegions.getRegionMaster().getRegionByLocation(block.getLocation());
	} // constructor
	
	public void buildDevelopment(DevelopmentType type) {
		Development development = new Development(type);
		builtDevelopments.add(development);
	}
	
	/**
	 * Initializes a DevelopmentRegister's Development
	 * @param register DevelopmentRegister
	 */
	/* public void initializeRegister(Development development) {
		Class developmentClass = register.getDevelopment();
		try {
			Development development = (Development) developmentClass.getConstructors()[0].newInstance();
			development.pre_init(register, this);
			development.init();
			initializedDevelopments.add(development);						
			Nobility.getDevelopmentManager().subtractCosts(register, this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} */
	
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
	
	
	
	public List<String> getUnbuiltDevelopments() {
		List<String> unbuiltDevelopments = new ArrayList<>();
		for (String name : DevelopmentType.getTypes().keySet()) {
			unbuiltDevelopments.add(name);
		}
		for (Development development : builtDevelopments) {
			unbuiltDevelopments.remove(development.getDevelopmentType().getName());
		}
		
		return unbuiltDevelopments;

	}
	
	public List<Development> getActiveDevelopments() {
		List<Development> activeDevelopments = new ArrayList<>();

		for(Development development : builtDevelopments) {
			if(development.isActive()) activeDevelopments.add(development);
		} // for

		return activeDevelopments;
	} // getActiveDevelopments
	
	public List<String> getActiveDevelopmentsToString() {
		List<String> activeDevelopments = new ArrayList<>();		
		for(Development development : builtDevelopments) {
			String name = development.getDevelopmentType().getName();
			activeDevelopments.add(name);		
		} // for
		return activeDevelopments;
	} // getActiveDevelopmentsToString

	public List<Development> getInactiveDevelopments() {
		List<Development> inactiveDevelopments = new ArrayList<>();

		for(Development development : builtDevelopments) {
			if(!development.isActive()) inactiveDevelopments.add(development);
		} // for

		return inactiveDevelopments;
	} // getInactiveDevelopments
	
	
	
	

	
	/*
	public List<Development> getUninitializedRegisteredDevelopments() {
		List<Development> uninitializedRegisteredDevelopments = new ArrayList<>();
		List<Development> initializedRegisteredDevelopments = new ArrayList<>();

		for(Development development: initializedDevelopments) {
			initializedRegisteredDevelopments.add(development);
		} // for

		for(Development register: registeredDevelopments.) {
			if(!initializedRegisteredDevelopments.contains(register)) uninitializedRegisteredDevelopments.add(register);
		} // for

		return uninitializedRegisteredDevelopments;
	} // getUninitializedRegisteredDevelopments

	public Development getDevelopmentForName(String name) {
		for(Development development: initializedDevelopments) {
			if(development.getDevelopmentType().getName().contentEquals(name)) return development;
		}

		return null;
	}*/

} // Class
