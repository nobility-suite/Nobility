package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;

import org.bukkit.block.Block;

import com.gmail.sharpcastle33.group.Group;

public class Estate {
	private ArrayList<Group> group = new ArrayList<Group>();
	private Block block;
	
	public Estate(Block block, ArrayList<Group> group) {
		this.setGroup(group);
		this.setBlock(block);

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

}
