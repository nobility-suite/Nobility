package net.civex4.nobility.customItem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/** 
 * Creates an instance of a custom item if a splash potion breaks
 * */

public class BottleBreak implements Listener {

	@EventHandler
	public void onBottleBreak(ProjectileHitEvent event) {
		if (!event.getEntity().getType().equals(EntityType.SPLASH_POTION)) return;
		Block block = event.getHitBlock();
		Location loc = block.getLocation().add(0, 1, 0);
		CustomItem customItem = CustomItem.getFactory().createItem(Material.GLASS, "Broken Glass");
		loc.getWorld().dropItemNaturally(loc, customItem.getItem());
	}

}
