package net.civex4.nobility.developments;

import org.bukkit.Material;

import io.github.kingvictoria.nodes.NodeType;
import net.civex4.nobility.development.Camp;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;

public class LumberCamp extends Camp{
	
	public LumberCamp() {
		super();
		this.isActive = true;
		this.nodeType = NodeType.FOREST;
		this.name = "Lumber Camp";
		this.icon = Material.OAK_LOG;
		this.buildDescription = "The Lumber Camp allows you to claim Forest nodes.";
		this.useDescription = "Use this building to allocate workers to your forests.";
	}

}
