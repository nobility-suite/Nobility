package net.civex4.nobility.developments;

import org.bukkit.Material;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class LumberCamp extends Development{
	
	public LumberCamp() {
		super(DevelopmentType.CAMP);
		this.isActive = true;
		this.name = "Lumber Camp";
		this.icon = Material.OAK_LOG;
		this.buildDescription = "The Lumber Camp allows you to claim Forest nodes.";
		this.useDescription = "Use this building to allocate workers to your forests.";
	}

}
