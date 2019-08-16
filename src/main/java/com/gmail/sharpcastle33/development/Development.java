package com.gmail.sharpcastle33.development;

import com.gmail.sharpcastle33.estate.Estate;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public abstract class Development {
	protected Estate estate;
	static protected String name;
	static protected Map<String, Integer> cost;
	static protected Material icon;
	static protected List<String> prerequisites;

	public Development() {
	} // constructor

	/**
	 * This development has been instantiated by a new Estate
	 * @param estate Estate instantiator
	 */
	public abstract void init(Estate estate);

	/**
	 * This development has been constructed (prerequisites met and cost paid)
	 */
	public abstract void activate();

	/**
	 * This development has been deactivated
	 */
	public abstract void deactivate();

	public Estate getEstate() {
		return estate;
	}

	public static String getName() {
		return name;
	}

	public static Map<String, Integer> getCost() {
		return cost;
	}

	public static Material getIcon() {
		return icon;
	}

	public static List<String> getPrerequisites() {
		return prerequisites;
	}
} // class