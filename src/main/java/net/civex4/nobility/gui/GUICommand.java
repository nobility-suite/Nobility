package net.civex4.nobility.gui;

import org.bukkit.DyeColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;

public class GUICommand implements CommandExecutor{
		
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//This command is for when I need to debug something
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to use this command!");
			return false;
		}
		
		Player p = (Player) sender;
		if (args.length == 0) {
			return false;
		}
		ClickableInventory inventory = new ClickableInventory(9 * args.length, "Test");
		for (int i = 0; i < args.length; i++) {
			DyeColor background;
			DyeColor text;
			switch (i) {
				case 0:
					background = DyeColor.GREEN;
					text = DyeColor.WHITE;
					break;
				case 1:
					background = DyeColor.RED;
					text = DyeColor.YELLOW;
					break;
				case 2:
					background = DyeColor.YELLOW;
					text = DyeColor.BLACK;
					break;
				default:
					background = DyeColor.BLACK;
					text = DyeColor.WHITE;
					
			}
			String arg = args[i];
			ItemStack[] banners = BannerLetter.createBanners(arg, background, text);
			for (int j = 0; j < banners.length; j++) {
				ItemStack item = banners[j];
				Clickable c = new DecorationStack(item);
				inventory.setSlot(c, (i * 9) + j);
			} 
		}
		
		inventory.showInventory(p);
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
