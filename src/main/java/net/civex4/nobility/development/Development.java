package net.civex4.nobility.development;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.development.behaviors.Collector;
import net.civex4.nobility.development.behaviors.DevelopmentBehavior;
import net.civex4.nobility.development.behaviors.Storehouse;
import net.civex4.nobility.gui.ButtonLibrary;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
/* TODO:
 * Developments need some refactoring. Either the developments
 * are composed of several different features (e.g. stores stuff,
 * collects stuff, etc.), or developments inherit from abstract
 * classes. The features could all be interfaces, and other
 * functions can see if a development is an instance of the
 * interface to check if things like "build()" or "destroy()" 
 * are applicable to it.
 */
public class Development {
	private DevelopmentType type;
	
	private List<DevelopmentBehavior> behaviors = new ArrayList<>();
	
	private boolean isActive;
		
	public Development(DevelopmentType type) {
		this.type = type;
		this.isActive = true;
	}
	
	public void build() {
		for (DevelopmentBehavior behavior : behaviors) {
			behavior.build();
		}
	}
	
	public void openGUI(Player player) {
		ClickableInventory gui = new ClickableInventory(9, this.getDevelopmentType().getTitle());
		Development development = this;
		
		// ACTIVATE / DEACTIVATE
		int activateSlot = 0;
		ItemStack activeIcon = new ItemStack(Material.EGG);
		ItemAPI.setDisplayName(activeIcon, ChatColor.GREEN + "Active");
		
		ItemStack inactiveIcon = new ItemStack(Material.EGG);
		ItemAPI.setDisplayName(inactiveIcon, ChatColor.RED + "Inactive");
		
		ItemStack initialIcon = isActive ? activeIcon : inactiveIcon;
	
		Clickable activate = new Clickable(initialIcon) {
			boolean state = development.isActive();
			@Override
			public void clicked(Player p) {
				state = !state;
				development.isActive = state;				
				item = state ? activeIcon : inactiveIcon;	
				ClickableInventory inventory = ClickableInventory.getOpenInventory(p);
				/* There's probably a better way to do this.
				 * If another button is added before this, the activateSlot has to be updated
				 */
				inventory.setSlot(this, activateSlot);
				inventory.showInventory(player);
			}
		};
		// or this could be setSlot with some extra logic to prevent overwriting
		gui.addSlot(activate);
		
		// INFO
		// TODO Add information. Maybe behavior.getInfo()

		// SPECIAL BEHAVIORS
		for (DevelopmentBehavior behavior : behaviors) {
			
			for (Clickable c : behavior.getClickables()) {
				gui.addSlot(c);
			};
		}
		
		// HOME
		gui.addSlot(ButtonLibrary.HOME.clickable());
		
		gui.showInventory(player);
	}

	public void tick() {
		for (DevelopmentBehavior behavior : behaviors) {
			behavior.tick();
		}
	}
	
	public void activate() {
		this.isActive = true;
	}
	
	public void deactivate() {
		this.isActive = false;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public DevelopmentType getDevelopmentType() {
		return type;
	}

	public void setDevelopmentType(DevelopmentType type) {
		this.type = type;
	}

	public List<DevelopmentBehavior> getBehaviors() {
		return behaviors;
	}

	public void addBehavior(DevelopmentBehavior behavior) {
		behaviors.add(behavior);
	}
	
	public Inventory getInventory() {
		if (!this.getDevelopmentType().isStorehouse()) {
			Bukkit.getLogger().warning("You cannot get the inventory of a development without an inventory");
			return null;
		}
		for (DevelopmentBehavior behavior : behaviors) {
			if (behavior instanceof Storehouse) {
				Storehouse storehouse = (Storehouse) behavior;
				return storehouse.getInventory();
			}
		}
		return null;
	}
	
	public Collector getCollector() {
		if (!this.getDevelopmentType().isCollector()) {
			Bukkit.getLogger().warning("You cannot get the collector of a development without a collector");
			return null;
		}
		for (DevelopmentBehavior behavior : behaviors) {
			if (behavior instanceof Storehouse) {
				Collector collector = (Collector) behavior;
				return collector;
			}
		}
		return null;
		
	}


}
