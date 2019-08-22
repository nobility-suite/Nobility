package com.gmail.sharpcastle33.development;

import com.gmail.sharpcastle33.estate.Estate;

public abstract class Development {
	protected Estate estate;
	protected DevelopmentRegister register;
	protected boolean active;

	/**
	 * Empty constructor required to instantiate arbitrary development from Class object
	 */
	public Development() {
	} // constructor

	/**
	 * Initialization common to all developments
	 * @param register
	 */
	public void pre_init(DevelopmentRegister register, Estate estate) {
		this.register = register;
		this.estate = estate;
	}

	/**
	 * This development has been instantiated by a new Estate
	 */
	public abstract void init();

	/**
	 * This development has been constructed (prerequisites met and cost paid)
	 */
	public abstract void activate();

	/**
	 * This development has been deactivated
	 */
	public abstract void deactivate();
	
	/**
	 * This development has ticked
	 * 
	 */
	public abstract void tick();

	public Estate getEstate() {
		return estate;
	}

	public DevelopmentRegister getRegister() { return register; }

	public boolean isActive() { return active; }

	public void setActive(boolean value) { active = value; }

	public String getName() { return register.getName(); }
	
} // class