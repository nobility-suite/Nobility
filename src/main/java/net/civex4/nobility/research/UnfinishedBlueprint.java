package net.civex4.nobility.research;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

import net.civex4.nobility.blueprints.AbstractBlueprint;
import net.civex4.nobility.blueprints.Blueprint;

public class UnfinishedBlueprint {
	
	private AbstractBlueprint baseBlueprint;
	private ArrayList<Card> actions;
	private Random seed;
	private int rounds;
	private int maxRounds;
	
	public UnfinishedBlueprint(AbstractBlueprint recipe) {
		this.seed = new Random();
		this.baseBlueprint = recipe;
	}
	
	public UnfinishedBlueprint parseFromItem(ItemStack i) {
		return null;
	}
	
	public ItemStack parseToItem() {
		return null;
	}
	
	public Blueprint generate() {
		return null;
	}
	
	public ArrayList<Card> getActions(){
		return this.actions;
	}
}
