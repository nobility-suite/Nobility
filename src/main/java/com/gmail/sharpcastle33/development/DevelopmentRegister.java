package com.gmail.sharpcastle33.development;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DevelopmentRegister {
    private Class development;
    private String name;
    private Map<String, Integer> cost;
    private Material icon;
    private List<String> prerequisites;
    private List<ItemStack> initialCost;
    private double productivity;
    
    public DevelopmentRegister(Class development, String name, Map<String, Integer> cost, Material icon, List<String> prerequisites, List<ItemStack> initialCost) {
        this.development = development;
        this.name = name;
        this.cost = cost;
        this.icon = icon;
        this.prerequisites = prerequisites;
        this.initialCost = initialCost;
        this.productivity = .4d;
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

	public List<ItemStack> getInitialCost() {
		return initialCost;
	}

	public void setInitialCost(List<ItemStack> initialCost) {
		this.initialCost = initialCost;
	}

	public double getProductivity() {
		return productivity;
	}

	public void setProductivity(double productivity) {
		this.productivity = productivity;
	}

}
