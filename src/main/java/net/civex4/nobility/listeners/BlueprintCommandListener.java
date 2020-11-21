package net.civex4.nobility.listeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.Nobility;
import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.Blueprint;
import net.civex4.nobilityitems.NobilityItem;

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
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can't be used from the console!");
                return true;
            }

            Player player = (Player) sender;

            if (args.length < 2) {
            	if(args[0].equals("research")) {
            		Player p = (Player) sender;
            		Nobility.getEstateGui().openBlueprintResearchGUI(p);
            	}
                if (args[0].equals("craft")) {
                    sender.sendMessage(ChatColor.YELLOW + "Crafting...");

                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item == null) {
                        sender.sendMessage(ChatColor.RED + "Must have a blueprint in your main hand!");
                        return true;
                    }

                    Blueprint blueprint = Blueprint.parseBlueprintFromItem(item);
                    for (NobilityItem ingredient : blueprint.ingredients.keySet()) {
                        if (!player.getInventory().contains(ingredient.getItemStack(blueprint.ingredients.get(ingredient)))) {
                            sender.sendMessage(ChatColor.RED + "You don't have all of the ingredients!");
                            return true;
                        }
                    }

                    for (NobilityItem ingredient : blueprint.ingredients.keySet()) {
                        player.getInventory().remove(ingredient.getItemStack(blueprint.ingredients.get(ingredient)));
                    }

                    player.getInventory().addItem(blueprint.result.getItemStack(blueprint.resultAmount));
                    sender.sendMessage(ChatColor.YELLOW + blueprint.name + " crafted!");
                    return true;
                }

                if (args[0].equals("generate")) {
                    sender.sendMessage(ChatColor.RED + "Usage: /blueprints generate <blueprint_name>");
                    return true;
                }

                sender.sendMessage(ChatColor.RED + "/blueprints " + args[0] + " is not a valid command!");
            } else if (args.length >= 2 && args[0].equals("generate")) {
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