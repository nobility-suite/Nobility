package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;

import com.gmail.sharpcastle33.estate.Estate;

public class Storehouse extends Development {

    static {
        name = "Storehouse";
        cost = new HashMap<>();
        icon = Material.BREAD;
        prerequisites = new ArrayList<>();
    }

    @Override
    public void init(Estate estate) {    	
    	//TODO
    }

    @Override
    public void activate() {
    	//<Temporary Code>
    	//Visual indication storehouse is built
    	Location loc = estate.getBlock().getLocation().add(0, 0, -1);
		loc.getBlock().setType(Material.CHEST);
		//</Temporary Code>
    }

    @Override
    public void deactivate() {
        // TODO
    }
}

