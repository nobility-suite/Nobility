package net.civex4.nobility.development;

import io.github.kingvictoria.regions.nodes.NodeType;

public class Camp extends Development{
	private int nodeLimit;
	public NodeType nodeType;
	
	public Camp() {
		super(DevelopmentType.CAMP);
		this.nodeLimit = 1;
	}
	
	public void setNodeLimit(int i) {
		this.nodeLimit = i;
	}
	
	public int getNodeLimit() {
		return this.nodeLimit;
	}

}
