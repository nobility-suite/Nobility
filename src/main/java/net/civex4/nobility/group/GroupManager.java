package net.civex4.nobility.group;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GroupManager {
	
	public ArrayList<Group> groups = new ArrayList<Group>();

	public Group getGroup(Player player) {
		for(int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			if (group.members.contains(player.getUniqueId())) {
				return group;
			}
		}
		return null;
	}


}
