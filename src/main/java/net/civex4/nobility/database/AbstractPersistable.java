package net.civex4.nobility.database;

public abstract class AbstractPersistable implements Persistable {

	private int primary_oid;

	//All subclasses need default constructor
	public AbstractPersistable() {
	}

	@Override
	public int getOid() {
		return primary_oid;
	}

	@Override
	public void setOid(int primary_oid) {
		this.primary_oid = primary_oid;
	}


}
