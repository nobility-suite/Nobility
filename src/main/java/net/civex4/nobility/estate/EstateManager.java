package net.civex4.nobility.estate;

import io.github.kingvictoria.regions.Region;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.group.Group;
import net.civex4.nobility.group.GroupPermission;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vg.civcraft.mc.civmodcore.api.ItemAPI;

import java.util.*;

public class EstateManager {
  	
	private ArrayList<Estate> estates = new ArrayList<>();
	private HashMap<UUID, Estate> estateOfPlayer = new HashMap<>();

	
	public int getDistance(Location loc, Estate e) {
		World world = loc.getWorld();
		if(e.getBlock().getLocation().getWorld() == world) {
			Location to = e.getBlock().getLocation();
			return TwoDDist(to,loc);
		}else return Integer.MAX_VALUE;
	}
	
	public Estate getNearestEstate(Location loc) {
		World world = loc.getWorld();
		int closest = Integer.MAX_VALUE;
		Estate chose = null;
		for(Estate e : estates) {
			if(e.getBlock().getLocation().getWorld() == world) {
				Location to = e.getBlock().getLocation();
				int distance = TwoDDist(to,loc);
				if(distance < closest) {
					closest = distance;
					chose = e;
				}
			}
		}
		return chose;
	}
	
	public Estate getEstate(Group g) {
		for(Estate e : this.estates) {
			if(e.getGroup() == g) {
				return e;
			}
		}
		return null;
	}
	
	public int TwoDDist(Location to, Location from) {
		if(to.getWorld() == from.getWorld()) {
			int tox = to.getBlockX();
			int toz = to.getBlockZ();
			
			int fromx = from.getBlockX();
			int fromz = from.getBlockZ();
			
			return (int) Math.sqrt(Math.pow(fromx-tox, 2) + Math.pow(fromz-toz, 2));
		}else { return (int) Integer.MAX_VALUE; }
	}
	
	public boolean isVulnerable(Estate e) {
		int h = e.getVulnerabilityHour(); //should be between 0 and 23;
		Calendar rightNow = Calendar.getInstance();
		int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
		return currentHour >= h && currentHour < ((h+2) % 24);
	}
	
	public Estate createEstate(Block block, Player player) {
		Group group = Nobility.getGroupManager().getGroup(player);
		group.setHasEstate(true);
		block.setType(Material.ENDER_CHEST);		
		Estate estate = new Estate(block, group);		
		estates.add(estate);
		
		player.sendMessage(ChatColor.GOLD + "You have created an estate for "+ ChatColor.AQUA + group.getName());
		for(UUID p : group.getMembers()) {
			setEstateOfPlayer(Bukkit.getPlayer(p), estate);
		}
		
		return estate;
		
	}

	public Estate removeEstate(Player player) {
		Group group = Nobility.getGroupManager().getGroup(player);
		Estate estate = Nobility.getEstateManager().getEstate(group);
		Block block = estate.getBlock();
		group.setHasEstate(false);
		block.setType(Material.AIR);
		estates.remove(estate);

		player.sendMessage(ChatColor.GOLD + "Successfully removed Estate for " + ChatColor.AQUA + group.getName());

		return estate;
	}
	

/* ============================================================
 * MENU CODE
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *
 */


	public List<Estate> getEstates() {
		return estates;
	}

	// Player-Estate map
	public void setEstateOfPlayer(Player p, Estate e) {
		estateOfPlayer.put(p.getUniqueId(), e);
	}

	public Estate getEstateOfPlayer(Player p) {
		return estateOfPlayer.get(p.getUniqueId());
	}

	public boolean playerHasEstate(Player p) {
		return estateOfPlayer.containsKey(p.getUniqueId());
	}

	// Region Estate
	public List<Estate> getEstatesInRegion(Region region) {
		List<Estate> estatesInRegion = new ArrayList<>();
		for (Estate estate : estates) {
			if (estate.getRegion().equals(region)) {
				estatesInRegion.add(estate);
			}
		}
		return estatesInRegion;
	}

	// Utilities
	private static void nameItem(ItemStack item, String name) {
		ItemAPI.setDisplayName(item, ChatColor.WHITE + name);
	}

	private static void addLore(ItemStack item, String text) {
		ItemAPI.addLore(item, text);
	}


	public void sendInfoMessage(Estate e, Player p) {

		Calendar rightNow = Calendar.getInstance();
		int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
		int minutes = rightNow.get(Calendar.MINUTE);

		Group g = e.getGroup();
		p.sendMessage(ChatColor.GOLD + "-=- " + g.getName() + " -=-");
		p.sendMessage(ChatColor.GOLD + "Region: " + ChatColor.YELLOW + e.getRegion().getName());
		p.sendMessage(ChatColor.GOLD + "Leader: " + ChatColor.YELLOW
				+ g.getLocalization(GroupPermission.LEADER)
				+ " " + g.getLeader().getName());

		sendOfficialsMessage(e,p);

		p.sendMessage(ChatColor.GOLD + "Total Members: " + ChatColor.YELLOW + g.getMembers().size());
		p.sendMessage(ChatColor.GOLD + "Location: " + ChatColor.YELLOW + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z");
		p.sendMessage(ChatColor.GOLD + "Siege Window: " + ChatColor.YELLOW + e.getVulnerabilityHour() + " to " + ((e.getVulnerabilityHour() + 2) % 24) + ChatColor.GOLD + " | Current Time: " + ChatColor.YELLOW + currentHour + ":" + minutes + ".");
		return; //TODO
	}

	public void sendOfficialsMessage(Estate e, Player p) {
		Group g = e.getGroup();
		ArrayList<String> list = g.getOfficials();

		int extras = 0;

		if(list.size() > 4) {
			extras = list.size()-4;
		}

		String message = ChatColor.GOLD + "Officials: " + ChatColor.YELLOW;

		if(list.size() == 0) {
			message += "None. ";
			return;
		}

		if(extras > 0) {
			for(int i = 0; i < 4; i++) {
				message += g.getLocalization(GroupPermission.OFFICER) + " " + list.get(i) + ", ";
				if(i == 3) {
					message += "and " + extras + " more...";
				}

			}
		}else {
			for(String name : list) {
				message += g.getLocalization(GroupPermission.OFFICER) + " " + name + ", ";
			}
		}

		p.sendMessage(message);

	}

	public void sendMembersMessage(Estate e, Player p) {

	}


	
	
}
