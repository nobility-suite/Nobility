package net.civex4.nobility.developments;

import org.bukkit.Material;

import io.github.kingvictoria.nodes.NodeType;
import net.civex4.nobility.development.Camp;

public class FarmingCamp extends Camp{

	public FarmingCamp() {
		super();
		this.isActive = true;
		this.nodeType = NodeType.FARM;
		this.name = "Farming Camp";
		this.icon = Material.HAY_BLOCK;
		this.buildDescription = "The Farming Camp allows you to claim Farm nodes.";
		this.useDescription = "Use this building to allocate workers to your farms.";
		
		// TODO Auto-generated constructor stub
	}

}
