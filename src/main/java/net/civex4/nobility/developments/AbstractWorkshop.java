package net.civex4.nobility.developments;

import org.bukkit.Location;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class AbstractWorkshop extends Development {
	public Location inputChest;
	public Location outputChest;

	
	public AbstractWorkshop(DevelopmentType type) {
		super(type);

	}

}
