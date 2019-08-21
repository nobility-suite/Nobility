package com.gmail.sharpcastle33.estate;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.development.DevelopmentRegister;
import io.github.kingvictoria.Region;
import org.bukkit.block.Block;

import com.gmail.sharpcastle33.group.Group;

import java.util.ArrayList;
import java.util.List;

public class Estate {
	private Group group;
	private Block block;
	private Region region;
	private List<DevelopmentRegister> registeredDevelopments;
	private List<Development> initializedDevelopments;
	
	public Estate(Block block, Group group) {
		this.setGroup(group);
		this.setBlock(block);
		this.registeredDevelopments = Nobility.getDevelopmentManager().getTypes();
		this.initializedDevelopments = new ArrayList<>();

		region = Nobility.getNobilityRegions().getRegionMaster().getRegionByLocation(block.getLocation());
	} // constructor

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

	public Region getRegion() { return region; }

	public List<DevelopmentRegister> getRegisteredDevelopments() { return registeredDevelopments; }

	public List<Development> getDevelopments() {
		return initializedDevelopments;
	}
	
	public List<Development> getActiveDevelopments() {
		List<Development> activeDevelopments = new ArrayList<>();

		for(Development development: initializedDevelopments) {
			if(development.isActive()) activeDevelopments.add(development);
		} // for

		return activeDevelopments;
	} // getActiveDevelopments

	public List<Development> getInactiveDevelopments() {
		List<Development> inactiveDevelopments = new ArrayList<>();

		for(Development development: initializedDevelopments) {
			if(!development.isActive()) inactiveDevelopments.add(development);
		} // for

		return inactiveDevelopments;
	} // getInactiveDevelopments

	public List<DevelopmentRegister> getUninitializedRegisteredDevelopments() {
		List<DevelopmentRegister> uninitializedRegisteredDevelopments = new ArrayList<>();
		List<DevelopmentRegister> initializedRegisteredDevelopments = new ArrayList<>();

		for(Development development: initializedDevelopments) {
			initializedRegisteredDevelopments.add(development.getRegister());
		} // for

		for(DevelopmentRegister register: registeredDevelopments) {
			if(!initializedRegisteredDevelopments.contains(register)) uninitializedRegisteredDevelopments.add(register);
		} // for

		return uninitializedRegisteredDevelopments;
	} // getUninitializedRegisteredDevelopments

} // Class
