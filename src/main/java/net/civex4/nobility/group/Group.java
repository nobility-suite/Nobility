package net.civex4.nobility.group;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.civex4.nobility.gui.Renamable;

public class Group {
	
	public ArrayList<UUID> members;
	//public String name;
	public Renamable name = new Renamable() {
		private String name;
		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;			
		}		
	};
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
		this.name.setName(name);
	}
	
	public void addMember(Player p) {
		members.add(p.getUniqueId());
	}
	
	public void removeMember(Player p) {
		members.remove(p.getUniqueId());
	}
	
	public String getName() {
		return name.getName();
	}
	
	public void setName(String name) {
		this.name.setName(name);
	}

}
