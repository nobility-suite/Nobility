package net.civex4.nobility.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
	public final long CANNON_COOLDOWN_PICKUP_MS = 1000*60*5;
	
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
				if(c.owner == e) {
					//TODO implement cannon cooldown here
					Block bore = c.block;
					Location bor = bore.getLocation();
					Location start = b.getLocation();
					
					Vector fire = bor.toVector().subtract(start.toVector());
					
					long time = System.currentTimeMillis();
					long diff = Nobility.getCannonManager().cannonCooldowns.get(c) - time;
					int formatted = (int) (diff/1000);
					if(diff < this.CANNON_COOLDOWN_FIRE_MS) {
						p.sendMessage(ChatColor.RED + "This cannon cannot be fire again for " + ChatColor.WHITE + formatted + ChatColor.RED + " seconds." );
						event.setCancelled(true);
						return;
					}
					
				    diff = Nobility.getCannonManager().playerCooldowns.get(p) - time;
					
				    if(diff < this.CANNON_COOLDOWN_FIRE_MS) {
						p.sendMessage(ChatColor.RED + "You cannot fire another cannon for " + ChatColor.WHITE + formatted + ChatColor.RED + " seconds." );
						event.setCancelled(true);
						return;
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
				if(distance < 10) {
					int x = Math.abs(loc.getBlockX() - to.getBlockX());
					int y = loc.getBlockY() - to.getBlockY();
					int z = Math.abs(loc.getBlockZ()-to.getBlockZ());
					
					if(x <= 3 || z <= 3) {
						//cancel
						Player p = event.getPlayer();
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You cannot place a block within a cannon's border (9x9x9 around and above)");
					}
					
					if(y >= 0 && y < 10) {
						Player p = event.getPlayer();
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You cannot place a block within a cannon's border (9x9x9 around and above)");
					
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
				if(distance < 10) {
					int x = Math.abs(loc.getBlockX() - to.getBlockX());
					int y = loc.getBlockY() - to.getBlockY();
					int z = Math.abs(loc.getBlockZ()-to.getBlockZ());
					
					if(x <= 4 || z <= 4) {
						//cancel
						Player p = event.getPlayer();
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You cannot place a block within a cannon's border (9x9x9 around and above)");
					}
					
					if(y >= 0 && y < 10) {
						Player p = event.getPlayer();
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You cannot place a block within a cannon's border (9x9x9 around and above)");
					
					}
				}
			}
		}
	}
	
	

}
