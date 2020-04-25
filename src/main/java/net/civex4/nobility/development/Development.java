package net.civex4.nobility.development;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.behaviors.Collector;
import net.civex4.nobility.development.behaviors.DevelopmentBehavior;
import net.civex4.nobility.development.behaviors.Storehouse;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.gui.ButtonLibrary;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.api.ItemNames;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

public abstract class Development {
	private DevelopmentType type;
	
	//private List<DevelopmentBehavior> behaviors = new ArrayList<>();
	
	public boolean isActive;
	public String name;
	public Material icon;
	private String useDescription;
	private String buildDescription;
	
	public Development(DevelopmentType type) {
		this.type = type;
		this.isActive = true;
	}
	
//	public void build() {
//
//	}
//	
//	public void openGUI(Player player) {
//		ClickableInventory gui = new ClickableInventory(9, this.getType().getTitle());
//		Development development = this;
//		Estate estate = Nobility.getEstateManager().getEstateOfPlayer(player);
//		
//		// ACTIVATE / DEACTIVATE
//		int activateSlot = 0;
//		ItemStack activeIcon = new ItemStack(Material.EGG);
//		ItemAPI.setDisplayName(activeIcon, ChatColor.GREEN + "Active");
//		
//		ItemStack inactiveIcon = new ItemStack(Material.EGG);
//		ItemAPI.setDisplayName(inactiveIcon, ChatColor.RED + "Inactive");
//		
//		ItemStack initialIcon = isActive ? activeIcon : inactiveIcon;
//	
//		Clickable activate = new Clickable(initialIcon) {
//			boolean state = development.isActive();
//			@Override
//			public void clicked(Player p) {
//				state = !state;
//				development.isActive = state;				
//				item = state ? activeIcon : inactiveIcon;	
//				ClickableInventory inventory = ClickableInventory.getOpenInventory(p);
//				/* There's probably a better way to do this.
//				 * If another button is added before this, the activateSlot has to be updated
//				 */
//				inventory.setSlot(this, activateSlot);
//				inventory.showInventory(player);
//			}
//		};
//		// or this could be setSlot with some extra logic to prevent overwriting
//		gui.addSlot(activate);
//		
//		// BEHAVIORS
//		for (DevelopmentBehavior behavior : behaviors) {
//			
//			// UPGRADE
//			if (behavior instanceof Upgradable) {
//				Upgradable upgradable = (Upgradable) behavior;
//				ItemStack upgradeIcon = ButtonLibrary.createIcon(Material.ANVIL, "Upgrade");
//				if(!type.getInitialCost().isEmpty() ) {
//					ItemAPI.addLore(upgradeIcon, ChatColor.GOLD + "Cost:");
//					for(ItemStack cost : type.getInitialCost()) {
//						ItemAPI.addLore(upgradeIcon, ChatColor.GRAY + ItemNames.getItemName(cost) +  ": " + ChatColor.WHITE + cost.getAmount());
//					}
//					if(!Nobility.getDevelopmentManager().checkCosts(type, estate.getInventory())) {
//						ItemAPI.addLore(upgradeIcon, ChatColor.RED + "Not enough to upgrade");
//					}
//				}
//
//				Clickable upgrade = new Clickable(upgradeIcon) {
//
//					@Override
//					public void clicked(Player p) {
//						if(Nobility.getDevelopmentManager().checkCosts(type, estate.getInventory())) {
//							// TODO test if subtracting costs was successful before upgrading
//							Nobility.getDevelopmentManager().subtractCosts(type, estate.getInventory());
//							upgradable.upgrade();
//							p.sendMessage("You have upgraded the " 
//									+ development.getType().getTitle() 
//									+ " to level "
//									+ upgradable.getLevel());
//							
//							// update icons including info
//							openGUI(p);
//						} else {
//							p.sendMessage("You don't have enough resources");
//						}
//
//						
//					}
//				};
//				gui.addSlot(upgrade);
//				
//			}
//			
//			// EXTRA BEHAVIORS
//			for (Clickable c : behavior.getClickables()) {
//				gui.addSlot(c);
//			};
//		}
//		
//		// HOME
//		gui.addSlot(ButtonLibrary.HOME.clickable());
//		
//		gui.showInventory(player);
//	}

	public void tick() {

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
	
	public DevelopmentType getType() {
		return type;
	}

	public void setType(DevelopmentType type) {
		this.type = type;
	}


}
