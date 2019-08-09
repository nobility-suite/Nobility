package com.gmail.sharpcastle33.group;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Group {
	
	public ArrayList<String> members;
	public String name;
	public String leaderPrefix;
	public Location estateLocation;
	public boolean hasEstate;
	public ArrayList<String> pendingInvites;
	
	public Group(String name, String playerName) {
		ArrayList<String> mem = new ArrayList<String>();
		mem.add(playerName);
		this.members = mem;
		this.leaderPrefix = "Lord ";
		this.hasEstate = false;
		ArrayList<String> pending = new ArrayList<String>();
		pendingInvites = pending;
	}
	
	public void addMember(Player p) {
		members.add(p.getName());
	}
	
	public void removeMember(Player p) {
		members.remove(p.getName());
	}

}
