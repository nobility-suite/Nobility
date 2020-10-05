package net.civex4.nobility.development;

import io.github.kingvictoria.regions.nodes.NodeType;
import org.bukkit.Location;

public class Camp extends Development{
	private int nodeLimit;
	public NodeType nodeType;
	public Location outputChest;

	public Camp() {
		super(DevelopmentType.CAMP);
		this.nodeLimit = 1;
	}
	
	public void setNodeLimit(int i) {
		this.nodeLimit = i;
	}

	public Location getOutputChest() { return this.outputChest; }

	public void setOutputChest(Location location) { this.outputChest = location; }

	public int getNodeLimit() {
		return this.nodeLimit;
	}

}
