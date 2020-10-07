package net.civex4.nobility.listeners;

import net.civex4.nobility.group.Group;
import net.civex4.nobility.group.GroupPermission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.AttributeManager;
import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import vg.civcraft.mc.civmodcore.api.BlockAPI;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProtectionListener implements Listener{
	
	final static String BLOCK_PREVENTED_MSG = ChatColor.RED + "You cannot build within the city limits of ";

	public boolean isPermittedPlayerNear(Block block, double distance) {
		Location location = block.getLocation();
		Estate estate = Nobility.getEstateManager().getNearestEstate(location);

		double diameter = distance;
		Collection<Entity> entities = location.getWorld().getNearbyEntities(location, diameter, diameter, diameter);
		for(Entity entity : entities) {
			if(entity instanceof Player) {
				Player nearPlayer = (Player) entity;
				if (Nobility.getEstateManager().getEstateOfPlayer(nearPlayer) == estate) {
					return true;
				}
				nearPlayer.sendMessage(ChatColor.RED + "Nobility blocked that action.");
			}
		}
		return false;
	}

	public boolean withinSquare(int radius, Location target, Location current) {
		int tarx = target.getBlockX();
		int tarz = target.getBlockZ();
		
		int curx = current.getBlockX();
		int curz = current.getBlockZ();

		if(Math.abs(tarx-curx) <= radius && Math.abs(tarz-curz) <= radius) {
			return true;
		}
		
		return false;
	}
	
	@EventHandler
	public void breakPrevent(BlockBreakEvent event){
		Location loc = event.getBlock().getLocation();
		Estate e = Nobility.getEstateManager().getNearestEstate(loc);
		if(e == null) { return; }
		
		int radius = AttributeManager.getCityLimit(e);
		
		if(!withinSquare(radius,e.getBlock().getLocation(),loc)) { return; }
		
		Player p = event.getPlayer();
		Group g = Nobility.getGroupManager().getGroup(p);
		if(g == null) {
			event.setCancelled(true);
			p.sendMessage(BLOCK_PREVENTED_MSG + ChatColor.WHITE + e.getGroup().getName());
			return;
		}
		GroupPermission perm = g.getPermission(p);
		if(Nobility.getEstateManager().getEstateOfPlayer(p) == e) {
			if(perm == GroupPermission.DEFAULT) {
				event.setCancelled(true);
				p.sendMessage(BLOCK_PREVENTED_MSG + ChatColor.WHITE + e.getGroup().getName());
			}
			//remove this after debugging
			return;
		}else { 
			event.setCancelled(true); 
			p.sendMessage(BLOCK_PREVENTED_MSG + ChatColor.WHITE + e.getGroup().getName()); 
		}
	}
	
	@EventHandler 
	public void placePrevent(BlockPlaceEvent event){
		Location loc = event.getBlock().getLocation();
		Estate e = Nobility.getEstateManager().getNearestEstate(loc);
		if(e == null) { return; }
		
		int radius = AttributeManager.getCityLimit(e);
		
		if(!withinSquare(radius,e.getBlock().getLocation(),loc)) { return; }
		
		Player p = event.getPlayer();
		Group g = Nobility.getGroupManager().getGroup(p);
		if(g == null) {
			event.setCancelled(true);
			p.sendMessage(BLOCK_PREVENTED_MSG + ChatColor.WHITE + e.getGroup().getName());
			return;
		}
		GroupPermission perm = g.getPermission(p);
		if(Nobility.getEstateManager().getEstateOfPlayer(p) == e) {
			if(perm == GroupPermission.DEFAULT) {
				event.setCancelled(true);
				p.sendMessage(BLOCK_PREVENTED_MSG + ChatColor.WHITE + e.getGroup().getName());
			}
			return;
		}else { 
			event.setCancelled(true); 
			p.sendMessage(BLOCK_PREVENTED_MSG + ChatColor.WHITE + e.getGroup().getName()); 
		}
	}

	@EventHandler
	public void pistonCheck(BlockPistonExtendEvent event) {
		Location loc = event.getBlock().getLocation();
		Estate e = Nobility.getEstateManager().getNearestEstate(loc);
		//Estate Check, that way we don't throw NPE's
		if(e == null) { return; }

		int radius = AttributeManager.getCityLimit(e) + 15; //+15 Meter Limit.

		if(!withinSquare(radius, e.getBlock().getLocation(), loc)) {
			//Check to see if piston is within Radius deemed "Bad"
			return;
		}

		//Just, Does the job until I can figure out a better way to do it. Wish I could permit Pistons within estate fields
		//but, thats not looking like a possibility.
		event.setCancelled(true);

	}

	@EventHandler
	public void bucketUse(PlayerBucketEmptyEvent event) {
		Location loc = event.getBlock().getLocation();
		Estate e = Nobility.getEstateManager().getNearestEstate(loc);
		if(e == null) { return; }

		int radius = AttributeManager.getCityLimit(e) + 15; //+15 Meter Limit.

		if(!withinSquare(radius,e.getBlock().getLocation(),loc)) {
			//Are u within city radius?
			return;
		}

		Player p = event.getPlayer();
		if (Nobility.getEstateManager().getEstateOfPlayer(p) == e) {
			//IS player a part of estate???
			return;
		} else {
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You cannot use, BUCKET. Within other estates.");
		}
	}

	@EventHandler
	public void ignitionPrevention(BlockIgniteEvent event) {
		Location loc = event.getBlock().getLocation();
		Estate e = Nobility.getEstateManager().getNearestEstate(loc);
		if(e == null) { return; }

		int radius = AttributeManager.getCityLimit(e) + 15; //+15 Meter Limit.

		if(!withinSquare(radius,e.getBlock().getLocation(),loc)) {
			//Are u within city radius?
			return;
		}

		Player p = event.getPlayer();
		if (Nobility.getEstateManager().getEstateOfPlayer(p) == e) {
			//IS player a part of estate???
			return;
		} else {
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You cannot use, FIRE. Within other estates.");
		}
	}


	public void restoneDoors(Block block, BlockFace skip) {
		for (Map.Entry<BlockFace, Block> entry : BlockAPI.getAllSidesMapped(block).entrySet()) {
			if (entry.getKey() == skip) {
				continue;
			}
			Block rel = entry.getValue();
			BlockData blockData = rel.getBlockData();
			if (!(blockData instanceof Openable)) {
				continue;
			}
			Location locationToSave;
			if (blockData instanceof Door) {
				Door door = (Door) blockData;
				if(door.getHalf() == Bisected.Half.TOP) {
					// block is upper half of a door
					locationToSave = rel.getRelative(BlockFace.DOWN).getLocation();
				} else {
					//doublecheck
					locationToSave = rel.getLocation();
				}
			} else {
				locationToSave = rel.getLocation();
			}

		}
	}

	@EventHandler
	public void button(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if(!(e.getClickedBlock().getBlockData() instanceof Switch)) {
			return;
		}
		Switch button = (Switch) (e.getClickedBlock().getBlockData());
		Block buttonBlock = e.getClickedBlock();
		Block attachedBlock = e.getClickedBlock().getRelative(button.getFacing().getOppositeFace());
		//PREPARE ALL SIDES FOR CHECK
		restoneDoors(buttonBlock, button.getFacing().getOppositeFace());
		//PREPARE ALL SIDES OF THE BLOCK ATTACHED TO
		restoneDoors(attachedBlock, button.getFacing());
	}

	@EventHandler
	public void redstonePower(BlockRedstoneEvent event) {
		if (event.getNewCurrent() <= 0 || event.getOldCurrent() > 0) {
			return;
		}

		Location loc = event.getBlock().getLocation();
		Estate e = Nobility.getEstateManager().getNearestEstate(loc);
		if(e == null) { return; }

		int radius = AttributeManager.getCityLimit(e); //No Limit Extension, this should be ONLY within city walls.

		if(!withinSquare(radius, e.getBlock().getLocation(), loc)) {
			return;
		}

		Block block = event.getBlock();
		BlockData blockData = block.getBlockData();
		if(!(blockData instanceof Openable)) {
			return;
		}
		Openable openable = (Openable) blockData;
		if (openable.isOpen()) {
			return;
		}
		if(blockData instanceof Door) {
			Door door = (Door) blockData;
			if(door.getHalf() == Bisected.Half.TOP) {
				block = block.getRelative(BlockFace.DOWN);
			}
		}

		boolean playerNearby = isPermittedPlayerNear(block, 2);
		if(!playerNearby) {
			event.setNewCurrent(event.getOldCurrent());
		}
		return;
	}

	@EventHandler
	public void doorPrevention(PlayerInteractEvent event) {
		if(!event.hasBlock()) {
			return;
		}
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Location loc = event.getClickedBlock().getLocation();
		Estate e = Nobility.getEstateManager().getNearestEstate(loc);
		if(e == null) { return; }

		if (Nobility.getEstateManager().getEstateOfPlayer(event.getPlayer()) == e) {
			return;
		}

		int radius = AttributeManager.getCityLimit(e); //No Limit Extension, this should be ONLY within city walls.

		if(!withinSquare(radius, e.getBlock().getLocation(), loc)) {
			return;
		}

		Block block = event.getClickedBlock();
		if(block.getState().getBlockData() instanceof Container) {
			event.setCancelled(true);
			event.getPlayer().closeInventory();
			String msg = String.format("%s is locked.", event.getClickedBlock().getType().name());
			event.getPlayer().sendMessage(ChatColor.RED + msg);
		}
		if(block.getState().getBlockData() instanceof Openable) {
			event.setCancelled(true);
			String msg = String.format("%s is locked.", event.getClickedBlock().getType().name());
			event.getPlayer().sendMessage(ChatColor.RED + msg);
		}
		if(block.getType() == Material.BEACON) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Beacon is locked.");
		}
		Material mat = block.getType();
		if(mat == Material.STONE_PRESSURE_PLATE || mat == Material.LIGHT_WEIGHTED_PRESSURE_PLATE || mat == Material.HEAVY_WEIGHTED_PRESSURE_PLATE || Tag.WOODEN_PRESSURE_PLATES.isTagged(mat)) {
			restoneDoors(block, BlockFace.EAST_SOUTH_EAST);
		}
		if(mat == Material.CHEST || mat == Material.TRAPPED_CHEST || mat == Material.HOPPER || mat == Material.BARREL || mat == Material.FURNACE || mat == Material.ENDER_CHEST || mat == Material.ITEM_FRAME || mat == Material.SMOKER || mat == Material.BLAST_FURNACE) {
			event.setCancelled(true);
			event.getPlayer().closeInventory();
			String msg = String.format("%s is locked.", event.getClickedBlock().getType().name());
			event.getPlayer().sendMessage(ChatColor.RED + msg);
		}
		return;
	}


}
