package net.civex4.nobility.development.storehouse;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;

/* 
 * Temporary implementation of a storehouse that uses
 * a config to save it
 */
public class ConfigStorehouse implements Storehouse {
	
	public static final String STOREHOUSES = "storehouses"; // section header
	
	
	private Inventory inv;
	private UUID id;
		
	public ConfigStorehouse(Inventory inv) {
		this.inv = inv;
		this.id = UUID.randomUUID();		
	}
	
	public ConfigStorehouse(UUID id) {
		setID(id);
	}
	
	@Override
	public Inventory loadInventory() {
		FileConfiguration config = Nobility.getNobility().getConfig();
		ConfigurationSection invSection = 
				config.getConfigurationSection(STOREHOUSES + "." + id.toString());
		Set<String> slots = invSection.getKeys(false);
		Inventory inv = Bukkit.createInventory(null, slots.size());
		
		int i = 0;
		for (String slot : slots) {
			if (invSection.isItemStack(slot)) {
				inv.setItem(i, invSection.getItemStack(slot));
			}
			i++;
		}
		
		this.inv = inv;
		
		return inv;		
	}

	@Override
	public void saveInventory() {
		if (inv == null) return;
		
		FileConfiguration config = Nobility.getNobility().getConfig();
		if (!config.contains(STOREHOUSES)) {
			config.createSection(STOREHOUSES);
		}
		
		if (!config.contains(STOREHOUSES + "." + id.toString())) {
			config.createSection(STOREHOUSES + "." + id.toString());
		}
		
		int i = 0;
		for (ItemStack item : inv.getStorageContents()) {
			if (item != null) {
				config.set(STOREHOUSES + "." + id.toString() + "." + i, item);
			} else {
				config.createSection(STOREHOUSES + "." + id.toString() + "." + i);
			}
			i++;
		}
		Nobility.getNobility().saveConfig();
		
		// TODO Auto-generated method stub
		// if no config exists, create "storehouse"
	}

	@Override
	public UUID getID() {
		return id;
	}


	@Override
	public void setInventory(Inventory inv) {
		this.inv = inv;		
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}

	@Override
	public void setID(UUID id) {
		this.id = id;
		if (Nobility.getNobility().getConfig().contains(STOREHOUSES + "." + id.toString())) {
			this.loadInventory();
		} else {
			throw new IllegalArgumentException("This storehouse doesn't exist");
		}
		
	}

}
