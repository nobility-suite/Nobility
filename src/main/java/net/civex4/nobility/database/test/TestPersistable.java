package net.civex4.nobility.database.test;

import net.civex4.nobility.database.AbstractPersistable;

public class TestPersistable extends AbstractPersistable {

	public String getTableName() {
		return "TEST_TABLE";
	}

	private String description;
	private Integer estateOid;
	private Integer playerOid;
	private String name;
	private Integer empty;
	private Object nonPrimOrWrap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEstateOid() {
		return estateOid;
	}

	public void setEstateOid(int estateOid) {
		this.estateOid = estateOid;
	}

	public int getPlayerOid() {
		return playerOid;
	}

	public void setPlayerOid(int playerOid) {
		this.playerOid = playerOid;
	}

	public int getEmpty() {
		return empty;
	}

	public void setEmpty(int empty) {
		this.empty = empty;
	}

	public Object getNonPrimOrWrap() {
		return nonPrimOrWrap;
	}

	public void setNonPrimOrWrap(Object nonPrimOrWrap) {
		this.nonPrimOrWrap = nonPrimOrWrap;
	}
}
