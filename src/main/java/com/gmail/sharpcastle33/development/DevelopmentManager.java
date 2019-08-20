package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DevelopmentManager {
    private List<DevelopmentRegister> types; // List of all types of developments

    public DevelopmentManager() {
        types = new ArrayList<>();
    } // constructor

    public void registerDevelopment(Class development, String name, Map<String, Integer> cost, Material icon, List<String> prerequisites) {
        types.add(new DevelopmentRegister(development, name, cost, icon, prerequisites));
    } // registerDevelopment

    public List<DevelopmentRegister> getTypes() {
        return types;
    } // getTypes
    
} // class
