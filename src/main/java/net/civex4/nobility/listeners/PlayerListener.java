package net.civex4.nobility.listeners;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {
	//**
	//* THIS MAINLY IS FOR ALERTMODE BEFORE SIEGES. THANKS
	//**

	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
		if (estate == null) {
			int onlinePlayers = Bukkit.getOnlinePlayers().size();
			player.sendTitle(ChatColor.GOLD + "Welcome back " + name, ChatColor.GRAY + "There are " + onlinePlayers + " players online. "
					+ "Use " + ChatColor.AQUA + "/helpop " + ChatColor.GRAY + "for help.", 10, 70, 20);
			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1 ,1);
		}
		boolean alert = estate.getAlert();

		if (alert == true) {
			player.sendTitle(ChatColor.GOLD + "Welcome back " + name, ChatColor.RED + "Your estate is going to be sieged soon...", 10, 70, 20);
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
			player.sendMessage(ChatColor.RED + "Your city has been sabotaged, and a siege is imminent...");
		} else {
			int onlinePlayers = Bukkit.getOnlinePlayers().size();
			player.sendTitle(ChatColor.GOLD + "Welcome back " + name, ChatColor.GRAY + "There are " + onlinePlayers + " players online. "
					+ "Use " + ChatColor.AQUA + "/helpop " + ChatColor.GRAY + "for help.", 10, 70, 20);
			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1 ,1);
		}
	}
}
