package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;

import com.gmail.sharpcastle33.estate.Estate;

public class Granary extends Development {

    @Override
    public void init() {
        // TODO this.addPrerequisite("Storehouse");
    }

    @Override
    public void activate() {
    	Location loc = estate.getBlock().getLocation().add(0, 0, 1);
		loc.getBlock().setType(Material.HAY_BLOCK);
    }

    @Override
    public void deactivate() {
        // TODO
    }
}
