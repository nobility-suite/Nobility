package net.civex4.nobility.mines;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import io.github.kingvictoria.Region;
import io.github.kingvictoria.RegionResource;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;

public class MineManager {
  
  public Map<Estate, Integer> getMineMapping(Region r, RegionResource resource){
    
    Map<Estate, Integer> ret = new HashMap<Estate, Integer>();
    
    int mines = (int) r.getResource(resource);
    
    for(Estate e : Nobility.getEstateManager().getEstatesInRegion(r)) {
      ret.put(e, e.getMines(resource));
    }
    
    return ret;
    
  }
  
  public boolean mineAvailable(Region r, RegionResource resource) {
    int cap = (int) r.getResource(resource);
    int taken = 0;
    for(Estate e : Nobility.getEstateManager().getEstatesInRegion(r)) {
      taken+= e.getMines(resource);
    }
    
    if(taken > cap) {
      Bukkit.getLogger().log(Level.SEVERE, "Region " + r.getName() + "has more mines claimed than available! Claimed: " + taken + ", total: " + cap);
    }
    
    if(taken >= cap) {
      return false;
    }else return true;


  }
  
  public boolean claimMine(Estate e, RegionResource resource) {
    Region r = e.getRegion();
    
    if(mineAvailable(r, resource)) {
      e.addMineSudo(resource);
      return true;
    }else return false;
  }
  

}
