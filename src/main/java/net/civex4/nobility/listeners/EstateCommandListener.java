package net.civex4.nobility.listeners;

import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.civex4.nobility.Nobility;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.estate.EstateManager;
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
        man.openEstateGUI(p);
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
    }
    
    if(args.length == 2) {
      if(args[0] == "info") {
        
      }
    }
    
    
    
    return false;
  }

}
