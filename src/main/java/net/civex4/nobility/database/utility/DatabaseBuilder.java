package net.civex4.nobility.database.utility;

import net.civex4.nobility.database.query.Query;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseBuilder {

	Map<String, String> schema = new HashMap<>();

	public DatabaseBuilder() {

		schema.put("TEST_TABLE", DatabaseConstants.TABLE_TEST_TABLE);
	}

	public void setUpDatabase() {

		System.out.println("Setting up database...");
		for (String table : schema.keySet()) {

			try {
				new Query(schema.get(table)).executeUpdate();
			} catch (SQLException exc) {
				System.out.println("Failed to create table " + table);
				System.out.println(exc.getMessage());
			}
		}
		System.out.println("Database setup complete..!");

	}

}
