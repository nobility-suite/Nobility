package net.civex4.nobility.development.storehouse;

import java.util.UUID;

import org.bukkit.inventory.Inventory;

/**
 * Interface for storehouse.
 * 
 * Has an inventory and a unique ID.
 * 
 * The way you do this is you store your Storehouse's id
 * with whatever class that "holds" it. When you load up
 * create an instance of a class that implements the Storehouse 
 * and set its ID to the ID you have stored and it will populate
 * the Storehouse's inventory. Save the inventory on shut down and/or
 * periodically, and whenever you set the inventory.
 * 
 * Lets you save and load to permanent storage
 */

public interface Storehouse {
	
	/**
	 * Loads the Inventory from persistent storage
	 *
	 * @return Inventory
	 */
	Inventory loadInventory();
	
	/**
	 * Puts the Inventory instance into persistent storage
	 *
	 * @return Inventory
	 */
	void saveInventory();	
	
	/**
	 * Sets the instance Inventory of a Storehouse object
	 * 
	 * This is the inventory that will get saved with 
	 * {@link Storehouse#saveInventory()}
	 *
	 * @return void
	 */
	void setInventory(Inventory inv);
	
	
	/**
	 * Gets the instance Inventory of a Storehouse object
	 * 
	 * This is the inventory that loads from 
	 * {@link Storehouse#loadInventory()}
	 *
	 * @return void
	 */
	Inventory getInventory();
	
	
	/**
	 * Gets the unique value the Storehouse is stored with
	 *
	 * @return UUID
	 */
	UUID getID();
	
	/**
	 * Gets the unique value the Storehouse is stored with
	 * 
	 * If the id already exists, the inventory is set to that ID
	 * 
	 * @throws IllegalArgumentException if you set the ID to one that
	 * does not already exist in the permanent storage 
	 * @return void
	 */
	void setID(UUID id);

}
