package net.civex4.nobility.developments;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import org.bukkit.Location;
import org.bukkit.Material;


public class Inn extends Development {
	public Location defaultSpawn;

	public Inn() {
		super(DevelopmentType.INN);
		this.isActive = true;
		this.name = "Town Inn";
		this.icon = Material.RED_BED;
		this.buildDescription = "An inn permits you to set a public bed spawn for those in your estate.";
		this.useDescription = "An inn lets you set the default bed spawn for members in your estate.";
		// TODO Auto-generated constructor stub
	}
}
