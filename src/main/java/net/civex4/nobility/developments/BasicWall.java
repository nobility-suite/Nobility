package net.civex4.nobility.developments;

import org.bukkit.Material;

import net.civex4.nobility.development.DevAttribute;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class BasicWall extends Development{

	public BasicWall() {
		super(DevelopmentType.GENERIC);
		this.isActive = true;
		this.name = "Wooden Wall";
		this.icon = Material.DARK_OAK_FENCE;
		this.buildDescription = "Walls increase your City HP, making you more protected from Siege.";
		this.useDescription = "Walls increase your City HP, making you more protected from Siege.";
		this.attributes.put(DevAttribute.CITY_HEALTH, 1000);
		// TODO Auto-generated constructor stub
	}
}
