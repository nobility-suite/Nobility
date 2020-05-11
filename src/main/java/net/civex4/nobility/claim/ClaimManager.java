package net.civex4.nobility.claim;

import java.util.HashMap;

import io.github.kingvictoria.nodes.Node;
import net.civex4.nobility.estate.Estate;

public class ClaimManager {
	
	public HashMap<Node,Estate> claims;
	
	public ClaimManager() {
		claims = new HashMap<Node,Estate>();

	}
	
	public boolean claim(Node n, Estate e) {
		if(claims.containsKey(n)) {
			if(claims.get(n) != null) {
				return false;
			}else {
				claims.put(n, e);
				return true;
			}
		}else {
			claims.put(n,e);
			return true;
		}
	}
	
	public void unclaim(Node n) {
		claims.put(n, null);
	}
}
