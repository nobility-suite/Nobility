package net.civex4.nobility.database;

import net.civex4.nobility.database.impl.PersistenceManagerImpl;
import net.civex4.nobility.database.query.Query;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface PersistenceManager {

	PersistenceManager persistenceManager = new PersistenceManagerImpl();

	<T extends Persistable> int insertEntity(T entity);

	<T extends Persistable> void updateEntity(T entity);

	<T extends Persistable> void updateEntity(T entity, boolean useNulls);

	<T extends Persistable> List<T> readEntity(T entity);

	<T extends Persistable> List<T> mapResultSet(ResultSet rs, Class<T> type) throws IllegalAccessException, NoSuchFieldException, SQLException, InvocationTargetException, InstantiationException, NoSuchMethodException;

	static PersistenceManager getInstance() {

		return persistenceManager;
	}

	Connection getConnection() throws SQLException;

	Query createQuery(String sql);

}
