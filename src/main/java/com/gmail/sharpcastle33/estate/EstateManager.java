package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.gmail.sharpcastle33.group.Group;
import com.gmail.sharpcastle33.region.Region;

public class EstateManager {
	
	public ArrayList<Estate> estates = new ArrayList<Estate>();
	
	public Estate createEstate(Block block, ArrayList<Group> group) {
		
		Region region = new Region(block.getBiome().toString());
		Estate estate = new Estate(block, group, region);
		
		estates.add(estate);
		
		block.setType(Material.ENDER_CHEST);
		
		return estate;
		
	}
	
}
