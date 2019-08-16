package com.gmail.sharpcastle33.development;

import org.bukkit.inventory.ItemStack;

/* Developments:
 * - name
 * - icon
 * - price
 */
public class Development {
	
	private String name;
	private ItemStack icon;
	private int price;
	private int level;
	
	public Development(String name, ItemStack icon, int price) {
		this.setName(name);
		this.setIcon(icon);
		this.setPrice(price);
		this.level = 1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
}
