package net.civex4.nobility.database.query;

import net.civex4.nobility.database.Persistable;
import net.civex4.nobility.database.PersistenceManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Query {

	List<QueryParam> queryParams = new ArrayList<>();
	String query;
	int parameterCount;

	public Query(String query) {
		this.query = query;
	}

	public Query addBoolean(Boolean bool) {
		queryParams.add((i, ps) -> ps.setBoolean(i, bool));
		return this;
	}

	public Query addLong(Long l) {
		queryParams.add((i, ps) -> ps.setLong(i, l));
		return this;
	}

	public Query addInt(Integer i) {
		queryParams.add((x, ps) -> ps.setInt(x, i));
		return this;
	}

	public Query addDouble(Double d) {
		queryParams.add((i, ps) -> ps.setDouble(i, d));
		return this;
	}

	public Query addTimestamp(Timestamp t) {
		queryParams.add((i, ps) -> ps.setTimestamp(i, t));
		return this;
	}

	public Query addDate(Date d) {
		queryParams.add((i, ps) -> ps.setDate(i, d));
		return this;
	}

	public <T extends Persistable> List<T> getResult(Class<T> classType) {

		try {

			Connection conn = PersistenceManager.getInstance().getConnection();

			PreparedStatement statement = conn.prepareStatement(query);

			for (int i = 1; i <= queryParams.size(); i++) {

				queryParams.get(i - 1).addParam(i, statement);
			}


			List<T> result = PersistenceManager.getInstance().mapResultSet(statement.executeQuery(), classType);


			statement.close();
			conn.close();

			return result;
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}

	public void executeUpdate() throws SQLException {

		Connection conn = PersistenceManager.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(query);

		queryParams.forEach(qp -> {
			try {
				qp.addParam(parameterCount++, statement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});


		statement.executeUpdate();

		statement.close();
		conn.close();
	}


}
