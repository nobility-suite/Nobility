package net.civex4.nobility.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GUICommand implements CommandExecutor{
		
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//This command is for when I need to debug something
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to use this command!");
			return false;
		}
		
		Player p = (Player) sender;
		/*
		ClickableInventory inventory = new ClickableInventory(9, "Inventory");
		ItemStack item = new ItemStack(Material.WHEAT);
		
		Clickable boolButton = new BooleanButton(new ItemStack(Material.WHEAT));
		Clickable textButton = new TextInputButton(item, new TestObject("Wheat", item));
		inventory.setSlot(boolButton, 0);
		inventory.setSlot(textButton, 1);
		inventory.showInventory(p);	*/
		
		//Nobility.getGroupManager().getGroup(p).setName("test");
		
		return true;
	}

}
