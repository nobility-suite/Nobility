package net.civex4.nobility.listeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.blueprints.AbstractBlueprint;

public class BlueprintCommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Usage of this command is restricted!");
            return true;
        }

        if (args.length == 0) {
            for (AbstractBlueprint blueprint : Nobility.getBlueprintManager().blueprints) {
                sender.sendMessage(ChatColor.YELLOW + blueprint.name);
            }
        } else if (args[0].equals("generate")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can't be used from the console!");
                return true;
            }

            Player player = (Player) sender;

            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /blueprints generate <blueprint_name>");
                return true;
            } else {
                String name = args[1];
                for (int i = 2; i < args.length; i++) name += " " + args[i];
                name = name.replace('&', '§');
                for (AbstractBlueprint blueprint : Nobility.getBlueprintManager().blueprints) {
                    if (name.equalsIgnoreCase(blueprint.name)) {
                        player.getInventory().addItem(blueprint.generate().parseBlueprintToItem());
                        sender.sendMessage(ChatColor.YELLOW + "Blueprint created!");
                        return true;
                    }
                }

                sender.sendMessage(ChatColor.RED + name + " doesn't match any blueprint!");
                return true;
            }
        }

        return true;
    }
    
}