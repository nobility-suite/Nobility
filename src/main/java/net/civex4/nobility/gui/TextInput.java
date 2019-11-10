package net.civex4.nobility.gui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

/* This is all convoluted and probably more complicated than it needs to be.
 * Not sure what to do.
 */

public class TextInput {
	
	// TODO Handle case of player logging out
	private static HashMap<UUID, TextInput> playerTyping = new HashMap<>();
	private static HashMap<UUID, String> lastInput = new HashMap<>();
	private static HashMap<UUID, ClickableInventory> playersInventory = new HashMap<>();
	
	private String input;
	private Player player;
	private Renamable renamable;
	
	public TextInput(Player player, Renamable renamable) {
		this.player = player;
		this.renamable = renamable;
		setPlayerTyping(player, this);		
	}
	
	public static Boolean isPlayerTyping(UUID uuid) {
		return playerTyping.containsKey(uuid);
	}
	
	public static Boolean isPlayerTyping(Player p) {
		return isPlayerTyping(p.getUniqueId());
	}
	
	public static void setPlayerTyping(UUID uuid, TextInput input) {
		playerTyping.put(uuid, input);
	}
	
	public static void setPlayerTyping(Player player, TextInput input) {
		setPlayerTyping(player.getUniqueId(), input);
	}
	
	public static TextInput getInputFromPlayer(UUID uuid) {
		return playerTyping.get(uuid);
	}
	
	public static TextInput getInputFromPlayer(Player player) {
		return getInputFromPlayer(player.getUniqueId());
	}
	
	public static TextInput removePlayerTyping(UUID uuid) {
		return playerTyping.remove(uuid);
	}
	
	public static TextInput removePlayerTyping(Player player) {
		return removePlayerTyping(player.getUniqueId());
	}

	public static String setLastInput(UUID uuid, String input) {
		return lastInput.put(uuid, input);
	}

	public static String setLastInput(Player player, String input) {
		return setLastInput(player.getUniqueId(), input);
	}
	
	public static String getLastInput(UUID uuid) {
		return lastInput.get(uuid);
	}

	public static String getLastInput(Player player) {
		return getLastInput(player.getUniqueId());
	}
	
	public static ClickableInventory getPlayersInventory(UUID uuid) {
		return playersInventory.get(uuid);
	}
	
	public static ClickableInventory getPlayersInventory(Player player) {
		return getPlayersInventory(player.getUniqueId());
	}
	
	public static ClickableInventory setPlayersInventory(UUID uuid, ClickableInventory inv) {
		return playersInventory.put(uuid, inv);
	}
	
	public static ClickableInventory setPlayersInventory(Player player, ClickableInventory inv) {
		return setPlayersInventory(player.getUniqueId(), inv);
	}
	
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public Player getPlayer() {
		return player;
	}

	public Renamable getRenamable() {
		return renamable;
	}
	
}
