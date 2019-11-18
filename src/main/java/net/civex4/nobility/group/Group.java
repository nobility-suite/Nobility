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
	public HashMap<GroupPermission,String> rankLocalizations;
	public Location estateLocation;
	public boolean hasEstate;
	public ArrayList<UUID> pendingInvites;
	
	public Group(String name, UUID playerName) {
	  
	    this.hasEstate = false;
	    this.name = name;
	    
		HashMap<UUID,GroupPermission> mem = new HashMap<>();
		mem.put(playerName,GroupPermission.LEADER);
		this.members = mem;
		
		ArrayList<UUID> pending = new ArrayList<>();
		pendingInvites = pending;
		
		HashMap<GroupPermission,String> localizations = new HashMap<>();
		localizations.put(GroupPermission.DEFAULT, "Peasant");
		localizations.put(GroupPermission.TRUSTED, "Citizen");
		localizations.put(GroupPermission.OFFICER, "Official");
		localizations.put(GroupPermission.LEADER, "Lord");
		this.rankLocalizations = localizations;
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
	
	public void setLocalization(GroupPermission perm, String str) {
	  this.rankLocalizations.put(perm, str);
	}
	
	public String getLocalization(GroupPermission perm) {
	  return this.rankLocalizations.get(perm);
	}

}
