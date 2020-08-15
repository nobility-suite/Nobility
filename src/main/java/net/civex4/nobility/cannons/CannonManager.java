package net.civex4.nobility.cannons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.AttributeManager;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.siege.Siege;
import net.md_5.bungee.api.ChatColor;

public class CannonManager {
	
	public ArrayList<Cannon> activeCannons;
	public HashMap<Cannon, Long> cannonCooldowns;
	public HashMap<UUID, Long> playerCooldowns;
	public final long CANNON_COOLDOWN_PICKUP_MS = 1000*60*5;

	public CannonManager() {
		this.activeCannons = new ArrayList<Cannon>();
		this.cannonCooldowns = new HashMap<Cannon, Long>();
	    this.playerCooldowns = new HashMap<UUID, Long>();
	}
	
	public void addCannon(Cannon c) {
		this.activeCannons.add(c);
	}
	
	public void removeCannon(Cannon c) {
		this.activeCannons.remove(c);
		dismantleCannon(c.block.getLocation());
		return;
	}
	
	public void dismantleCannon(Location loc) {
		Location corner = loc.clone().add(new Vector(-2,-2,-2));
		int x = corner.getBlockX();
		int y = corner.getBlockY();
		int z = corner.getBlockZ();
		World world = corner.getWorld();
		
		world.playSound(loc, Sound.BLOCK_ANVIL_BREAK, 6, 0.8f);

		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
		    @Override
		    public void run() {
				world.playSound(loc, Sound.BLOCK_ANVIL_PLACE, 6, 0.8f);
		    	for(int tx = 0; tx < 5; tx++) {
					for(int ty = 0; ty < 5; ty++) {
						for(int tz = 0; tz < 5; tz++) {
							Block b = loc.getWorld().getBlockAt(x+tx, y+ty, z+tz);
							Material m = b.getType();
							if(m == Material.PACKED_ICE || m == Material.SPRUCE_STAIRS || m == Material.SPRUCE_WOOD
									|| m == Material.LEVER || m == Material.STONE_BUTTON || m == Material.SPRUCE_TRAPDOOR || m == Material.SPRUCE_LOG) {
								b.setType(Material.AIR);
							}
							
						}
					}
				}
		    }
		}, 40L); //20 Tick (1 Second) delay before run() is called
		
		
	}
	
	public boolean isCannon(Location loc) {
		if(getCannon(loc) != null) {
			return true;
		}else return false;
	}
	
	public boolean attemptPickupCannon(Player p) {
		Location loc = p.getLocation();
		Estate e = Nobility.getEstateManager().getEstateOfPlayer(p);
		if(e == null) { return false; }
		
		for(Cannon c : this.activeCannons) {
			if(c == null) { continue; }
			Location to = c.block.getLocation();
			if(to.getWorld() == loc.getWorld()) {
				if(to.distance(loc) <= 4) {
					if(this.cannonCooldowns.containsKey(c)) {
						if(this.cannonCooldowns.get(c) > this.CANNON_COOLDOWN_PICKUP_MS) {
							if(c.owner == e) {
								if(AttributeManager.getCannonLimit(e) > AttributeManager.getCannons(e)) {
									this.removeCannon(c);
									AttributeManager.addCannon(e);
									p.sendMessage(ChatColor.GREEN + "Cannon recovered.");
									e.getGroup().announce(ChatColor.WHITE + p.getName() + ChatColor.GREEN + " has recovered a cannon at:" 
											+ ChatColor.WHITE + " [" + loc.getBlockX() + "x, " + loc.getBlockY() + "y, " + loc.getBlockZ() + " z]");
									return true;
								}else {
									p.sendMessage(ChatColor.RED + "Your estate cannot store any more cannons.");
									return false;
								}
							}else {
								p.sendMessage(ChatColor.RED + "You do not own this cannon.");
								return false;
							}
						}else {
							p.sendMessage(ChatColor.RED + "You must wait five minutes without firing to pick up this cannon.");
							return false;
						}
					}
				}
			}
		}
		
		p.sendMessage(ChatColor.RED + "There is no cannon nearby.");
		return false;
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
		c.health = Math.max(0, c.health-amt);
	}
	
	private boolean hasCannonPermission(Player p) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean validCannonSpot(Location loc, Player p) {
		
		if(onSolidGround(loc)) {
			
		}else {
			p.sendMessage(ChatColor.RED + "This cannon isn't on solid ground.");
			p.sendMessage(ChatColor.RED + "A cannon requires a 5x5x3 cube of blocks beneath it in order to fire.");
			return false;
		}
		
		if(hasClearance(loc)) {
			
		}else {
			p.sendMessage(ChatColor.RED + "You don't have enough clearance to use a cannon here.");
			p.sendMessage(ChatColor.RED + "A cannon requires a 9x9x9 cube to be clear of blocks surrounding it.");
			return false;
		}
		
		return true;
	}
	
	public boolean hasClearance(Location loc) {
		Location ground = loc.clone().add(new Vector(0,0,0));
		Location corner = ground.clone().add(new Vector(-4,0,-4));
		int x = corner.getBlockX();
		int y = corner.getBlockY();
		int z = corner.getBlockZ();
		World world = ground.getWorld();
		for(int tx = 0; tx < 9; tx++) {
			for(int tz = 0; tz < 9; tz++) {
				for(int ty = 0; ty < 9; ty++) {
					Block b = world.getBlockAt(new Location(world,x+tx,Math.max(1,y+ty),z+tz));
					Material m = b.getType();
					if(m != Material.AIR) {
						Bukkit.getServer().getLogger().info("Cannon failed, solid block at " + b.getLocation()); 
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean hasClearanceToFire(Location loc) {
		Location ground = loc.clone().add(new Vector(0,0,0));
		Location corner = ground.clone().add(new Vector(-4,0,-4));
		int x = corner.getBlockX();
		int y = corner.getBlockY();
		int z = corner.getBlockZ();
		World world = ground.getWorld();
		int blockCount = 0;
		for(int tx = 0; tx < 9; tx++) {
			for(int tz = 0; tz < 9; tz++) {
				for(int ty = 0; ty < 9; ty++) {
					Block b = world.getBlockAt(new Location(world,x+tx,Math.max(1,y+ty),z+tz));
					Material m = b.getType();
					if(m != Material.AIR) {
						if(m == Material.PACKED_ICE || m == Material.SPRUCE_STAIRS || m == Material.SPRUCE_WOOD
								|| m == Material.LEVER || m == Material.STONE_BUTTON || m == Material.SPRUCE_TRAPDOOR || m == Material.SPRUCE_LOG) {
							blockCount += 1;
							if(blockCount >= 15) {
								Bukkit.getServer().getLogger().info("Cannon failed, too many solid block at " + b.getLocation()); 
								return false;
							}
						}else {
							Bukkit.getServer().getLogger().info("Cannon failed, solid block at " + b.getLocation()); 
							return false;
						}

					}
				}
			}
		}
		return true;
	}
	
	public boolean onSolidGround(Location loc) {
		Location ground = loc.clone().add(new Vector(0,-1,0));
		Location corner = ground.clone().add(new Vector(-2,-2,-2));
		World world = ground.getWorld();
		int x = corner.getBlockX();
		int y = corner.getBlockY();
		int z = corner.getBlockZ();
		for(int tx = 0; tx < 5; tx++) {
			for(int tz = 0; tz < 5; tz++) {
				for(int ty = 0; ty < 3; ty++) {
					Block b = world.getBlockAt(new Location(world,x+tx,Math.max(1,y+ty),z+tz));
					Material m = b.getType();
					if(m == Material.AIR || m == Material.WATER) {
						Bukkit.getServer().getLogger().info("Cannon failed, air block at " + b.getLocation()); 
						return false;
					}
				}
			}
		}
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
	
	public void spawnCannon(Location loc, Estate e, Vector v) throws WorldEditException {
		//loc.getWorld().getBlockAt(loc).setType(Material.PACKED_ICE);
		Location loc2 = loc.add(new Vector(0,1,0));
		Block b = loc2.getBlock();
		
		Clipboard schem = Nobility.getSchematic("cannon");
        AffineTransform transform = getRotatedTransform(v);
        
        World world = loc.getWorld();
        ClipboardHolder clipHolder = new ClipboardHolder(schem);
        clipHolder.setTransform(transform);
		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
		    Operation operation = clipHolder
		            .createPaste(editSession)
		            .to(BlockVector3.at(loc.getBlockX(), loc.getBlockY()-2, loc.getBlockZ()))
		            // configure here
		            .build();
		    Operations.complete(operation);
		}
        
		Cannon c = new Cannon(b,e);
		this.activeCannons.add(c);
		this.cannonCooldowns.put(c,System.currentTimeMillis());
		//TODO implement use up cannons here.
		
	}
	
	private AffineTransform getRotatedTransform(Vector v) {
		// TODO Auto-generated method stub
		AffineTransform transform = new AffineTransform();
		v = v.clone();
		
		double vx = Math.abs(v.getX());
		double vz = Math.abs(v.getZ());
		
		if(vx > vz) {
			if(v.getX() > 0) {
				//the schematic is facing east already
			}else {
				transform = transform.rotateY(180);
			}
		}else {
			if(v.getZ() > 0) {
				transform = transform.rotateY(-90);
			}else {
				transform = transform.rotateY(90);
			}
		}
		
		
		return transform;
	}

	public void summonCannon(Player p) {
		Estate e = Nobility.getEstateManager().getEstateOfPlayer(p);
		if(e != null) {
			Location loc = p.getLocation();
			if(canPlaceCannons(e)) {
				if(hasCannonPermission(p)) {
					if(hasCannons(e)) {
						if(validCannonSpot(loc,p)) {
							try {
								spawnCannon(loc,e,p.getEyeLocation().getDirection());
							} catch (WorldEditException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							AttributeManager.spendCannon(e);
							p.sendMessage(ChatColor.GREEN + "Summoned cannon successfully.");
							e.getGroup().announce(ChatColor.WHITE + p.getName() + ChatColor.GREEN + " has summoned a cannon at:" 
									+ ChatColor.WHITE + " [" + loc.getBlockX() + "x, " + loc.getBlockY() + "y, " + loc.getBlockZ() + " z]");
						}else {
							//p.sendMessage(ChatColor.RED + "This is not a valid cannon location");
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

		Vector norm = fire.normalize();
		World world = p.getWorld();
		Location lc = c.block.getLocation();
		Location loc = lc.add(new Vector(0.5,0.5,0.5));
		
		world.playSound(c.block.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 2, 0.8f);
		world.spawnParticle(Particle.FLAME, c.block.getLocation().clone().add(new Vector(0,0.5,0)), 5, 0.2, 0.1, 0.2, 0.2);
		
		long time = System.currentTimeMillis();
		cannonCooldowns.put(c, time);
		playerCooldowns.put(p.getUniqueId(), time);
		
		playFireStorm(loc,40);
		damageNearbyEstate(loc,5,p);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
		    @Override
		    public void run() {
				world.playSound(c.block.getLocation(), Sound.ENTITY_TNT_PRIMED, 6, 1);
		    }
		}, 4L); //20 Tick (1 Second) delay before run() is called
		
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
	
    public void playFireStorm(final Location location, int duration) {
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
		}, duration); //20 Tick (1 Second) delay before run() is called
				
    	
    }


}
