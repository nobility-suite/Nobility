package net.civex4.nobility.workers;

import java.util.HashMap;
import java.util.UUID;

import io.github.kingvictoria.regions.nodes.Node;
import net.civex4.nobility.Nobility;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class WorkerManager {
	HashMap<UUID, Integer> workerMap;
	HashMap<UUID, ActivityLevel> activityMap;

	public final int INACTIVE_WORKERS = 0;
	public final int ACTIVE_WORKERS = 5;
	public final int VERY_ACTIVE_WORKERS = 7;

	public WorkerManager() {
		workerMap = new HashMap<UUID, Integer>();
		activityMap = new HashMap<UUID, ActivityLevel>();
	}

	public int getWorkers(Player p) {
		UUID u = p.getUniqueId();

		if (workerMap.containsKey(u)) {
			return workerMap.get(u);
		} else {
			workerMap.put(u, ACTIVE_WORKERS);
			activityMap.put(u, ActivityLevel.ACTIVE);
			return ACTIVE_WORKERS;
		}
	}

	public boolean spendWorker(Player p) {
		if (workerMap.get(p.getUniqueId()) > 0) {
			int current = workerMap.get(p.getUniqueId());
			current -= 1;
			workerMap.put(p.getUniqueId(), current);
			return true;
		} else return false;
	}

	public void removeWorker(Node node, OfflinePlayer player) throws Exception {
		if (node.removeWorker(player)) {
			workerMap.put(player.getUniqueId(), workerMap.get(player.getUniqueId()) + 1);
		} else {
			throw new Exception("Attempted to Remove worker from node that did not exist.");
		}
	}
	public ActivityLevel getActivityLevel(Player p) {
		return this.activityMap.get(p.getUniqueId());
	}
}
