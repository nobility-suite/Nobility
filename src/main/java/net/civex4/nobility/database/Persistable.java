package net.civex4.nobility.database;

public interface Persistable {

	String getTableName();

	int getOid();

	void setOid(int oid);
}
