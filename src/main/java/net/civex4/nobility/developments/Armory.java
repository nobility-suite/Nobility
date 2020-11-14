package net.civex4.nobility.developments;

import net.civex4.nobility.development.DevAttribute;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobilityitems.NobilityItem;
import org.bukkit.Material;

import java.util.HashMap;

public class Armory extends Development {

	public NobilityItem upgradeItem;

	public Armory() {
		super(DevelopmentType.ARMORY);
		this.icon = Material.IRON_BLOCK;
		this.name = "Armory";
		this.isActive = true;
		this.buildDescription = "Allows you to use more heavy duty combat equipment.";
		this.useDescription = "Provides power and integrity to heavy duty combat equipment.";
		this.attributes = new HashMap<DevAttribute, Integer>();
		this.attributes.put(DevAttribute.ARMORY_LEVEL, 1);
	}
}
