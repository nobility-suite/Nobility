package net.civex4.nobility.gui;

import org.bukkit.inventory.ItemStack;

public class TestObject implements Renamable {
	//Example of a Renamable
	
	String name;
	ItemStack item;
	
	public TestObject(String name, ItemStack item) {
		this.name = name;
		this.item = item;
		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}

}
