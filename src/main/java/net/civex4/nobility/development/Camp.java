package net.civex4.nobility.development;

import io.github.kingvictoria.regions.nodes.Node;
import io.github.kingvictoria.regions.nodes.NodeType;
import net.civex4.nobility.Nobility;
import net.civex4.nobilityitems.NobilityItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Camp extends Development{
	private int nodeLimit;
	public NodeType nodeType;
	public Location outputChest;

	public Camp() {
		super(DevelopmentType.CAMP);
		this.nodeLimit = 1;
	}
	
	public void setNodeLimit(int i) {
		this.nodeLimit = i;
	}

	public Location getOutputChest() { return this.outputChest; }

	public void setOutputChest(Location location) { this.outputChest = location; }

	public void produceOutput(Node node) throws Exception {
		Location location = getOutputChest();
		if (location == null) {
			Nobility.getNobility().getLogger().warning(node.getName() + " did not have valid output chest!");
			return;
		}
		int workers = node.getUsedSlots();
		List<UUID> workerUUID = node.getWorkers();
		Chest chest = (Chest) location.getBlock().getState();
		Inventory chestInv = chest.getBlockInventory();
		Map<NobilityItem, Integer> map = node.getOutput();

		for (NobilityItem item : map.keySet()) {
			Integer amount = map.get(item);
			int outputAmount = amount * workers;

			ItemStack output = item.getItemStack(outputAmount);

			chestInv.addItem(output);
		}

		for(UUID u : workerUUID) {
			OfflinePlayer pl = Bukkit.getOfflinePlayer(u);
			Nobility.getWorkerManager().removeWorker(node, pl);
		}
	}

	public int getNodeLimit() {
		return this.nodeLimit;
	}

}
