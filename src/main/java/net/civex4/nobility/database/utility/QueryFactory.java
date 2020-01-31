package net.civex4.nobility.database.utility;

import net.civex4.nobility.database.Persistable;
import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.Field;

public class QueryFactory {

	public static <T extends Persistable> String buildQueryWithAvailableFields(T entity) throws IllegalAccessException {
		StringBuilder query = new StringBuilder(DatabaseConstants.QUERY_SELECT_START + entity.getTableName() + " WHERE ");

		for (Field field : entity.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (null != field.get(entity) && isPrimitiveOrWrapper(field)) {
				query.append(convertCamelCasetoSnakeCase(field.getName())).append(" = ").append(formatString(field.get(entity))).append(" AND ");
			}
		}
		query.delete(query.length() - 5, query.length()).append(";"); // remove the last comma
		return query.toString();
	}

	/*
	@param updateNulls if true, update will include null values, meaning possibly erasing data
	 */
	public static <T extends Persistable> String buildUpdateWithAvailableFields(T entity, boolean updateNulls) throws IllegalAccessException {

		StringBuilder query = new StringBuilder(DatabaseConstants.QUERY_UPDATE_START).append(entity.getTableName()).append(" SET ");
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];

			field.setAccessible(true);
			if (updateNulls || null != field.get(entity) && isPrimitiveOrWrapper(field)) {

				query.append(convertCamelCasetoSnakeCase(field.getName()));
				query.append(" = ").append(formatString(field.get(entity))).append(", ");


			}

		}

		query.deleteCharAt(query.length() - 2); // remove the last comma

		query.append("WHERE ").append(DatabaseConstants.PRIMARY_OID).append(" = ").append(entity.getOid()).append(";");
		System.out.println(query);
		return query.toString();

	}

	public static <T extends Persistable> String buildInsertWithAvailableFields(T entity) throws IllegalAccessException {


		StringBuilder query = new StringBuilder(DatabaseConstants.QUERY_INSERT_START).append(entity.getTableName()).append("( ");

		StringBuilder valuesClause = new StringBuilder("VALUES (");

		Field[] fields = entity.getClass().getDeclaredFields();


		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);

			if (isPrimitiveOrWrapper(field)) {
				query.append(convertCamelCasetoSnakeCase(field.getName())).append(", ");

				valuesClause.append(formatString(field.get(entity))).append(", ");
			}

		}
		query.deleteCharAt(query.length() - 2); // remove the last comma
		valuesClause.deleteCharAt(valuesClause.length() - 2); // remove the last comma

		query.append(") ");
		valuesClause.append(") ");

		query.append(valuesClause).append(";");
		return query.toString();

	}


	private static String convertCamelCasetoSnakeCase(String camelCase) {
		StringBuilder snakeCase = new StringBuilder();

		for (char c : camelCase.toCharArray()) {
			if (Character.isUpperCase(c)) {
				snakeCase.append("_");
			}
			snakeCase.append(Character.toLowerCase(c));
		}
		return snakeCase.toString();
	}


	private static Object formatString(Object field) {
		if (field instanceof String) {
			return "'" + field + "'";
		} else {
			return field;
		}
	}

	private static boolean isPrimitiveOrWrapper(Field f) {

		return f.getType().isPrimitive() || ClassUtils.wrapperToPrimitive(f.getType()) != null || (f.getType().getSimpleName().equals(String.class.getSimpleName()));

	}

}
