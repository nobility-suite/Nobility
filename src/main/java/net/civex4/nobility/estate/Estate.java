package net.civex4.nobility.estate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.Region;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.group.Group;

public class Estate {
	private Group group;
	private Block block;
	private Region region;
	//TODO: Replace builtDevelopments with a hashmap marking each developmentType to a boolean
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
		Development development = new Development(type, this);
		development.getDeveloper().build();
		builtDevelopments.add(development);
		Nobility.getDevelopmentManager().subtractCosts(type, this);
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
	
	
	//Returns nice looking titles (used to compare with items in GUI)
	public List<DevelopmentType> getUnbuiltDevelopments() {
		List<DevelopmentType> unbuiltDevelopments = new ArrayList<>();
		for (String name : DevelopmentType.getTypes().keySet()) {
			unbuiltDevelopments.add(DevelopmentType.getDevelopmentType(name));
		}
		for (Development development : builtDevelopments) {
			unbuiltDevelopments.remove(development.getDevelopmentType());
		}
		
		return unbuiltDevelopments;
	}
	
	public List<Development> getActiveDevelopments() {
		List<Development> activeDevelopments = new ArrayList<>();

		for(Development development : builtDevelopments) {
			if(development.isActive()) activeDevelopments.add(development);
		} // for

		return activeDevelopments;
	}
	
	public List<String> getActiveDevelopmentsToString() {
		List<String> activeDevelopments = new ArrayList<>();		
		for(Development development : builtDevelopments) {
			String name = development.getDevelopmentType().getName();
			activeDevelopments.add(name);		
		} // for
		return activeDevelopments;
	}

	public List<Development> getInactiveDevelopments() {
		List<Development> inactiveDevelopments = new ArrayList<>();

		for(Development development : builtDevelopments) {
			if(!development.isActive()) inactiveDevelopments.add(development);
		} // for

		return inactiveDevelopments;
	} // getInactiveDevelopments
	
}
