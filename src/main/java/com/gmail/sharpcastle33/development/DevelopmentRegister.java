package com.gmail.sharpcastle33.development;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class DevelopmentRegister {
    private Class development;
    private String name;
    private Map<String, Integer> cost;
    private Material icon;
    private List<String> prerequisites;

    public DevelopmentRegister(Class development, String name, Map<String, Integer> cost, Material icon, List<String> prerequisites) {
        this.development = development;
        this.name = name;
        this.cost = cost;
        this.icon = icon;
        this.prerequisites = prerequisites;
    } // constructors

    public Class getDevelopment() {
        return development;
    }

    public void setDevelopment(Class development) {
        this.development = development;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getCost() {
        return cost;
    }

    public void setCost(Map<String, Integer> cost) {
        this.cost = cost;
    }

    public Material getIcon() {
        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }
}
