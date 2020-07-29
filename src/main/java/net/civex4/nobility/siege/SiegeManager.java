package net.civex4.nobility.siege;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.group.Group;
import net.md_5.bungee.api.ChatColor;

public class SiegeManager {
	
	ArrayList<Siege> activeSieges;
	
	public SiegeManager() {
		activeSieges = new ArrayList<Siege>();
	}
	
	public void addSiege(Siege s) {
		s.active = true;
		activeSieges.add(s);
		
		Estate e = s.getDefender();
		e.getGroup().announce(ChatColor.WHITE + e.getGroup().getName() + ChatColor.RED + " is now under siege.");
		e.getGroup().announceTitle(ChatColor.WHITE + e.getGroup().getName() + ChatColor.RED + " is under siege!", "");
		playSiegeSoundToFarPlayers(s);
		playSiegeSound(s);
		
		
	}
	
	public void playSiegeSoundToFarPlayers(Siege s) {
		Estate e = s.getDefender();
		Group g = e.getGroup();
		
		for(UUID u : g.getMembers()) {
			Player p = Bukkit.getPlayer(u);
			if(p.isOnline()) {
				boolean playsound = false;
				World world = p.getLocation().getWorld();
				Location home = e.getBlock().getLocation();
				World homeworld = home.getWorld();
				if(world == homeworld) {
					int distance = (int) p.getLocation().distance(home);
					if(distance > 300) {
						playsound = true;
					}
				}else {
					playsound = true;
				}
				
				if(playsound) {
					p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 20, 0.5f);
					Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
					    @Override
					    public void run() {
							p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 20, 0.5f);

					    }
					}, 30L); //20 Tick (1 Second) delay before run() is called
					Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
					    @Override
					    public void run() {
							p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 20, 0.5f);

					    }
					}, 60L); //20 Tick (1 Second) delay before run() is called
				}

				}
			}
		}
	
	
	public void playSiegeSound(Siege s) {
		Location loc = s.getDefender().getBlock().getLocation();
		loc = loc.add(new Vector(0,12,0));
    	loc.getWorld().playSound(loc, Sound.BLOCK_BELL_USE, 20, 0.5f);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
		    @Override
		    public void run() {
				Location loc = s.getDefender().getBlock().getLocation();
				loc = loc.add(new Vector(0,12,0));
		    	loc.getWorld().playSound(loc, Sound.BLOCK_BELL_USE, 20, 0.5f);
		    }
		}, 30L); //20 Tick (1 Second) delay before run() is called
		Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
		    @Override
		    public void run() {
				Location loc = s.getDefender().getBlock().getLocation();
				loc = loc.add(new Vector(0,12,0));
		    	loc.getWorld().playSound(loc, Sound.BLOCK_BELL_USE, 20, 0.5f);
		    }
		}, 60L); //20 Tick (1 Second) delay before run() is called
	}

}
