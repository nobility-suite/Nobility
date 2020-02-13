package net.civex4.nobility.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Group {
	  
    private HashMap<UUID, GroupPermission> members = new HashMap<>();
	private String name;
	private HashMap<GroupPermission, String> rankLocalizations; // possibly change to localRankNames
	private boolean hasEstate = false;
	private ArrayList<UUID> pendingInvites = new ArrayList<>();
	
	public Group(String name, UUID leader) {	  
		this.hasEstate = false;
	    this.name = name;
		members.put(leader, GroupPermission.LEADER);
		this.rankLocalizations = GroupPermission.getDefaultRanks();
	}
	
	public void addMember(Player p) {
		addMember(p.getUniqueId());
	}
	
	public void addMember(UUID id) {
		members.put(id, GroupPermission.DEFAULT);
	}
	
	public void removeMember(Player p) {
		members.remove(p.getUniqueId());
	}
	
	public Set<UUID> getMembers() {
		return members.keySet();
	}
	
	public GroupPermission getPermission(Player p) {
		return members.get(p.getUniqueId());
	}
	
	public void setPermission(Player p, GroupPermission permission) {
		members.put(p.getUniqueId(), permission);
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

	public ArrayList<UUID> getPendingInvites() {
		return pendingInvites;
	}

	public void addPendingInvite(UUID id) {
		pendingInvites.add(id);
	}
	
	public boolean hasEstate() {
		return hasEstate;
	}
	
	public void setHasEstate(boolean hasEstate) {
		this.hasEstate = hasEstate;
	}

  public Player getLeader() {
    // TODO Auto-generated method stub
    for(UUID u : members.keySet()) {
      if(members.get(u) == GroupPermission.LEADER) {
        return Bukkit.getPlayer(u);
      }
    }
    return null;
  }

  public ArrayList<String> getOfficials() {
    // TODO Auto-generated method stub
    ArrayList<String> ret = new ArrayList<String>();
    
    for(UUID u : members.keySet()) {
      if(members.get(u) == GroupPermission.OFFICER) {
        ret.add(Bukkit.getPlayer(u).getName());
      }
    }
    return ret;
  }
}
