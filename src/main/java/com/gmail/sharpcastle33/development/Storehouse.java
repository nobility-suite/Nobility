package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;

import com.gmail.sharpcastle33.estate.Estate;

public class Storehouse extends Development {

    @Override
    public void init() {
    	//TODO
    }

    @Override
    public void activate() {
    	//<Temporary Code>
    	//Visual indication storehouse is built
    	Location loc = estate.getBlock().getLocation().add(1, 0, 0);
		loc.getBlock().setType(Material.CHEST);
		//</Temporary Code>
    }

    @Override
    public void deactivate() {
        // TODO
    }

	@Override
	public void tick() {
		// TODO		
	}
    
}

