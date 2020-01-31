package net.civex4.nobility.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface QueryParam {
	void addParam(int i, PreparedStatement ps) throws SQLException;
}
