package net.civex4.nobility.development.storehouse;

import java.util.UUID;

import org.bukkit.inventory.Inventory;

public interface Storehouse {

	Inventory loadInventory();
	void saveInventory();
	
	UUID getID();

}
