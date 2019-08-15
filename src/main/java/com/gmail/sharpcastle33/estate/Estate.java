package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;

import org.bukkit.block.Block;

import com.gmail.sharpcastle33.group.Group;
import com.gmail.sharpcastle33.region.Region;

public class Estate {
	private ArrayList<Group> group = new ArrayList<Group>();
	private Block block;
	private Region region;
	
	public Estate(Block block, ArrayList<Group> group, Region region) {
		this.setGroup(group);
		this.setBlock(block);
		this.setRegion(region);
	}

	public ArrayList<Group> getGroup() {
		return group;
	}

	public void setGroup(ArrayList<Group> group) {
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

	public void setRegion(Region region) {
		this.region = region;
	}
}
