package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.db.entity.EntityTable;
import com.shawn.geektime.homework.user.db.entity.SqlStatementParamCreator;
import com.shawn.geektime.homework.user.db.util.EntityUtils;
import com.shawn.geektime.homework.user.db.util.SQLBuilder;
import com.shawn.geektime.homework.user.entity.User;
import org.junit.Before;
import org.junit.Test;

public class SQLBuilderTests {

  private EntityTable entityTable;

  @Before
  public void setUp() {
    entityTable = EntityUtils.getEntityTable(User.class);
  }

  @Test
  public void test_build_insert_sql() {
    User user = new User();
    user.setId(1);
    user.setUsername("Shawn");
    user.setPassword("123456");
    user.setAge(26);
    user.setAddress("SHANGHAI");
    SqlStatementParamCreator sql = SQLBuilder.builder().buildInsertSQL(entityTable, user);
    System.out.println(sql);
  }
}
