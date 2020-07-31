package net.civex4.nobility.listeners;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.estate.EstateManager;
import net.civex4.nobility.group.Group;
import net.md_5.bungee.api.ChatColor;

public class EstateCommandListener implements CommandExecutor {
  
  EstateManager man = Nobility.getEstateManager();
  
  public static String ERROR_NO_ESTATE = ChatColor.RED + "You are not part of an estate.";
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
   
    Player p = (Player) sender;
    UUID playerId = p.getUniqueId();
    
    if(args.length == 0) {
      if(man.playerHasEstate(p) == false) {
        p.sendMessage(ERROR_NO_ESTATE);
      }else {
        Nobility.getEstateGui().openEstateGUI(p);
      }
    }
    
    if(args.length == 1) {
      if(args[0] == "info") {
        if(man.playerHasEstate(p) == false) {
          p.sendMessage(ERROR_NO_ESTATE);
        }else {
          Estate estate = man.getEstateOfPlayer(p);
          man.sendInfoMessage(estate, p);
        }
      }
      
      if(args[0] == "create") {
     	  if(Nobility.getGroupManager().getGroup(p) == null) {
    		  p.sendMessage(ChatColor.RED + "You must create a NobilityGroup to found an estate. For more information: /nobility create");
    	  }else {
    		  if (!(Nobility.getEstateManager().playerHasEstate(p))) {
    			  	Group group = Nobility.getGroupManager().getGroup(p);
    			  	if(group.hasEstate()) {
    			  		p.sendMessage(ChatColor.RED + "Your group already has an Estate!");
    			  	}else {
    			  		Block b = p.getLocation().getBlock();
    			  		b.setType(Material.CHEST);
    			  		Nobility.getEstateManager().createEstate(b, p);
    			  		p.sendMessage(ChatColor.GREEN + "Successfully created estate " + group.getName());
    			  		return true;
    			  	}
    			  
    		  }else { p.sendMessage(ChatColor.RED + "You are already part of an Estate! You must leave your current estate before creating a new one.");}
    		  
//  		Player p = e.getPlayer();
//  		if (Nobility.getGroupManager().getGroup(p) == null) return;
//  		Block b = e.getClickedBlock();
//  			if (!(Nobility.getEstateManager().playerHasEstate(p))) {
//  				if (b != null && b.getType() == Material.CHEST) {				
//  					Nobility.getEstateManager().createEstate(b, p);
//  				}
     	}
      }
    }
    
    if(args.length == 2) {
      if(args[0] == "info") {
        
      }
    }
    
    
    
    return false;
  }

}
