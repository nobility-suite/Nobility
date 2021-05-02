package net.civex4.nobility.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.inventory.items.ItemUtils;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

public class BooleanButton extends Clickable {
	
	// This button doesn't really work, but I haven't deleted it so I can refer to it as a template
	
	boolean state;
	
	public BooleanButton(ItemStack item) {
		super(item);	
		this.state = false;		
	}

	@Override
	public void clicked(Player player) {
		//Default Implementation:
		state = !state;
		ItemStack is = new ItemStack(item.getType());
		ItemUtils.addLore(is, "State: " + state);
		item = is;
		ClickableInventory inventory = ClickableInventory.getOpenInventory(player);
		inventory.setSlot(this, 0);
		player.updateInventory();
		//Create a subclass/anonymous class to connect with real variables
	}
}
