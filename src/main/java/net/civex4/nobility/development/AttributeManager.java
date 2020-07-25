package net.civex4.nobility.development;

import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;

public class AttributeManager {
	
	public static String getAttributeText(DevAttribute attr, Integer amt) {
		return ChatColor.BLUE + attr.name + ": " + ChatColor.WHITE + amt;
	}
	
	public static int getCityLimit(Estate e) {
		int ret = 0;
		for(Development d : e.getBuiltDevelopments()) {
			if(d.isActive) {
				if(d.attributes.containsKey(DevAttribute.CITY_RADIUS)) {
					int radius = d.attributes.get(DevAttribute.CITY_RADIUS);
					if(radius > ret) {
						ret = radius;
					}
				}
			}
		}
		return ret;
	}
	
	public static int getCannonLimit(Estate e) {
		int ret = 0;
		for(Development d : e.getBuiltDevelopments()) {
			if(d.isActive) {
				if(d.attributes.containsKey(DevAttribute.CANNON_LIMIT)) {
					int amt = d.attributes.get(DevAttribute.CANNON_LIMIT);
					if(amt > ret) {
						ret = amt;
					}
				}
			}
		}
		return ret;
	}
	
	

}
