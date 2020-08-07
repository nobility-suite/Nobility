package net.civex4.nobility.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.cannons.Cannon;
import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;

public class CannonListener implements Listener {
	
	public final long CANNON_COOLDOWN_FIRE_MS = 1000*60;
	
	@EventHandler
	public void onButtonPress(PlayerInteractEvent event) {
		
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }

		if(event.getClickedBlock() == null) { return; }
		
		Player p = event.getPlayer();

		Location loc = event.getClickedBlock().getLocation();
		if(event.getClickedBlock().getType() == Material.STONE_BUTTON) {
			Block b = event.getClickedBlock();
			Switch button = (Switch) b.getBlockData();
			BlockFace face = button.getFacing();
			Block behind = b.getRelative(face.getOppositeFace());
			Location loc2 = behind.getLocation();
			Cannon c = Nobility.getCannonManager().getCannon(loc2);
			if(c != null) {
				Estate e = Nobility.getEstateManager().getEstateOfPlayer(p);
				//You must be part of the cannon's estate to use the cannon
				if(c.owner == e) {
					if(c.health > (c.maxHealth/2)) {
						Block bore = c.block;
						Location bor = bore.getLocation();
						Location start = b.getLocation();
						//Get vector away from cannon
						Vector fire = bor.toVector().subtract(start.toVector());
						
						long diff = 0;
						long time = System.currentTimeMillis();
						
						//Cannon Cooldown
						if(Nobility.getCannonManager().cannonCooldowns.containsKey(c)) {
							diff =time - Nobility.getCannonManager().cannonCooldowns.get(c) ;
							int formatted = (int) (this.CANNON_COOLDOWN_FIRE_MS - diff)/1000;
							if(diff < this.CANNON_COOLDOWN_FIRE_MS) {
								p.sendMessage(ChatColor.RED + "This cannon cannot be fired again for " + ChatColor.WHITE + formatted + ChatColor.RED + " seconds." );
								event.setCancelled(true);
								return;
							}
						}
						//Player cooldown
						if(Nobility.getCannonManager().playerCooldowns.containsKey(p.getUniqueId())) {

						    diff = time - Nobility.getCannonManager().playerCooldowns.get(p.getUniqueId());
							int formatted = (int) (this.CANNON_COOLDOWN_FIRE_MS - diff)/1000;

						    if(diff < this.CANNON_COOLDOWN_FIRE_MS) {
								p.sendMessage(ChatColor.RED + "You cannot fire another cannon for " + ChatColor.WHITE + formatted + ChatColor.RED + " seconds." );
								event.setCancelled(true);
								return;
						    }
						}
		
						
						
						
						if(Nobility.getCannonManager().hasClearanceToFire(bor.clone().add(new Vector(0,-1,0)))) {
							if(Nobility.getCannonManager().onSolidGround(bor.clone().add(new Vector(0,-1,0)))) {
								Nobility.getCannonManager().fireCannon(c,p,fire);
							}else {
								p.sendMessage(ChatColor.RED + "This cannon isn't on solid ground.");
								p.sendMessage(ChatColor.RED + "A cannon requires a 5x5x3 cube of blocks beneath it in order to fire.");
							}
						}else {
							p.sendMessage(ChatColor.RED + "You don't have enough clearance to use a cannon here.");
							p.sendMessage(ChatColor.RED + "A cannon requires a 9x9x9 cube to be clear of blocks surrounding it.");
						}
					}else {
						p.sendMessage(ChatColor.RED + "This cannon is too damaged to fire!" + ChatColor.WHITE + " [" + c.health + "/" + c.maxHealth +"]");
					}
					
				}else {
					p.sendMessage(ChatColor.RED + "Your estate does not own this cannon.");
				}
			}else {
	
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Location loc = event.getBlock().getLocation();
		
		for(Cannon c : Nobility.getCannonManager().activeCannons) {
			Location to = c.block.getLocation();
			if(to.getWorld() == loc.getWorld()) {
				int distance = (int) loc.distance(to);
				if(distance < 14) {
					int x = Math.abs(loc.getBlockX() - to.getBlockX());
					int y = loc.getBlockY() - to.getBlockY();
					int z = Math.abs(loc.getBlockZ()-to.getBlockZ());
					
					if(x <= 4 && z <= 4) {
						//cancel
						if(y >= -1 && y < 10) {
							Player p = event.getPlayer();
							event.setCancelled(true);
							p.sendMessage(ChatColor.RED + "You cannot place a block within a cannon's border (9x9x9 around and above)");
							
						}
					}
					
					
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
	Location loc = event.getBlock().getLocation();
		
		for(Cannon c : Nobility.getCannonManager().activeCannons) {
			Location to = c.block.getLocation();
			if(to.getWorld() == loc.getWorld()) {
				int distance = (int) loc.distance(to);
				if(distance < 14) {
					int x = Math.abs(loc.getBlockX() - to.getBlockX());
					int y = loc.getBlockY() - to.getBlockY();
					int z = Math.abs(loc.getBlockZ()-to.getBlockZ());
					if(x <= 4 && z <= 4) {
						//cancel
						if(y >= 1 && y < 10) {
							Player p = event.getPlayer();
							event.setCancelled(true);
							p.sendMessage(ChatColor.RED + "You cannot place a block within a cannon's border (9x9x9 around and above)");
							
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Material m = event.getBlock().getType();
		
		if(m == Material.COAL_BLOCK || m == Material.SPRUCE_STAIRS || m == Material.SPRUCE_WOOD
				|| m == Material.LEVER || m == Material.STONE_BUTTON || m == Material.SPRUCE_TRAPDOOR || m == Material.SPRUCE_LOG) {
			Location loc = event.getBlock().getLocation();
			Cannon c = Nobility.getCannonManager().getCannon(loc);
			if(c == null) {
				return;
			}
			
			if(m == Material.COAL_BLOCK) {
				//TODO damage cannnon
				Nobility.getCannonManager().damageCannon(c, 1);
				World world = loc.getWorld();
				world.playSound(loc,Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1,  0.8f);
				Player p = event.getPlayer();
				p.sendMessage(ChatColor.RED + "Cannon health: " + ChatColor.WHITE + c.health);
				event.setCancelled(true);
				if(c.health == 0) {
					this.cannonDestroyAnimation();
					//TODO remove cannon
					Nobility.getCannonManager().removeCannon(c);
				
				}
				if(c.health < c.maxHealth/2) {
					this.cannonDisableAnimation();
					//TODO cannon comes back after 5 minutes?
					Bukkit.getScheduler().scheduleSyncDelayedTask(Nobility.getNobility(), new Runnable() {
					    @Override
					    public void run() {
					    	if(Nobility.getCannonManager().activeCannons.contains(c)) {
								world.playSound(loc, Sound.BLOCK_ANVIL_USE, 6, 0.8f);
								c.health = c.maxHealth;
					    	}
					    }
					}, 20*60*5L); //20 Tick (1 Second) delay before run() is called
				}
			}else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You cannot remove this cannon's blocks. If it is your cannon, use /n cannon recover.");
			}
			
		}else return;
			
	}
	
	public void cannonDisableAnimation() {
		
	}
	
	public void cannonDestroyAnimation() {
		
	}
	

}
