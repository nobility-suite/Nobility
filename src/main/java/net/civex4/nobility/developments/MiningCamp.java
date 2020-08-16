package net.civex4.nobility.developments;

import org.bukkit.Material;

import io.github.kingvictoria.regions.nodes.NodeType;
import net.civex4.nobility.development.Camp;
import net.civex4.nobility.development.DevelopmentType;

public class MiningCamp extends Camp{

	public MiningCamp() {
		super();
		this.isActive = true;
		this.nodeType = NodeType.MINE;
		this.name = "Mining Camp";
		this.icon = Material.IRON_ORE;
		this.buildDescription = "The Mining Camp allows you to claim Mine nodes.";
		this.useDescription = "Use this building to allocate workers to your mines.";
		
		// TODO Auto-generated constructor stub
	}

}
