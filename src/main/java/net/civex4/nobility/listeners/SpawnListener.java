package net.civex4.nobility.listeners;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.developments.Inn;
import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class SpawnListener implements Listener {

	@EventHandler
	public void spawnListener(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
		if(estate == null) { return; }

		if(event.isBedSpawn()) { return; }
		if(event.isAnchorSpawn()) { return; }

		Location spawnLocation = null;

		List<Development> developments = estate.getBuiltDevelopments();
		for(Development d : developments) {
			if(d.getType() == DevelopmentType.INN) {
				Inn development = (Inn) d;
				if(development.defaultSpawn == null) { return; }
				spawnLocation = development.defaultSpawn;
			}
		}
		if(spawnLocation == null) { return; }
		event.setRespawnLocation(spawnLocation);
		player.sendMessage(ChatColor.GREEN + "No default bedspawn was found, so you were respawned at your town home.");
	}
}
