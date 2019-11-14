package net.civex4.nobility.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

public class TextInputButton extends Clickable {
	
	private TextInput textInput;
	public Renamable renamable;
	
	public TextInputButton(ItemStack item, Renamable renamable) {
		super(item);
		this.renamable = renamable;
	}
	
	

	@Override
	public void clicked(Player p) {
		textInput = new TextInput(p, renamable);
		TextInput.setPlayersInventory(p, ClickableInventory.getOpenInventory(p));
		p.closeInventory();
		p.sendMessage("Enter in a new name:");		
	}
	
	public String getTextInput() {
		return textInput.getInput();
	}
	
	public void setItemStack(ItemStack item) {
		this.item = item;
	}

}
