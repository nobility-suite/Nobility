package net.civex4.nobility.blueprints;

import java.util.Map;

import net.civex4.nobilityitems.NobilityItem;

public abstract class ItemGroup {
  
  public abstract Map<NobilityItem, Integer> generate();

}
