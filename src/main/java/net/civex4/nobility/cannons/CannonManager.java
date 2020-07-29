package net.civex4.nobility.cannons;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.AttributeManager;
import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;

public class CannonManager {
	
	public ArrayList<Cannon> activeCannons;

	public CannonManager() {
		this.activeCannons = new ArrayList<Cannon>();
	}
	
	public void addCannon(Cannon c) {
		this.activeCannons.add(c);
	}
	
	public void removeCannon(Cannon c) {
		this.activeCannons.remove(c);
	}
	
	public void dismantleCannon(Location loc) {
		//TODO
	}
	
	public boolean isCannon(Location loc) {
		return false;
		//TODO
	}
	
	public Cannon getCannon(Location loc) {
		for(Cannon c : activeCannons) {
				Location can = c.block.getLocation();
				if(can.getWorld() == loc.getWorld()) {
					if(can.distance(loc) <= 2) {
						return c;
					}
				}
		}
		return null;
	}
	
	public void damageCannon(Cannon c, int amt) {
		//TODO
	}
	
	private boolean hasCannonPermission(Player p) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean validCannonSpot(Location loc) {
		return true;
	}
	
	public boolean canPlaceCannons(Estate e) {
		return true;
	}
	
	public boolean hasCannons(Estate e) {
			int amt = AttributeManager.getCannons(e);
			if(amt >= 1) {
				return true;
			}else return false;
	}
	
	public void spawnCannon(Location loc, Estate e) {
		loc.getWorld().getBlockAt(loc).setType(Material.COAL_BLOCK);
		Block b = loc.getBlock();
		
		Cannon c = new Cannon(b,e);
		this.activeCannons.add(c);
		//TODO implement use up cannons here.
		
	}
	
	public void summonCannon(Player p) {
		Estate e = Nobility.getEstateManager().getEstateOfPlayer(p);
		if(e != null) {
			Location loc = p.getLocation();
			if(canPlaceCannons(e)) {
				if(hasCannonPermission(p)) {
					if(hasCannons(e)) {
						if(validCannonSpot(loc)) {
							spawnCannon(loc,e);
							p.sendMessage(ChatColor.GREEN + "Summoned cannon successfully.");
							e.getGroup().announce(ChatColor.WHITE + p.getName() + ChatColor.GREEN + " has summoned a cannon at:" 
									+ ChatColor.WHITE + " [" + loc.getBlockX() + "x, " + loc.getBlockY() + "y, " + loc.getBlockZ() + " z]");
						}else {
							p.sendMessage(ChatColor.RED + "This is not a valid cannon location");
							return;
						}
					}else{
						p.sendMessage(ChatColor.RED + "Your estate has no more cannons.");
					}
					
				}else {
					p.sendMessage(ChatColor.RED + "You don't have permission to summon cannons from your Estate.");
				}
				
			}else {
				p.sendMessage(ChatColor.RED + "Your estate cannon place cannons right now.");
				return;
			}

		}
	}

	public void fireCannon(Cannon c, Player p, Vector fire) {
		// TODO Auto-generated method stub
		p.sendMessage(ChatColor.DARK_RED + "Boom.");
		p.playSound(c.block.getLocation(), Sound.ENTITY_TNT_PRIMED, 6, 1);
		
		Vector norm = fire.normalize();
		World world = p.getWorld();
		Location loc = c.block.getLocation();
		
		Entity fireball = world.spawnEntity(c.block.getLocation().add(norm), EntityType.PRIMED_TNT);
		fireball.setVelocity(fire);
		fireball.setGravity(false);
		TNTPrimed tnt = (TNTPrimed) fireball;
		tnt.setGlowing(true);
		tnt.setYield(0);
		
		
	}


}
