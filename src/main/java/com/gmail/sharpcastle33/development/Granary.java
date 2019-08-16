package com.gmail.sharpcastle33.development;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.estate.Estate;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class Granary extends Development {

    static {
        name = "Granary";
        cost = new HashMap<>();
        icon = Material.BREAD;
        prerequisites = new ArrayList<>();
    }

    @Override
    public void init(Estate estate) {
        // TODO
    }

    @Override
    public void activate() {
        // TODO
    }

    @Override
    public void deactivate() {
        // TODO
    }
}
