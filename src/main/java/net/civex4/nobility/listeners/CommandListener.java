package net.civex4.nobility.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.group.Group;
import net.md_5.bungee.api.ChatColor;

public class CommandListener implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		UUID playerId = player.getUniqueId();
		
		if(args.length == 0) {
			player.sendMessage(ChatColor.BLUE + "-== Nobility Commands ==- \n"
					+ ChatColor.GOLD + "/nobility create <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Creates a Nobility group. \n"
					+ ChatColor.GOLD + "/nobility info" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Lists information about your Nobility group. \n"
					+ ChatColor.GOLD + "/nobility info <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Lists information about a Nobility group. \n"
					+ ChatColor.GOLD + "/nobility join <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Joins a Nobility group, if you have an invite. \n"
					+ ChatColor.GOLD + "/nobility estate" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Access estate-related commands. Nobility groups without an estate are considered Nomads. \n"
					+ ChatColor.GOLD + "/nobility invite <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Invites a player to your Nobility group.\n"
					+ ChatColor.GOLD + "/nobility kick <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Kicks a player from your Nobility group. \n");
			return true;
		} else if (args.length == 1) {
			//nobility create
			if (args[0].equalsIgnoreCase("create")) {
			  player.sendMessage(ChatColor.GOLD + "This command creates a Nobility group, which can be used later to allow a group of players to claim land by founding an Estate. \n" + ChatColor.RED + "Correct usage is /nobility create <name>");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("nextday")) {
				Nobility.tickDay();
				return true;
			}
			
		} else if (args.length == 2) {
			//nobility create <NAME>
			if(args[0].equalsIgnoreCase("create") && args[1].length() >= 2) {

				for (int i = 0; i < Nobility.getGroupManager().groups.size(); i++) {
					Group group = Nobility.getGroupManager().groups.get(i);
					if(group.getName().equals(args[1])) {
						player.sendMessage(ChatColor.RED + "That group already exists. Try another name");
						return true;
					}
				}
				
				Group tempGroup = new Group(args[1], playerId);
				Nobility.getGroupManager().groups.add(tempGroup);
				player.sendMessage(ChatColor.GOLD + "You created the group " + tempGroup.getName());
				return true;
			}
			
			//nobility invite <name>
			if (args[0].equalsIgnoreCase("invite")) {
				UUID inviter = playerId;
				Player recieverPlayer = Bukkit.getPlayer(args[1]);				
				if (recieverPlayer == null) {
					player.sendMessage("That player is not online");
					return true;
				}				
				UUID reciever = recieverPlayer.getUniqueId();			
				
				boolean inGroup = false;
				Group tempGroup = null;
				for (int i = 0; i < Nobility.getGroupManager().groups.size(); i++) {
					Group group = Nobility.getGroupManager().groups.get(i);
					for (UUID id : group.members) {
						if (id.equals(reciever)) {
							player.sendMessage(ChatColor.RED + "That player is already part of an Estate.");
							return true;
						}
						
						if (id.equals(inviter)) {
							inGroup = true;
							tempGroup = group;
						}
					}
				}
				
				if(inGroup) {
					tempGroup.pendingInvites.add(reciever);
					recieverPlayer.sendMessage(ChatColor.GOLD + "You have been invited to join the Nobility Group " + ChatColor.BLUE + tempGroup.getName());
				}else {
					player.sendMessage(ChatColor.RED + "You are not part of a Nobility Group.");
					return true;
				}
				
				return true;
				
			}
			
			//nobility join <name>
			if(args[0].equalsIgnoreCase("join")) {
				Group tempGroup = null;
				for(int i = 0; i < Nobility.getGroupManager().groups.size(); i++) {
					Group group = Nobility.getGroupManager().groups.get(i);
					for(UUID id : group.members) {
						if(playerId.equals(id)) {
							player.sendMessage(ChatColor.RED + "You are already part of a Nobility group.");
							return true;
						}
					}
					if(group.getName().equalsIgnoreCase(args[1])) {
						tempGroup = group;
					}
				}
				
				if(tempGroup != null) {
					if(tempGroup.pendingInvites.contains(playerId)) {
						tempGroup.pendingInvites.remove(playerId);
						tempGroup.members.add(playerId);
						player.sendMessage(ChatColor.GREEN + "You have been added to " + tempGroup.getName());
					}
				}
				return true;
			}
		}
		return false;
	}


}
