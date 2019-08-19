package com.gmail.sharpcastle33.group;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.gmail.sharpcastle33.Nobility;

public class GroupManager {
	
	public ArrayList<Group> groups = new ArrayList<Group>();

	public Group getGroup(Player player) {
		for(int i = 0; i < groups.size(); i++) {
			Group g = groups.get(i);
			for(String s : g.members) {
				if(player.getName().equals(s)) {
					return g;
				}
			}
		}
		return null;
	}
	
	/*public GroupManager() {
		ArrayList<Group> groups = new ArrayList<Group>();
	}*/

}
