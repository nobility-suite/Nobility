package com.gmail.sharpcastle33.development;

import com.gmail.sharpcastle33.estate.Estate;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public abstract class Development {
	protected Estate estate;
	protected DevelopmentRegister register;

	public Development() {
	} // constructor

	public void setRegister(DevelopmentRegister register) {
		this.register = register;
	}

	/**
	 * This development has been instantiated by a new Estate
	 * @param estate Estate instantiator
	 */
	public abstract void init(Estate estate);

	/**
	 * This development has been constructed (prerequisites met and cost paid)
	 */
	public abstract void activate(Estate estate);

	/**
	 * This development has been deactivated
	 */
	public abstract void deactivate();

	public Estate getEstate() {
		return estate;
	}

	public String getName() {
		return register.getName();
	}

	public Map<String, Integer> getCost() {
		return register.getCost();
	}

	public Material getIcon() {
		return register.getIcon();
	}
	
	public List<String> getPrerequisites() {
    	return register.getPrerequisites();
	}
	
	public void addPrerequisite(String development) {
		register.getPrerequisites().add(development);
	}
	
	public void setActive(boolean b) {
		register.setActive(b);
	}
	
	public boolean getActive() {
		return register.getActive();
	}
	
} // class