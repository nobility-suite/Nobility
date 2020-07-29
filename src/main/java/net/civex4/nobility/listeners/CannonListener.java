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
import org.bukkit.event.player.PlayerInteractEvent;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.cannons.Cannon;
import net.civex4.nobility.cannons.CannonManager;
import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;

public class CannonListener implements Listener {
	
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
					Nobility.getCannonManager().fireCannon(c,p);
				}else {
					p.sendMessage(ChatColor.RED + "Your estate does not own this cannon.");
				}
			}else {
	
			}
		}
	}

}
