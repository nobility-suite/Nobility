package net.civex4.nobility.developments;

import org.bukkit.Location;
import org.bukkit.Material;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class BasicWorkshop extends AbstractWorkshop{
	
	public Location inputChest;
	public Location outputChest;
	
	public BasicWorkshop() {
		super(DevelopmentType.WORKSHOP);
		this.icon = Material.CRAFTING_TABLE;
		this.name = "Workshop";
		this.isActive = true;
		this.buildDescription = "Building workshops unlocks new crafting recipes for your city.";
		this.useDescription = "Workshops can be used to craft powerful upgrades for your city and its citizens.";
	}

}
