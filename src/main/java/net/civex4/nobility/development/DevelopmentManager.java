package net.civex4.nobility.development;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import net.civex4.nobility.developments.Arsenal;
import net.civex4.nobility.developments.BasicWall;
import net.civex4.nobility.developments.BasicWorkshop;
import net.civex4.nobility.developments.CommunityWell;
import net.civex4.nobility.developments.FarmingCamp;
import net.civex4.nobility.developments.LumberCamp;
import net.civex4.nobility.developments.MiningCamp;
import net.civex4.nobility.developments.SiegeWorkshop;
import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;

public class DevelopmentManager {
	private HashMap<String,DevelopmentBlueprint> blueprints;
	private HashMap<String,DevelopmentBlueprint> upgrades;

	public DevelopmentManager() { 
		blueprints = new HashMap<String,DevelopmentBlueprint>();
		blueprints = loadBlueprints();
		
		upgrades = new HashMap<String,DevelopmentBlueprint>();
		upgrades = loadUpgrades();
	}

	private HashMap<String,DevelopmentBlueprint> loadUpgrades() {
		HashMap<String,DevelopmentBlueprint> ret = new HashMap<String,DevelopmentBlueprint>();
		

		
		
		return ret;
	}
	
	public HashMap<String,DevelopmentBlueprint> getBlueprints(){
		return this.blueprints;
	}
	
	public HashMap<String,DevelopmentBlueprint> getUpgrades(){
		return this.upgrades;
	}

	private HashMap<String,DevelopmentBlueprint>loadBlueprints() {
		HashMap<String,DevelopmentBlueprint> ret = new HashMap<String,DevelopmentBlueprint>();
		
		HashMap<String,Integer> costs = new HashMap<String,Integer>();
		costs.put("Iron Ingot", 10);
		costs.put("Log", 50);
		
		//CAMPS
		DevelopmentBlueprint miningCamp = new DevelopmentBlueprint();
		miningCamp.result = new MiningCamp();
		miningCamp.cost = costs;
		ret.put(miningCamp.result.name,miningCamp);
		
		DevelopmentBlueprint lumberCamp = new DevelopmentBlueprint();
		lumberCamp.result = new LumberCamp();
		lumberCamp.cost = costs;
		ret.put(lumberCamp.result.name,lumberCamp);
		
		DevelopmentBlueprint farmCamp = new DevelopmentBlueprint();
		farmCamp.result = new FarmingCamp();
		farmCamp.cost = costs;
		ret.put(farmCamp.result.name,farmCamp);
		
		//WORKSHOPS
		DevelopmentBlueprint basicWorkshop = new DevelopmentBlueprint();
		basicWorkshop.result = new BasicWorkshop();
		basicWorkshop.cost = costs;
		ret.put(basicWorkshop.result.name,basicWorkshop);
		
		DevelopmentBlueprint siegeWorkshop = new DevelopmentBlueprint();
		siegeWorkshop.result = new SiegeWorkshop();
		siegeWorkshop.cost = costs;
		ret.put(siegeWorkshop.result.name,siegeWorkshop);
		
		//ARSENAL
		DevelopmentBlueprint basicArsenal = new DevelopmentBlueprint();
		basicArsenal.result = new Arsenal();
		basicArsenal.cost = costs;
		ret.put(basicArsenal.result.name,basicArsenal);
		
		//WALLS
		DevelopmentBlueprint basicWall = new DevelopmentBlueprint();
		basicWall.result = new BasicWall();
		basicWall.cost = costs;
		ret.put(basicWall.result.name,basicWall);
		
		//CITY RADIUS
		DevelopmentBlueprint basicWell = new DevelopmentBlueprint();
		basicWell.result = new CommunityWell();
		basicWell.cost = costs;
		ret.put(basicWell.result.name,basicWell);
		
		return ret;
	}
	
	public boolean build (DevelopmentBlueprint b, Estate e, Player p) {
		List<Development> built = e.getBuiltDevelopments();
		
		p.closeInventory();
		
		for(Development d : built) {
			if(d.name == b.result.name){
				p.sendMessage(ChatColor.RED + "Your estate already has a " + ChatColor.WHITE + d.name + " built.");
				return false;
			}
		}
		
		if(!checkCosts(b,p)) {
			p.sendMessage(ChatColor.RED + "You do not have enough materials to construct a " + ChatColor.WHITE + b.result.name + ".");
			return false;
		}
		
		if(false) {
			//TODO check permissions
			p.sendMessage(ChatColor.RED + "You must be an official of " + ChatColor.WHITE + e.getGroup().getName() + ChatColor.RED + " to construct this development.");
			return false;
		}
		
		p.sendMessage(ChatColor.GREEN + "Constructing...");
		//TODO subtract costs here
		DevelopmentManager.sudoBuildDevelopment(b,e);
		p.sendMessage(ChatColor.GREEN + "Constructed a " + ChatColor.WHITE + b.result.name + ChatColor.GREEN + " in " + ChatColor.WHITE + e.getGroup().getName());
		return true;
		
	}
	
	private static void sudoBuildDevelopment(DevelopmentBlueprint b, Estate e) {
		Development result = b.result;
		e.addDevelopment(result);	
	}

	public boolean checkCosts(DevelopmentBlueprint b, Player p) {
		return true; //TODO implement
	}
	
//	public void subtractUpkeep(DevelopmentType type, Estate estate) {		
//		if (type.getUpkeepCost() == null) return;
//		Inventory inventory = estate.getInventory();
//		for (ItemStack cost : type.getUpkeepCost()) {	
//			if (inventory.containsAtLeast(cost, cost.getAmount())) {
//				inventory.removeItem(cost);
//			} else {
//				estate.getDevelopment(type).deactivate();
//			}
//
//		}
//	}
//	
//	public boolean checkCosts(DevelopmentType type, Inventory inv) {		
//		if (type.getInitialCost().isEmpty()) return true;
//		if (inv == null) {
//			Bukkit.getLogger().warning("You can't check the cost of a null inventory");
//			return false;
//		}
//		for (ItemStack cost : type.getInitialCost()) {
//			if (!inv.containsAtLeast(cost, cost.getAmount())) {
//				return false;
//			}
//		}		
//		return true;   	
//	}
//	
//	public boolean checkCosts(DevelopmentType type, Estate estate) {
//		return checkCosts(type, estate.getInventory());
//	}
//	
//	public boolean subtractCosts(DevelopmentType type, Estate estate) {
//		Inventory inv = estate.getInventory();
//		return subtractCosts(type, inv);
//	}
//	
//	public boolean subtractCosts(DevelopmentType type, Inventory inv) {
//		if (type.getInitialCost() == null) return true;		
//		boolean ok = true;
//		for (ItemStack cost : type.getInitialCost()) {
//			inv.removeItem(cost);
//		} 
//		/* else {
//			Nobility.getNobility().warning("You cannot subtract costs if an inventory does not contain the costs");
//		} */
//		return ok;
//	}

	
}
