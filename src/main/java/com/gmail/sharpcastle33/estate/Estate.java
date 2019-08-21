package com.gmail.sharpcastle33.estate;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.development.Development;
import com.gmail.sharpcastle33.development.DevelopmentRegister;
import io.github.kingvictoria.Region;
import org.bukkit.block.Block;

import com.gmail.sharpcastle33.group.Group;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Estate {
	private Group group;
	private Block block;
	private Region region;
	private List<Development> developments;
	private List<String> activeDevelopments;
	
	public Estate(Block block, Group group) {
		this.setGroup(group);
		this.setBlock(block);
		region = Nobility.getNobilityRegions().getRegionMaster().getRegionByLocation(block.getLocation());

		setDevelopments(new ArrayList<>());
		setActiveDevelopments(new ArrayList<>());
		for(DevelopmentRegister register: Nobility.getDevelopmentManager().getTypes()) {
			try {
				Development development = (Development) register.getDevelopment().newInstance();
				development.setRegister(register);
				development.init(this);
				getDevelopments().add(development);
			} catch (Exception e) {
				e.printStackTrace();
			} // try/catch
		} // for
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

	public List<Development> getDevelopments() {
		return developments;
	}

	public void setDevelopments(List<Development> developments) {
		this.developments = developments;
	}
	
	public List<String> getActiveDevelopments() {
		return activeDevelopments;
	}
	
	public void setActiveDevelopments(List<String> activeDevelopments) {
		this.activeDevelopments = activeDevelopments;
	}
	
	public void addActiveDevelopment(String development) {
		activeDevelopments.add(development);
	}

}
