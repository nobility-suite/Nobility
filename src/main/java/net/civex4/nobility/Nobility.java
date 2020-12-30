package net.civex4.nobility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.kingvictoria.regions.nodes.Node;
import net.civex4.nobility.development.Camp;
import net.civex4.nobility.development.DevelopmentType;
import net.civex4.nobility.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
 
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;

import io.github.kingvictoria.NobilityRegions;
import net.civex4.nobility.blueprints.BlueprintManager;
import net.civex4.nobility.cannons.CannonManager;
import net.civex4.nobility.claim.ClaimManager;
import net.civex4.nobility.database.utility.DatabaseBuilder;
import net.civex4.nobility.development.Development;
import net.civex4.nobility.development.DevelopmentManager;
import net.civex4.nobility.estate.Estate;
import net.civex4.nobility.estate.EstateManager;
import net.civex4.nobility.group.GroupManager;
import net.civex4.nobility.gui.EstateGui;
import net.civex4.nobility.gui.GUICommand;
import net.civex4.nobility.siege.SiegeManager;
import net.civex4.nobility.workers.WorkerManager;
import net.md_5.bungee.api.ChatColor;
//import vg.civcraft.mc.citadel.Citadel;
//import vg.civcraft.mc.citadel.ReinforcementManager;
import vg.civcraft.mc.civmodcore.ACivMod;
import vg.civcraft.mc.civmodcore.playersettings.PlayerSettingAPI;
import vg.civcraft.mc.civmodcore.playersettings.gui.MenuSection;

public class Nobility extends ACivMod {
	
	private static GroupManager groupMan;
	private static EstateManager estateMan;
	private static Nobility nobility;
	private static NobilityRegions nobilityRegions;
	private static DevelopmentManager developmentManager;
	private static WorkerManager workerManager;
	private static CannonManager cannonManager;
	private static SiegeManager siegeManager;
	private static BlueprintManager blueprintManager;
	private static EstateGui estateGui;
	private static ChestSelectionListener chestSelect;
	//private static Citadel CitadelManager;
	//public static ReinforcementManager reinforcementManager;

	private static ClaimManager claimManager;
	private static MenuSection nobilityMenu = PlayerSettingAPI.getMainMenu().createMenuSection("Nobility", "Settings");
	
	public static int currentDay = 0;
	public static Map<String, Clipboard> schematics = new HashMap<>();


	@Override
	public void onEnable() {
		super.onEnable();
		nobility = this;
		nobilityRegions = getPlugin(NobilityRegions.class);
		initializeManagers();
		registerConfig();
		reloadConfig();
		this.getCommand("nobility").setExecutor(new CommandListener());
		this.getCommand("test").setExecutor(new GUICommand());
		this.getCommand("estate").setExecutor(new EstateCommandListener());
		this.getCommand("create").setExecutor(new CreateCommand());
		this.getCommand("blueprints").setExecutor(new BlueprintCommandListener());

		blueprintManager.init(new File(getDataFolder(), "blueprints.yml"));
		//DevelopmentType.loadDevelopmentTypes(getConfig().getConfigurationSection("developments"));

		registerEvents();
		
		

		new DatabaseBuilder().setUpDatabase();
	}
	
	private static void initializeManagers() {
		groupMan = new GroupManager();
		estateMan = new EstateManager();
		developmentManager = new DevelopmentManager();
		claimManager = new ClaimManager();
		workerManager = new WorkerManager();
		cannonManager = new CannonManager();
		siegeManager = new SiegeManager();
		blueprintManager = new BlueprintManager();
		estateGui = new EstateGui();
		chestSelect = new ChestSelectionListener();
	}
	
	public static GroupManager getGroupManager() {
		return groupMan;
	}
	
	public static EstateManager getEstateManager() {
		return estateMan;
	}

	public static DevelopmentManager getDevelopmentManager() {
		return developmentManager;
	}
	
	public static ClaimManager getClaimManager() {
		return claimManager;
	}
	
	public static CannonManager getCannonManager() {
		return cannonManager;
	}
	
	public static WorkerManager getWorkerManager() {
		return workerManager;
	}
	
	public static SiegeManager getSiegeManager() {
		return siegeManager;		
	}

	public  static BlueprintManager getBlueprintManager() {
		return blueprintManager;
	}

	public static EstateGui getEstateGui() { return estateGui; }
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EstateCreate(), this);
		pm.registerEvents(new ChestClick(), this);
		pm.registerEvents(new ProtectionListener(), this);
		pm.registerEvents(new CannonListener(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(chestSelect, this);

		
		if(pm.isPluginEnabled("Citadel")) {
			Plugin pl = pm.getPlugin("Citadel");
			//this.CitadelManager = (Citadel) pl;
			//this.reinforcementManager = CitadelManager.getReinforcementManager();
		}
		
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
	public static io.github.kingvictoria.NobilityRegions getNobilityRegions() {
		return nobilityRegions;
	}

	public static void tickDay() {
		currentDay += 1;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(ChatColor.GOLD + "Rise and shine! A new dawn is upon us. The current day is: "
					+ ChatColor.BLUE + ChatColor.BOLD + currentDay);
		}

		for (Estate estate : estateMan.getEstates()) {
			for (Development development : estate.getActiveDevelopments()) {
				//developmentManager.subtractUpkeep(development.getType(), estate);
			}
		}

		for (Estate estate : estateMan.getEstates()) {
			for (Development development : estate.getActiveDevelopments()) {
				development.tick();
			}
		}

		List<Estate> estates = Nobility.getEstateManager().getEstates();

		for (Estate estate : estates) {
			List<Development> developments = estate.getBuiltDevelopments();
			ArrayList<Camp> camps = new ArrayList<Camp>();
			ArrayList<Node> nodes = estate.getNodes();

			for (Development development : developments) {
				if (development.getType() == DevelopmentType.CAMP) {
					camps.add((Camp) development);
				}
				if (development.getType() == DevelopmentType.ARSENAL) {
					Nobility.getDevelopmentManager().arsenalUpkeep(development, estate);
				}
			}
			for (Camp camp : camps) {
				for (Node node : nodes) {
					if (node.getType() == camp.nodeType) {
						try {
							camp.produceOutput(node);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	
	
	public static MenuSection getMenu() {
		return nobilityMenu;
	}
	
	public static Clipboard getSchematic(String name) {
		Clipboard schematic = Nobility.schematics.get(name);
		if (schematic != null) {
			return schematic;
		}
		File schemDir = new File(Nobility.getNobility().getDataFolder(), "schematics");
		if (!schemDir.exists()) {
			//noinspection ResultOfMethodCallIgnored
			schemDir.mkdirs();
		}

		File schemFile = new File(schemDir, name + ".schem");
		if (!schemFile.exists()) {
			return null;
		}

		ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
		if (format == null) {
			return null;
		}
		try (ClipboardReader reader = format.getReader(new FileInputStream(schemFile))) {
			schematic = reader.read();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Nobility.getNobility().schematics.put(name, schematic);
		return schematic;
	}
	
	public static ChestSelectionListener getChestSelector() {
		return chestSelect;
	}

}
