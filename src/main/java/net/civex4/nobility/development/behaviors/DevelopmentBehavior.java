package net.civex4.nobility.development.behaviors;

import java.util.List;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;

public interface DevelopmentBehavior {
	
	void build();
	void tick();
	List<Clickable> getClickables();

}
