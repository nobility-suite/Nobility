package net.civex4.nobility.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.civex4.nobility.Nobility;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

public class TextInputListener implements Listener {
	//Takes new name and updates renamable and the clickable
	@EventHandler
	public void onPlayerChat (AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();		
		
		if (TextInput.isPlayerTyping(player)) {
			String input = event.getMessage();
			TextInput textInput = TextInput.getInputFromPlayer(player);
			textInput.setInput(input);
			textInput.getRenamable().setName(input);
			TextInput.setLastInput(player, input);
			TextInput.removePlayerTyping(player);
			event.setCancelled(true);
			
			//Using runnable to avoid using deprecated PlayerChatEvent
			new BukkitRunnable() {
				@Override
				public void run() {
					ClickableInventory inv = TextInput.getPlayersInventory(player);
					TextInputButton button = (TextInputButton) inv.getSlot(1);
					ItemStack newItem = button.getItemStack();
					ItemAPI.setDisplayName(newItem, ChatColor.WHITE + input);
					button.setItemStack(newItem);
					inv.setSlot(button, 1);
					inv.updateInventory();
					inv.showInventory(player);
				}				
			}.runTaskLater(Nobility.getNobility(), 1);

		}
	}
	
}
