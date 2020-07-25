package net.civex4.nobility.developments;

import org.bukkit.Material;

import net.civex4.nobility.development.DevAttribute;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class CommunityWell extends Development{

	public CommunityWell() {
		super(DevelopmentType.GENERIC);
		this.isActive = true;
		this.name = "Community Well";
		this.icon = Material.CAULDRON;
		this.buildDescription = "Increases the radius of your city.";
		this.useDescription = "Increases the radius of your city.";
		this.attributes.put(DevAttribute.CITY_RADIUS, 50);
		// TODO Auto-generated constructor stub
	}
}
