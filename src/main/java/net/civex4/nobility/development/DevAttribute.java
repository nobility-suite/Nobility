package net.civex4.nobility.development;

public enum DevAttribute {
	
	CANNON_LIMIT("Cannon Limit"),
	CANNON_STORED("Cannons Stored"),
	CANNON_DISREPAIRED("Cannons Disrepaired"),
	CITY_BED_SPAWN("City Bed Spawn"),
	CITY_HEALTH("City Health"),
	CITY_RADIUS("City Radius"),
	ADD_CITY_HEALTH("Add City Health");
	
	public String name;
	
	DevAttribute(String name) {
		this.name = name;
	}
}
