package net.civex4.nobility.developments;

import org.bukkit.Material;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class SiegeWorkshop extends Development{

	public SiegeWorkshop() {
		super(DevelopmentType.WORKSHOP);
		this.icon = Material.CRAFTING_TABLE;
		this.name = "Siege Workshop";
		this.isActive = true;
		this.buildDescription = "Allows you to build Siege Equipment, used for attacking other cities.";
		this.useDescription = "Crafts Siege Equipment and other offensive weapons.";
	}

}
