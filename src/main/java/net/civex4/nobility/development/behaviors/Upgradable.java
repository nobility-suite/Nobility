package net.civex4.nobility.development.behaviors;

public interface Upgradable {
	int getLevel();
	void upgrade();
	void setLevel(int level);
}
