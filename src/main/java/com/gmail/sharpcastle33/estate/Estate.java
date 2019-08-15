package com.gmail.sharpcastle33.estate;

import org.bukkit.block.Block;

import com.gmail.sharpcastle33.group.Group;

public class Estate {
	private Group group;
	private Block block;
	
	public Estate(Block block, Group group) {
		this.setGroup(group);
		this.setBlock(block);

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

}
