package net.civex4.nobility.development;

import java.util.ArrayList;

import net.civex4.nobility.estate.Estate;
/* TODO:
 * Developments need some refactoring. Either the developments
 * are composed of several different features (e.g. stores stuff,
 * collects stuff, etc.), or developments inherit from abstract
 * classes. The features could all be interfaces, and other
 * functions can see if a development is an instance of the
 * interface to check if things like "build()" or "destroy()" 
 * are applicable to it.
 */
public class Development {
	private DevelopmentType development;
	private Developer developer;
	private Estate estate;
	
	public enum Type {
		COLLECTOR, WALL, STOREHOUSE
	}
	
	ArrayList<Type> Types = new ArrayList<>();
	
	private double productivity;
	private int collectionPower;
	private boolean isActive;
		
	public Development(DevelopmentType development, Estate estate) {
		this.setDevelopmentType(development);
		this.productivity = .4d;
		this.setCollectionPower(10);
		
		//TODO: Use a factory method pattern
		if (this.getDevelopmentType().isStorehouse()) {
			this.setDeveloper(new Storehouse(estate, this));
		} else if (this.getDevelopmentType().isCollector()) {
			this.setDeveloper(new Collector(estate, this));
		}
		
	}
	
	public void addType(Type type) {
		Types.add(type);
	}

	public void tick() {
		developer.tick();
	}
	
	public void activate() {
		this.isActive = true;
	}
	
	public void deactivate() {
		this.isActive = false;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public DevelopmentType getDevelopmentType() {
		return development;
	}

	public void setDevelopmentType(DevelopmentType development) {
		this.development = development;
	}

	public double getProductivity() {
		return productivity;
	}

	public void setProductivity(double productivity) {
		this.productivity = productivity;
	}

	public int getCollectionPower() {
		return collectionPower;
	}
	
	public void setCollectionPower(int collectionPower) {
		this.collectionPower = collectionPower;
	}

	public Developer getDeveloper() {
		return developer;
	}


	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}


	public Estate getEstate() {
		return estate;
	}


	public void setEstate(Estate estate) {
		this.estate = estate;
	}

}
