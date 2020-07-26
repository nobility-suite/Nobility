package net.civex4.nobility.developments;

import java.util.HashMap;

import org.bukkit.Material;

import net.civex4.nobility.development.DevAttribute;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class Arsenal extends Development {
	public Arsenal() {
		super(DevelopmentType.ARSENAL);
		this.icon = Material.COAL_BLOCK;
		this.name = "Basic Arsenal";
		this.isActive = true;
		this.buildDescription = "An Arsenal allows you to store and maintain Siege Equipment.";
		this.useDescription = "Stores and maintains Siege Equipment.";
		this.attributes = new HashMap<DevAttribute, Integer>();
		this.attributes.put(DevAttribute.CANNON_LIMIT,5);
	}
}
