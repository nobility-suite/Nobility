package com.gmail.sharpcastle33.development;

public class Development {
	private DevelopmentType development;
	
	private double productivity;
	private int collectionPower;
	private boolean isActive;
		
	public Development(DevelopmentType development) {
		this.setDevelopmentType(development);
		this.productivity = .4d;
		this.setCollectionPower(10);
	} // constructors
	

	public void tick() {
		if (this.getDevelopmentType().isCollectsFood()) {
			//collect food
		}
		
		if (this.getDevelopmentType().isCollectsResources()) {
			//collect resources
		}
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

}
