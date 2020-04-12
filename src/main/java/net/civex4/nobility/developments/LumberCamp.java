package net.civex4.nobility.developments;

import org.bukkit.Material;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class LumberCamp extends Development{
	
	public LumberCamp() {
		super(DevelopmentType.CAMP);
		this.isActive = true;
		this.name = "Lumber Camp";
		this.icon = Material.IRON_AXE;
	}

}
