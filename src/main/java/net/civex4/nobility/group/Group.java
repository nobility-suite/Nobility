package net.civex4.nobility.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Group {
	
  
    public HashMap<UUID,GroupPermission> members;
	//public ArrayList<UUID> members;
	public String name;
	public String leaderPrefix;
	public Location estateLocation;
	public boolean hasEstate;
	public ArrayList<UUID> pendingInvites;
	
	public Group(String name, UUID playerName) {
		HashMap<UUID,GroupPermission> mem = new HashMap<>();
		mem.put(playerName,GroupPermission.LEADER);
		this.members = mem;
		this.leaderPrefix = "Lord ";
		this.hasEstate = false;
		ArrayList<UUID> pending = new ArrayList<>();
		pendingInvites = pending;
		this.name = name;
	}
	
	public void addMember(Player p) {
		members.put(p.getUniqueId(),GroupPermission.DEFAULT);
	}
	
	public void removeMember(Player p) {
		members.remove(p.getUniqueId());
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
