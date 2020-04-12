package net.civex4.nobility.developments;

import org.bukkit.Material;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class MiningCamp extends Development{

	public MiningCamp() {
		super(DevelopmentType.CAMP);
		this.isActive = true;
		this.name = "Mining Camp";
		this.icon = Material.IRON_PICKAXE;
		
		// TODO Auto-generated constructor stub
	}

}
