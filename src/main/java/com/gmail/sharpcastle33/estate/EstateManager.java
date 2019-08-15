package com.gmail.sharpcastle33.estate;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.group.Group;

public class EstateManager {
	
	public ArrayList<Estate> estates;

	
	public EstateManager() {
		ArrayList<Estate> estates = new ArrayList<Estate>();
	}
	
	public Estate createEstate(Block block, Player player) {
		Group group = null;
		for(int i = 0; i < Nobility.groupMan.groups.size(); i++) {
			Group g = Nobility.groupMan.groups.get(i);
			for(String s : g.members) {
				if(player.getName().equals(s)) {
					group = g;
				}
			}
		}
		
		Estate estate = new Estate(block, group);		
		estates.add(estate);
		
		block.setType(Material.ENDER_CHEST);
		player.sendMessage("You have created an estate for " + group.name);
		
		return estate;
		
	}
	
}
