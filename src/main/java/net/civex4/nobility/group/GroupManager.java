package net.civex4.nobility.group;

import java.util.ArrayList;
import java.util.UUID;

import net.civex4.nobility.Nobility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class GroupManager {
	
	public ArrayList<Group> groups = new ArrayList<Group>();

	public Group getGroup(Player player) {
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			if (group.getMembers().contains(player.getUniqueId())) {
				return group;
			}
		}
		return null;
	}

	public void disbandGroup(Player player, Group group) {
		GroupPermission perm = group.getPermission(player);
		String groupname = group.getName();
		if(!(perm == GroupPermission.LEADER)) {
			player.sendMessage(ChatColor.RED + "Wait... how'd you do that? (IF Statement Bypassed for disbandGroup...)");
			return;
		}

		if(group.hasEstate() == true) {
			Nobility.getEstateManager().removeEstate(player);
		}
		//Finally, delete the group...
		groups.remove(group);
		player.sendMessage(ChatColor.GOLD + "The group " + ChatColor.AQUA + groupname + ChatColor.GOLD + " has been deleted.");
	}


}
