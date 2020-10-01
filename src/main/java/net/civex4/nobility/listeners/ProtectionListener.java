package net.civex4.nobility.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.AttributeManager;
import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;

public class ProtectionListener implements Listener{
	
	final static String BLOCK_PREVENTED_MSG = ChatColor.RED + "You cannot build within the city limits of ";

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
		if(Nobility.getEstateManager().getEstateOfPlayer(p) == e) {
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
		if(Nobility.getEstateManager().getEstateOfPlayer(p) == e) {
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
	
	
}
