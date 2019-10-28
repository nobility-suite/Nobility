package com.gmail.sharpcastle33.group;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Group {
	
	public ArrayList<UUID> members;
	public String name;
	public String leaderPrefix;
	public Location estateLocation;
	public boolean hasEstate;
	public ArrayList<UUID> pendingInvites;
	
	public Group(String name, UUID playerName) {
		ArrayList<UUID> mem = new ArrayList<>();
		mem.add(playerName);
		this.members = mem;
		this.leaderPrefix = "Lord ";
		this.hasEstate = false;
		ArrayList<UUID> pending = new ArrayList<>();
		pendingInvites = pending;
		this.name = name;
	}
	
	public void addMember(Player p) {
		members.add(p.getUniqueId());
	}
	
	public void removeMember(Player p) {
		members.remove(p.getUniqueId());
	}

}
