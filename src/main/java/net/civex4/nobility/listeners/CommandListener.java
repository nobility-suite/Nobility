package net.civex4.nobility.listeners;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import net.civex4.nobility.estate.EstateManager;
import net.civex4.nobility.group.GroupManager;
import net.civex4.nobility.group.GroupPermission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.development.DevAttribute;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.group.Group;
import net.civex4.nobility.siege.Siege;
import net.md_5.bungee.api.ChatColor;
import org.slf4j.Logger;

public class CommandListener implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		UUID playerId = player.getUniqueId();


		/**
		 * Sends basic help information to user, honestly not the best way to do it, but it works :^)
		 */
		if(args.length == 0) {
			player.sendMessage(ChatColor.BLUE + "-== Nobility Commands ==- \n"
					+ ChatColor.GOLD + "/nobility create <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Creates a Nobility group. \n"
					+ ChatColor.GOLD + "/nobility info" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Lists information about your Nobility group. \n"
					+ ChatColor.GOLD + "/nobility info <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Lists information about a Nobility group. \n"
					+ ChatColor.GOLD + "/nobility join <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Joins a Nobility group, if you have an invite. \n"
					+ ChatColor.GOLD + "/nobility estate" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Access estate-related commands. Nobility groups without an estate are considered Nomads. \n"
					+ ChatColor.GOLD + "/nobility invite <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Invites a player to your Nobility group.\n"
					+ ChatColor.GOLD + "/nobility kick <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Kicks a player from your Nobility group. \n"
					+ ChatColor.GOLD + "/nobility promote <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Promotes a player in your Nobility group. \n"
					+ ChatColor.GOLD + "/nobility demote <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Demotes a player in your Nobility group. \n"
					+ ChatColor.GOLD + "/nobility rankrename <rank> <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Renames a certain rank in your Nobility group. \n"
					+ ChatColor.GOLD + "/nobility rank" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Checks your current rank in your Nobility group. \n"
			);
			return true;
		} else if (args.length == 1) {
			//nobility create
			if (args[0].equalsIgnoreCase("create")) {
			  player.sendMessage(ChatColor.GOLD + "This command creates a Nobility group, which can be used later to allow a group of players to claim land by founding an Estate. \n" + ChatColor.RED + "Correct usage is /nobility create <name>");
				return true;
			}

			//checks players current rank.
			if (args[0].equalsIgnoreCase("rank")) {
				Group g = Nobility.getGroupManager().getGroup(player);
				GroupPermission perm = g.getPermission(player);
				player.sendMessage(ChatColor.GOLD + "You are the rank: " + ChatColor.AQUA + perm.name());
			}
			
			if (args[0].equalsIgnoreCase("nextday")) {
				Nobility.tickDay();
				for(Estate e : Nobility.getEstateManager().getEstates()) {
					e.setCurrentHealth(e.getMaxHealth());
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("info")) {
				if(Nobility.getEstateManager().getEstateOfPlayer(player) != null) {
					Estate e = Nobility.getEstateManager().getEstateOfPlayer(player);
					player.sendMessage(ChatColor.BLUE + "You are a part of the Estate " + ChatColor.WHITE + e.getGroup().getName());
				}
			if(args[0].equalsIgnoreCase("kick")) {
				player.sendMessage(ChatColor.GOLD + "This command kicks a player from a nobility group. \n" + ChatColor.GOLD + "Proper usage is /nobility kick <player>");
			}
			if(args[0].equalsIgnoreCase("promote")) {
				player.sendMessage(ChatColor.GOLD + "This command promotes a member of your nobility group. \n" + ChatColor.GOLD + "Proper usage is /nobility promote <player>");
			}
			if(args[0].equalsIgnoreCase("demote")) {
				player.sendMessage(ChatColor.GOLD + "This command demotes a member of your nobility group. \n" + ChatColor.GOLD + "Proper usage is /nobility demote <player>");
			}
			if(args[0].equalsIgnoreCase("rankrename")) {
				player.sendMessage(ChatColor.GOLD + "This command renames a rank in your nobility group. \n" + ChatColor.GOLD + "Proper usage is /nobility rankrename <rank> <name>");
			}
			}
			
		} else if (args.length == 2) {
			//nobility create <NAME>
			if(args[0].equalsIgnoreCase("cannon") && args[1].equalsIgnoreCase("place")) {
				Nobility.getCannonManager().summonCannon(player);
				return true;
			}
			if(args[0].equalsIgnoreCase("cannon") && args[1].equalsIgnoreCase("recover")) {
				Nobility.getCannonManager().attemptPickupCannon(player);
				return true;
			}

			/**
			 * Promotion Command for Nobility Groups.
			 */
			if(args[0].equalsIgnoreCase("promote")) {
				//nobility promote <player>
				Group g = Nobility.getGroupManager().getGroup(player);

				//Checking if player is in a group before continuing.
				if(g == null) {
					player.sendMessage(ChatColor.RED + "You are not a part of a nobility group!");
					return true;
				}

				GroupPermission perm = g.getPermission(player);
				if(perm == GroupPermission.OFFICER || perm == GroupPermission.LEADER) {
					String promoteString = args[1].toString();
					OfflinePlayer promotePlayer = Bukkit.getOfflinePlayer(promoteString);
					GroupPermission promotePerm = g.getPermission(promotePlayer);
					//Large amount of If statements below, im sorry if these affect u ):

					if(promotePerm == null) {
						player.sendMessage(ChatColor.RED + "You can't demote someone not in your group!");
					}

					if(player.getUniqueId() == promotePlayer.getUniqueId()) {
						player.sendMessage(ChatColor.RED + "You can't promote yourself!");
						return true;
					}

					//Check to make sure the player can promote.
					if(perm == GroupPermission.DEFAULT || perm == GroupPermission.TRUSTED) {
						player.sendMessage(ChatColor.RED + "You don't have enough power to do that!");
						return true;
					}
					//Promote Moderator to Officer.
					if(promotePerm == GroupPermission.TRUSTED && perm == GroupPermission.LEADER) {
						g.setPermission(promotePlayer, GroupPermission.OFFICER);
						Bukkit.getServer().getLogger().info("Updated player's rank to: " + g.getPermission(promotePlayer));
						Group f = Nobility.getGroupManager().getGroup(player); //Reload variable that for some weird fucking reason fixes promotion bug.
						Bukkit.getServer().getLogger().info("Player rank recheck to: " + f.getPermission(promotePlayer));
						player.sendMessage(ChatColor.GOLD + "Successfully promoted " + ChatColor.AQUA + promotePlayer.getName() + ".");
						if(promotePlayer.isOnline()) {
							promotePlayer.getPlayer().sendMessage(ChatColor.GREEN + "You have been promoted!");
						}
						return true;
					}
					//Promote Member to Moderator.
					if(promotePerm == GroupPermission.DEFAULT && perm == GroupPermission.OFFICER) {
						g.setPermission(promotePlayer, GroupPermission.TRUSTED);
						player.sendMessage(ChatColor.GOLD + "Successfully promoted " + ChatColor.AQUA + promotePlayer.getName() + ".");
						if(promotePlayer.isOnline()) {
							promotePlayer.getPlayer().sendMessage(ChatColor.GREEN + "You have been promoted!");
						}
						return true;
					}
					//Promote Member to Moderator. (As leader)
					if(promotePerm == GroupPermission.DEFAULT && perm == GroupPermission.LEADER) {
						g.setPermission(promotePlayer, GroupPermission.TRUSTED);
						player.sendMessage(ChatColor.GOLD + "Successfully promoted " + ChatColor.AQUA + promotePlayer.getName() + ".");
						if(promotePlayer.isOnline()) {
							promotePlayer.getPlayer().sendMessage(ChatColor.GREEN + "You have been promoted!");
						}
						return true;
					}
					//Prevent Officer promotion without group transfer.
					if(promotePerm == GroupPermission.OFFICER && perm == GroupPermission.LEADER) {
						player.sendMessage(ChatColor.RED + "You cannot transfer group ownership with this command! \n" +
						ChatColor.GOLD + "Try using /nobility transfer <player> instead!");
						return true;
					}
				}
				player.sendMessage(ChatColor.RED + "You don't have permission to promote that player!");
				return true;
			}

			/**
			 * Demotion Command for Nobility Groups.
			 */
			if(args[0].equalsIgnoreCase("demote")) {
				Group g = Nobility.getGroupManager().getGroup(player);
				//Check to make sure player is in a group before getting permissions.
				if(g == null) {
					player.sendMessage(ChatColor.RED + "You are not a part of a nobility group!");
					return true;
				}

				GroupPermission perm = g.getPermission(player);
				if(perm == GroupPermission.OFFICER || perm == GroupPermission.LEADER) {
					String demoteString = args[1].toString();
					OfflinePlayer demotePlayer = Bukkit.getOfflinePlayer(demoteString);
					GroupPermission demotePerm = g.getPermission(demotePlayer);

					if(player.getUniqueId() == demotePlayer.getUniqueId()) {
						player.sendMessage(ChatColor.RED + "You can't demote yourself!");
						return true;
					}
					if(demotePerm == null) {
						player.sendMessage(ChatColor.RED + "That player is not in your nobility group!");
						return true;
					}
					if(demotePerm == GroupPermission.DEFAULT) {
						player.sendMessage(ChatColor.RED + "You can't demote a player that is already the lowest rank. \n" + "Try /nobility kick <player> to remove them instead!");
						return true;
					}
					if(demotePerm == GroupPermission.TRUSTED) {
						player.sendMessage(ChatColor.GOLD + "Successfully demoted " + ChatColor.AQUA + demotePlayer.getName() + ".");
						if(demotePlayer.isOnline()) {
							demotePlayer.getPlayer().sendMessage(ChatColor.RED + "You have been demoted.");
						}
						g.setPermission(demotePlayer, GroupPermission.DEFAULT);
						return true;
					}
					if(demotePerm == GroupPermission.OFFICER && perm == GroupPermission.LEADER) {
						player.sendMessage(ChatColor.GOLD + "Successfully demoted " + ChatColor.AQUA + demotePlayer.getName() + ".");
						if(demotePlayer.isOnline()) {
							demotePlayer.getPlayer().sendMessage(ChatColor.RED + "You have been demoted.");
						}
						g.setPermission(demotePlayer, GroupPermission.TRUSTED);
						return true;
					}
				}
				player.sendMessage(ChatColor.RED + "You don't have permission to demote that player!");
				return true;
			}


			/**
			 * Kick Command for Nobility Groups.
			 */
			if(args[0].equalsIgnoreCase("kick")) {
				//nobility kick <player>
				Group g = Nobility.getGroupManager().getGroup(player);
				if(g == null) {
					player.sendMessage(ChatColor.RED + "You are not a part of a nobility group!");
					return true;
				}

				/**
				 * Above makes sure you are in group before checking perms since you get an NPE otherwise.
				 */
				GroupPermission perm = g.getPermission(player);
				if(perm == GroupPermission.OFFICER || perm == GroupPermission.LEADER) {
					String kickString = args[1].toString();
					OfflinePlayer kickedPlayer = Bukkit.getOfflinePlayer(kickString);
					GroupPermission kickPerm = g.getPermission(kickedPlayer);
					if(kickPerm == null) {
						player.sendMessage(ChatColor.RED + "That player is not in your nobility group!");
						return true;
					}
					if(perm == GroupPermission.LEADER && kickPerm == GroupPermission.OFFICER) {
						player.sendMessage(ChatColor.GOLD + "Successfully removed " + ChatColor.AQUA + kickedPlayer.getName() + ChatColor.GOLD + " from your nobility group!");
						g.removeMember(kickedPlayer);
						return true;
					}
					if(kickPerm == GroupPermission.OFFICER || kickPerm == GroupPermission.LEADER) {
						player.sendMessage(ChatColor.RED + "You cannot kick players of equal, or above rank as you!");
						return true;
					}
					g.removeMember(kickedPlayer);
					player.sendMessage(ChatColor.GOLD + "Successfully removed " + ChatColor.AQUA + kickedPlayer.getName() + ChatColor.GOLD + " from your nobility group!");
				} else {
					player.sendMessage(ChatColor.RED + "You don't have permission to kick players from your nobility group!");
					return true;
				}
				return true;
			}
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
				Player receiverPlayer = Bukkit.getPlayer(args[1]);
				if (receiverPlayer == null) {
					player.sendMessage("That player is not online");
					return true;
				}				
				UUID receiver = receiverPlayer.getUniqueId();
				
				boolean inGroup = false;
				Group tempGroup = null;
				for (int i = 0; i < Nobility.getGroupManager().groups.size(); i++) {
					Group group = Nobility.getGroupManager().groups.get(i);
					for (UUID id : group.getMembers()) {
						if (id.equals(receiver)) {
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
					tempGroup.getPendingInvites().add(receiver);
					receiverPlayer.sendMessage(ChatColor.GOLD + "You have been invited to join the Nobility Group " + ChatColor.BLUE + tempGroup.getName());
					player.sendMessage(ChatColor.GREEN + "You have invited " + ChatColor.WHITE + receiverPlayer.getName() + ChatColor.GREEN + " to " + ChatColor.WHITE + tempGroup.getName());
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
					for(UUID id : group.getMembers()) {
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
					if(tempGroup.getPendingInvites().contains(playerId)) {
						tempGroup.getPendingInvites().remove(playerId);
						tempGroup.addMember(playerId);
						if(tempGroup.hasEstate()) {
							Nobility.getEstateManager().setEstateOfPlayer(Bukkit.getPlayer(playerId), Nobility.getEstateManager().getEstate(tempGroup));
						}
						player.sendMessage(ChatColor.GREEN + "You have been added to " + tempGroup.getName());
						tempGroup.announce(ChatColor.GREEN + "New Member: " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + " has joined " + ChatColor.WHITE + tempGroup.getName());
					}
					player.sendMessage(ChatColor.RED + "You have not been invited to " + ChatColor.WHITE + tempGroup.getName() + ChatColor.GREEN + ".");
				}else {
					player.sendMessage(ChatColor.RED + "Could not find group " + ChatColor.WHITE + args[1]);
				}
				return true;
			}

		}
		if(args.length == 3) {
			Player p = (Player) sender;
			if(args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("siege")) {
				String target = args[2];
				Estate e = null;
				for(Group g : Nobility.getGroupManager().groups) {
					if(g.getName().equalsIgnoreCase(target)) {
						if(g.hasEstate()) {
							e = Nobility.getEstateManager().getEstate(g);
						}else {
							p.sendMessage(ChatColor.DARK_RED + "That group does not have an estate");
						}
					}
				}
				if(e == null) { p.sendMessage(ChatColor.DARK_RED + "That group does not exist.");
					return true;
				}else {
					Siege s = new Siege(e);
					Nobility.getSiegeManager().addSiege(s);
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("cannon")) {
				Estate e = Nobility.getEstateManager().getEstateOfPlayer(player);
				if(e != null) {
					for(Development d : e.getBuiltDevelopments()) {
						if(d.getType() == DevelopmentType.ARSENAL) {
							HashMap<DevAttribute,Integer> attributes = d.attributes;
							if(attributes != null) {
								if(attributes.containsKey(DevAttribute.CANNON_LIMIT) && attributes.containsKey(DevAttribute.CANNON_STORED)) {
									int amt = Integer.parseInt(args[2]);
									amt = Math.max(attributes.get(DevAttribute.CANNON_LIMIT), amt);
									attributes.put(DevAttribute.CANNON_STORED, amt);
									player.sendMessage(ChatColor.DARK_RED + "[ADMIN MODE]: Added " + ChatColor.WHITE + amt + ChatColor.DARK_RED + " cannons to your estate.");
									
								}
							}
						}
					}
				}
			}
		}
		return false;
	}


}
