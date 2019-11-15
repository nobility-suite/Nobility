package net.civex4.nobility.development;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;

public class DevelopmentFactory {
	
	public static Development buildDevelopment(DevelopmentType type, Estate estate) {
		
		Development development = new Development(type);
		
		// ADD BEHAVIORS
		if (type.isCollector()) {
			development.addBehavior(new Collector(estate, development));
		}
		
		if (type.isStorehouse()) {
			development.addBehavior(new Storehouse(estate, development));
		}
		
		
		development.build();
		
		estate.getBuiltDevelopments().add(development);
		Nobility.getDevelopmentManager().subtractCosts(type, estate);
		
		return development;
		
	}

}
