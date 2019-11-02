package net.civex4.nobility;

import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentManager;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.estate.EstateManager;
import net.civex4.nobility.group.GroupManager;
import net.civex4.nobility.listeners.ChestClick;
import net.civex4.nobility.listeners.CommandListener;
import net.civex4.nobility.listeners.DevelopmentGUI;
import net.civex4.nobility.listeners.EstateCreate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.kingvictoria.NobilityRegions;
import net.md_5.bungee.api.ChatColor;

public class Nobility extends JavaPlugin {

  public static GroupManager groupMan;
  public static EstateManager estateMan;
  private static Nobility nobility;
  private static NobilityRegions nobilityRegions;
  private static DevelopmentManager developmentManager;

  public static int currentDay = 0;

  public void onEnable() {

    nobility = this;
    nobilityRegions = getPlugin(NobilityRegions.class);
    groupMan = new GroupManager();
    estateMan = new EstateManager();

    developmentManager = new DevelopmentManager();
    registerConfig();
    reloadConfig();
    getCommand("nobility").setExecutor(new CommandListener());
    DevelopmentType.loadDevelopmentTypes(getConfig().getConfigurationSection("developments"));

    registerEvents();
  }

  public static GroupManager getGroupManager() {
    return groupMan;
  }

  private void registerEvents() {
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new EstateCreate(), this);
    pm.registerEvents(new ChestClick(), this);
    pm.registerEvents(new DevelopmentGUI(), this);
  }


  public void onDisable() {

  }

  /**
   * Gets the instance of the Nobility plugin
   *
   * @return Nobility Plugin
   */
  public static Nobility getNobility() {
    return nobility;
  }

  /**
   * Gets the instance of the NobilityRegions plugin
   *
   * @return NobilityRegions Plugin
   */
  public static NobilityRegions getNobilityRegions() {
    return nobilityRegions;
  }

  public static DevelopmentManager getDevelopmentManager() {
    return developmentManager;
  }

  public static void tickDay() {
    currentDay += 1;
    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
      p.sendMessage(ChatColor.GOLD + "Rise and shine! A new dawn is upon us. The current day is: "
          + ChatColor.BLUE + ChatColor.BOLD + currentDay);
    }

    //DO STUFF
    for (Estate estate : estateMan.estates) {
      for (Development development : estate.getActiveDevelopments()) {
        developmentManager.subtractUpkeep(development.getDevelopmentType(), estate);
      }
    }

    for (Estate estate : estateMan.estates) {
      for (Development development : estate.getActiveDevelopments()) {
        if (development.isActive()) {
          development.tick();
        }
      }
    }
  }

  private void registerConfig() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }

}
