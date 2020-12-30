package net.civex4.nobility.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.AttributeManager;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.group.GroupPermission;
import vg.civcraft.mc.civmodcore.api.ItemAPI;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;

public enum ButtonLibrary {
	HOME (
		new Clickable(createIcon(Material.PAPER, "Home")) {
			@Override
			public void clicked(Player p) {
				Nobility.getEstateGui().openEstateGUI(p);
			}			
		}),
	;
	
	private final Clickable clickable;
	
	ButtonLibrary(Clickable clickable) {
		this.clickable = clickable;
	}
	
	public Clickable clickable() { return clickable; }
	public ItemStack icon() { return clickable.getItemStack(); }
	
	
	public static ItemStack createIcon(Material mat, String name) {
		ItemStack icon = new ItemStack(mat);
		ItemAPI.setDisplayName(icon, ChatColor.WHITE + name);
		return icon;
	}
	
	public static Clickable createResearchTutorial() {
		ItemStack icon = createIcon(Material.BOOK, ChatColor.WHITE + "Research Tutorial");
		ItemAPI.addLore(icon, ChatColor.GRAY + "Choose a card to progress your Blueprint.");
		ItemAPI.addLore(icon, ChatColor.GRAY + "Your choices allow you to influence the final outcome.");
		ItemAPI.addLore(icon, ChatColor.GRAY + "Each round you will be shown a new set of cards.");
		Clickable ret = new DecorationStack(icon);
		return ret;
	}
	
	public static Clickable createWorkshopInfo(Development d) {
		ItemStack icon = createIcon(Material.BOOK, "Workshop Info");
		ItemAPI.addLore(icon, ChatColor.BLUE + "Workshop Type: " +  ChatColor.WHITE + d.name, ChatColor.GRAY + d.useDescription);
		Clickable ret = new DecorationStack(icon);
		return ret;
	}
	
	public static Clickable createWorkerInfo(Player p) {
		ItemStack playerIcon = ButtonLibrary.createIcon(Material.PLAYER_HEAD, p.getName());
		int workers = Nobility.getWorkerManager().getWorkers(p);
		playerIcon.setAmount(workers);
		SkullMeta im = (SkullMeta) ItemAPI.getItemMeta(playerIcon);
		im.setOwningPlayer(p);
		playerIcon.setItemMeta(im);
		ItemAPI.addLore(playerIcon, ChatColor.BLUE + "Workers: " + ChatColor.WHITE + workers,
				ChatColor.BLUE + "Activity Level: " + ChatColor.WHITE + "" + Nobility.getWorkerManager().getActivityLevel(p),
				ChatColor.GRAY + "Your activity level determines how many",
				ChatColor.GRAY + "Workers you recieve per day.");
		Clickable pcon = new DecorationStack(playerIcon);
		return pcon;
	}
	
	public static Clickable createEstateInfo(Estate e) {
		ItemStack info = ButtonLibrary.createIcon(Material.BOOK, ChatColor.GOLD + e.getGroup().getName());
		ItemAPI.addLore(info, ChatColor.BLUE + "Members: " + ChatColor.WHITE + "" + e.getGroup().getMembers().size(),
				ChatColor.BLUE + "Leader: " + ChatColor.WHITE + "" + e.getGroup().getLocalization(GroupPermission.LEADER) + " " + e.getGroup().getLeader().getName(),
				ChatColor.BLUE + "Region: " + ChatColor.WHITE + e.getRegion().getName(),
				ChatColor.BLUE + "Location: " + ChatColor.WHITE + e.getBlock().getX() + "X, " + e.getBlock().getZ() + "Z",
				ChatColor.BLUE + "Vulnerability Hour: " + ChatColor.WHITE + e.getVulnerabilityHour());
		int radius = AttributeManager.getCityLimit(e);
		ItemAPI.addLore(info, ChatColor.BLUE + "City Radius: " + ChatColor.WHITE + radius);
		Clickable infoIcon = new DecorationStack(info);
		return infoIcon;
	}
}
