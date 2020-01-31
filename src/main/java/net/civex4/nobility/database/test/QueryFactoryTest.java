package net.civex4.nobility.database.test;

import net.civex4.nobility.database.utility.QueryFactory;
import org.junit.Test;
import org.junit.Assert;

public class QueryFactoryTest {

	TestPersistable testPersistable = new TestPersistable();

	{ // set up test info
		testPersistable.setOid(1);
		testPersistable.setDescription("test description");
		testPersistable.setEstateOid(123);
		testPersistable.setPlayerOid(312);
		testPersistable.setName("Test");
	}

	@Test
	public void testInsertBuild() throws Exception {

		String sql = QueryFactory.buildInsertWithAvailableFields(testPersistable);

		Assert.assertEquals(
				"INSERT INTO TEST_TABLE( description, estate_oid, player_oid, name, empty ) VALUES ('test description', 123, 312, 'Test', null ) ;",
				sql);
	}

	@Test
	public void testUpdateBuildNoNulls() throws Exception {
		String sql = QueryFactory.buildUpdateWithAvailableFields(testPersistable, false);

		Assert.assertEquals(
				"UPDATE TEST_TABLE SET description = 'test description', estate_oid = 123, player_oid = 312, name = 'Test' WHERE PRIMARY_OID = 1;",
				sql);
	}

	@Test
	public void testUpdateBuildWithNulls() throws Exception {
		String sql = QueryFactory.buildUpdateWithAvailableFields(testPersistable, true);

		Assert.assertEquals(
				"UPDATE TEST_TABLE SET description = 'test description', estate_oid = 123, player_oid = 312, name = 'Test', empty = null, non_prim_or_wrap = null WHERE PRIMARY_OID = 1;",
				sql);
	}

	@Test
	public void testQueryBuild() throws Exception {
		String sql = QueryFactory.buildQueryWithAvailableFields(testPersistable);

		Assert.assertEquals("SELECT * FROM TEST_TABLE WHERE description = 'test description' AND estate_oid = 123 AND player_oid = 312 AND name = 'Test';"
				, sql);

	}
}
