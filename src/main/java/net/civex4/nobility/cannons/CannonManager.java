package net.civex4.nobility.cannons;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.AttributeManager;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.siege.Siege;
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
	
	public void spawnCannon(Location loc, Estate e) throws WorldEditException {
		//loc.getWorld().getBlockAt(loc).setType(Material.COAL_BLOCK);
		Location loc2 = loc.add(new Vector(0,1,0));
		Block b = loc2.getBlock();
		
		Clipboard schem = Nobility.getSchematic("cannon");
		World world = loc.getWorld();
		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
		    Operation operation = new ClipboardHolder(schem)
		            .createPaste(editSession)
		            .to(BlockVector3.at(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()))
		            // configure here
		            .build();
		    Operations.complete(operation);
		}
        
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
							try {
								spawnCannon(loc,e);
							} catch (WorldEditException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							AttributeManager.spendCannon(e);
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
	
	public void damageNearbyEstate(Location loc, int amount, Player p) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
			Siege s = Nobility.getSiegeManager().getNearbySiege(loc);
			
			
			@Override
		    public void run() {
				if(s == null) {
					p.sendMessage(ChatColor.DARK_RED + "Could not find a siege nearby. No damage was dealt.");
					return; }
		    	s.getDefender().reduceHealth(amount);
		    	
		    }
		}, 135L);
	}

	public void fireCannon(Cannon c, Player p, Vector fire) {
		// TODO Auto-generated method stub
		p.sendMessage(ChatColor.DARK_RED + "Boom.");
		p.getWorld().playSound(c.block.getLocation(), Sound.ENTITY_TNT_PRIMED, 6, 1);
		
		Vector norm = fire.normalize();
		World world = p.getWorld();
		Location lc = c.block.getLocation();
		Location loc = lc.add(new Vector(0.5,0.5,0.5));
		
		//world.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, loc.add(0,1,0), 5, 0, 0, 0, 0.02);
		playFireStorm(loc);
		damageNearbyEstate(loc,5,p);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
		    @Override
		    public void run() {
				world.playSound(c.block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 6, 1.2f);
		    	Entity fireball = world.spawnEntity(loc.add(norm), EntityType.PRIMED_TNT);
		    	Vector v = fire.normalize();
		    	v.add(new Vector (0,0.8,0));
		    	v.multiply(2);
		    	
		    	Random rand = new Random();
		    	double rx = rand.nextDouble()/3;
		    	double ry = rand.nextDouble()/8;
		    	double rz = rand.nextDouble()/3;
		    	int coinflip = rand.nextInt(1);
		    	if(coinflip == 0) { coinflip = -1; }
		    	rx = rx*coinflip;
		    	coinflip = rand.nextInt(1);
		    	if(coinflip == 0) { coinflip = -1; }
		    	ry = ry*coinflip;
		    	coinflip = rand.nextInt(1);
		    	if(coinflip == 0) { coinflip = -1; }
		    	rz = rz*coinflip;
		    	v.add(new Vector(rx,ry,rz));
		    	
				fireball.setVelocity(v);
				TNTPrimed tnt = (TNTPrimed) fireball;
				tnt.setGlowing(true);
				tnt.setYield(0);
		    }
		}, 40L); //20 Tick (1 Second) delay before run() is called
		
		
	
		
		
	}
	
    public void playFireStorm(final Location location) {
    	final int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Nobility.getNobility(), new Runnable() {
    	  Location l = location;
            @Override
            public void run() {
                try {
                        l.getWorld().spawnParticle(Particle.SMOKE_LARGE, l, 5, 0.3f, 0, 0.3f, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2);
    	
    	Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
		    @Override
		    public void run() {
		    	Bukkit.getScheduler().cancelTask(id);
		    }
		}, 40L); //20 Tick (1 Second) delay before run() is called
				
    	
    }


}
