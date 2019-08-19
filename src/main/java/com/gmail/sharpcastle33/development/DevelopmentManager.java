package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DevelopmentManager {
    private List<Class> types; // List of all types of developments

    public DevelopmentManager() {
        types = new ArrayList<>();
    } // constructor

    /**
     * Register a Development with Nobility
     * @param type Class type of Development
     */
    public void registerDevelopment(Class type) {
        types.add(type);
    } // registerDevelopment

    public List<Class> getTypes() {
        return types;
    } // getTypes
    
    public void openDevelopmentMenu(Player player, Block block) {
    	
    	for (int i = 0; i < this.types.size(); i++) {
    		//types.get(i).getIcon();
    	}    	
    }
    
} // class
