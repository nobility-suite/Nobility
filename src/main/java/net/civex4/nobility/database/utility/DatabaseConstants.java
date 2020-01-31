package net.civex4.nobility.database.utility;

public class DatabaseConstants {

	public static final String QUERY_SELECT_START = "SELECT * FROM ";
	public static final String QUERY_UPDATE_START = "UPDATE ";
	public static final String QUERY_INSERT_START = "INSERT INTO ";
	public static final String PRIMARY_OID = "PRIMARY_OID";
	public static final String DATABASE_URL = "jdbc:sqlite:nobilitydb.db";








	/* ############### SQL TABLE DECLARATIONS ############### */

	//Nice guide to SQLite syntax; https://www.sqlitetutorial.net/sqlite-create-table/

	/*RULES
	1) Always specify "IF NOT EXISTS" otherwise database setup could fail every time the mod starts
	2) Make sure to add any new tables to DatabaseBuilder.schema, otherwise it won't be added. Do this in DatabaseBuilder's constructor
	3) All tables must have the column "primary_oid INTEGER PRIMARY KEY"

	*/

	public static final String TABLE_TEST_TABLE = ""
			+ "CREATE TABLE IF NOT EXISTS 'test_table' (" +
			"    'primary_oid' INTEGER PRIMARY KEY," +
			"    'description' TEXT NULL," +
			"    'estate_oid' INTEGER NOT NULL," + // OIDS SHOULD HAVE FOREIGN KEY CONSTRAINT (but this is just a test
			"    'player_oid' INTEGER NOT NULL," +
			"    'name'       TEXT NULL," +
			"    'empty' TEXT NULL" +
			");";

}
