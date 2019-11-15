package net.civex4.nobility.development;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;
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
	
	private double productivity; // add to collector
	private int collectionPower; // add to collector
	private boolean isActive;
		
	public Development(DevelopmentType development) {
		this.setDevelopmentType(development);
		this.productivity = .4d;
		this.setCollectionPower(10);
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
		
		// INFO
		// TODO Add information. Maybe behavior.getInfo()
		Clickable info = new DecorationStack(new ItemStack(Material.PAPER));
		gui.addSlot(info);
		
		// ACTIVATE / DEACTIVATE
		// This is kind of convoluted. Maybe should use that BooleanButton...
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
				inventory.setSlot(this, 1);
				p.updateInventory();
			}			
		};
		gui.addSlot(activate);

		/*
		for (DevelopmentBehavior behavior : behaviors) {
			
			for (Clickable c : behavior.getClickables()) {
				gui.addSlot(c);
			};
		}*/
		
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

	public double getProductivity() {
		return productivity;
	}

	public void setProductivity(double productivity) {
		this.productivity = productivity;
	}

	public int getCollectionPower() {
		return collectionPower;
	}
	
	public void setCollectionPower(int collectionPower) {
		this.collectionPower = collectionPower;
	}

	public List<DevelopmentBehavior> getBehaviors() {
		return behaviors;
	}


	public void addBehavior(DevelopmentBehavior behavior) {
		behaviors.add(behavior);
	}


}
