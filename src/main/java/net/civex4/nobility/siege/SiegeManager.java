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
import net.civex4.nobility.development.AttributeManager;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.group.Group;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.scoreboard.side.CivScoreBoard;

public class SiegeManager {
	
	ArrayList<Siege> activeSieges;
	private final int CANNON_MAX_DISTANCE = 60;
	
	public SiegeManager() {
		activeSieges = new ArrayList<Siege>();
	}
	
	public Siege getNearbySiege(Location loc) {
		if(activeSieges.size() == 0) {
			Bukkit.getServer().getLogger().info("No sieges active. Is this a bug?");
		}
		for(Siege s : activeSieges) {
			Location to = s.getDefender().getBlock().getLocation();
			
			if(to.getWorld() == loc.getWorld()) {
				int dist = Nobility.getEstateManager().TwoDDist(to, loc);
				Bukkit.getServer().getLogger().info(dist + "_distance");
				if(dist > 300) { continue; }
				Estate e = s.getDefender();
				int radius = AttributeManager.getCityLimit(e);
				if(dist-radius < CANNON_MAX_DISTANCE) {
					return s;
				}
			}
		}
		return null;
	}
	
	public void addSiege(Siege s) {
		s.active = true;
		activeSieges.add(s);
		
		Estate e = s.getDefender();
		e.getGroup().announce(ChatColor.WHITE + e.getGroup().getName() + ChatColor.RED + " is now under siege.");
		e.getGroup().announceTitle(ChatColor.WHITE + e.getGroup().getName() + ChatColor.RED + " is under siege!", "");
		playSiegeSoundToFarPlayers(s);
		playSiegeSound(s);
		
		startScoreboard(s);
		
		
		
		
	}
	
	public void startScoreboard(Siege s) {
		Bukkit.getScheduler().runTaskTimer(Nobility.getNobility(), () -> {
			for (Player p : Bukkit.getOnlinePlayers()) {
				updateScoreboard(s,p);
			}
		}, 5L, 5L);
	}
	
	public void updateScoreboard(Siege s, Player p) {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(ChatColor.BLACK + "a1 " + ChatColor.RED + ChatColor.BOLD + s.getDefender().getGroup().getName());
		lines.add(ChatColor.BLACK + "a2 " + ChatColor.WHITE + "" + s.getOnlineDefenders() + ChatColor.RED + " Defenders ");
		
		lines.add(ChatColor.BLACK + "a3 " + ChatColor.RED + "Health: " + ChatColor.WHITE + "[" + s.getHealth() + "/" + s.getMaxHealth() + "]");
		lines.add(ChatColor.BLACK + "a4 " + ChatColor.RED + "Siege ends in:");
		lines.add(ChatColor.BLACK + "a5 " + ChatColor.WHITE + "" + s.formatTime());
		int linenum = 0;
		for(CivScoreBoard score : s.getScoreboard()) {
			score.set(p, lines.get(Math.min(linenum,lines.size()-1)));
			linenum++;
		}
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
