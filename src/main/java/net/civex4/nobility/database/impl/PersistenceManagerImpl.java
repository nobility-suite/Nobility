package net.civex4.nobility.database.impl;

import net.civex4.nobility.database.query.Query;
import net.civex4.nobility.database.utility.DatabaseConstants;
import net.civex4.nobility.database.Persistable;
import net.civex4.nobility.database.PersistenceManager;
import net.civex4.nobility.database.utility.QueryFactory;
import org.sqlite.SQLiteDataSource;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

public class PersistenceManagerImpl implements PersistenceManager {

	private SQLiteDataSource dataSource;

	//singleton
	public PersistenceManagerImpl() {
		dataSource = new SQLiteDataSource();
		dataSource.setUrl(DatabaseConstants.DATABASE_URL);
	}

	@Override
	public <T extends Persistable> int insertEntity(T entity) {
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement(QueryFactory.buildInsertWithAvailableFields(entity));
			statement.executeUpdate();

			statement.close();
			connection.close();

			//I dont like this because it's not thread safe. Issues should be extremely rare, however causing potentially very damaging data-confusion
			int oid = getLastOid(entity.getTableName());
			entity.setOid(oid);
			return oid;
		} catch (Exception e) {
			System.out.println("Error inserting to; " + entity.getTableName());
			e.printStackTrace();
			return 0;
		}

	}


	private int getLastOid(String table) {
		//This method is garbage, I should fix this
		// Sadly SQLLite doesn't have 'Returning into' so here we are
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement("select max(rowid) from " + table + ";");
			ResultSet rs = statement.executeQuery();

			int oid = rs.getInt(1);

			rs.close();
			statement.close();
			connection.close();

			return oid;
		} catch (SQLException exc) {
			exc.printStackTrace();
			return 0;
		}
	}

	@Override
	public <T extends Persistable> void updateEntity(T entity) {
		updateEntity(entity, false);
	}

	@Override
	public <T extends Persistable> void updateEntity(T entity, boolean useNulls) {
		try {
			//If entity is missing an oid, this method should not be used to update
			assert entity.getOid() != 0;

			Connection connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement(QueryFactory.buildUpdateWithAvailableFields(entity, useNulls));

			statement.executeUpdate();
			statement.close();
			connection.close();
		} catch (Exception exc) {
			System.out.println("Error updating table; " + entity.getTableName());
			exc.printStackTrace();
		}
	}

	@Override
	public <T extends Persistable> List<T> readEntity(T entity) {

		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(QueryFactory.buildQueryWithAvailableFields(entity));
			// T in mapResultSet extends Persistable, so this cast should be safe
			List<T> results = (List<T>) mapResultSet(statement.executeQuery(), entity.getClass());

			statement.close();
			connection.close();
			return results;
		} catch (Exception exc) {
			System.err.println("Couldn't query " + entity.getTableName()); //TODO proper logging
			exc.printStackTrace();
			return Collections.EMPTY_LIST;
		}

	}

	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}


	public <T extends Persistable> List<T> mapResultSet(ResultSet rs, Class<T> type) throws IllegalAccessException, NoSuchFieldException, SQLException, InvocationTargetException, InstantiationException, NoSuchMethodException {

		ArrayList<T> results = new ArrayList<>();

		ResultSetMetaData rsmd = rs.getMetaData();
		while (rs.next()) {
			//Unchecked cast but at this point it must always be subClass of Persistable, so this SHOULD be okay...
			T resultElement = type.getDeclaredConstructor().newInstance();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String columnName = rsmd.getColumnName(i);

				if (columnName.equalsIgnoreCase("primary_oid")) {
					resultElement.setOid(rs.getInt(i));
					continue;
				}


				Field f = resultElement.getClass().getDeclaredField(convertSnakeCasetoCamelCase(columnName));
				f.setAccessible(true);
				f.set(resultElement, rs.getObject(i));

			}
			results.add(resultElement);
		}

		rs.close();
		return results;
	}

	private String convertSnakeCasetoCamelCase(String snakeCase) {
		StringBuilder camelCase = new StringBuilder();

		boolean capitalizeNext = false;

		for (char c : snakeCase.toCharArray()) {
			if (c == '_') {
				capitalizeNext = true;
			} else {
				if (capitalizeNext) {
					camelCase.append(Character.toUpperCase(c));
					capitalizeNext = false;
				} else {
					camelCase.append(Character.toLowerCase(c));
				}
			}
		}

		return camelCase.toString();
	}

	@Override
	public Query createQuery(String sql) {
		return new Query(sql);
	}

}
