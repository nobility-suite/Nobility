package net.civex4.nobility.gui;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

public class GUICommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to use this command!");
			return false;
		}
		
		Player p = (Player) sender;
		
		ClickableInventory inventory = new ClickableInventory(9, "Inventory");
		ItemStack item = new ItemStack(Material.WHEAT);
		
		Clickable boolButton = new BooleanButton(new ItemStack(Material.WHEAT));
		Clickable textButton = new TextInputButton(item, new TestObject("Wheat", item));
		inventory.setSlot(boolButton, 0);
		inventory.setSlot(textButton, 1);
		inventory.showInventory(p);	
		
		return true;
	}

}
