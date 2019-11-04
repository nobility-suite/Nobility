package net.civex4.nobility.development;

import net.civex4.nobility.estate.Estate;

public class Development {
	private DevelopmentType development;
	private Developer developer;
	private Estate estate;
	
	private double productivity;
	private int collectionPower;
	private boolean isActive;
		
	public Development(DevelopmentType development, Estate estate) {
		this.setDevelopmentType(development);
		this.productivity = .4d;
		this.setCollectionPower(10);
		
		
		if (this.getDevelopmentType().isStorehouse()) {
			this.setDeveloper(new Storehouse(estate, this));
		} else if (this.getDevelopmentType().isCollector()) {
			this.setDeveloper(new Collector(estate, this));
		}
		
	}
	

	public void tick() {
		developer.tick();
	}
	
	public void activate() {
		setActive(true);
	}
	
	public void deactivate() {
		setActive(false);
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
