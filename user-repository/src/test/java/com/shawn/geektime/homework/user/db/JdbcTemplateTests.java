package com.shawn.geektime.homework.user.db;

import java.util.List;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class JdbcTemplateTests {

  private JdbcTemplate jdbcTemplate;
  private static final String URL =
      "jdbc:mysql://127.0.0.1:3306/practice?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "123456";
  private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

  private static final String CREATE_USER_TABLE =
      "CREATE TABLE user("
          + " id int(11) PRIMARY KEY  NOT NULL AUTO_INCREMENT, "
          + " username varchar(45) NOT NULL, "
          + " password varchar(45) NOT NULL, "
          + " age int(11) NOT NULL, "
          + " address varchar(45) NOT NULL"
          + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

  private static final String INSERT_USER_RECORD =
      "INSERT INTO user (username, password, age, address) VALUES (?, ?, ?, ?)";

  private static final String QUERY_USER_BY_PRIMARY_KEY = "SELECT * FROM user WHERE id = ?";

  private static final String UPDATE_USER_PASSWORD = "UPDATE user SET password = ? WHERE id = ?";

  private static final String DELETE_USER_RECORD = "DELETE FROM user WHERE id = ?";

  @Before
  public void test_create_connection() {
    jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.connect();
  }

  @Test
  public void test_create_table() {
    jdbcTemplate.execute(CREATE_USER_TABLE);
  }

  @Test
  public void test_insert() {
    Object[] params = new Object[] {"Shawn", "123456", 26, "SHANGHAI"};
    int count = jdbcTemplate.update(INSERT_USER_RECORD, params);
    Assert.assertEquals(1, count);
  }

  @Test
  public void test_query_for_object() {
    Object[] params = new Object[] {1};
    User u = jdbcTemplate.queryForObject(QUERY_USER_BY_PRIMARY_KEY, params, new UserRowMapper());
    Assert.assertEquals(1, u.getId());
  }

  @Test
  public void test_update() {
    Object[] params = new Object[] {"654321", 1};
    int count = jdbcTemplate.update(UPDATE_USER_PASSWORD, params);
    Assert.assertEquals(1, count);
    Object[] query_params = new Object[] {1};
    User u =
        jdbcTemplate.queryForObject(QUERY_USER_BY_PRIMARY_KEY, query_params, new UserRowMapper());
    Assert.assertEquals("654321", u.getPassword());
  }

  @Test
  public void test_delete() {
    Object[] delete_params = new Object[] {1};
    int count = jdbcTemplate.update(DELETE_USER_RECORD, delete_params);
    Assert.assertEquals(1, count);
    Object[] query_params = new Object[] {1};
    List<User> users =
        jdbcTemplate.queryForList(QUERY_USER_BY_PRIMARY_KEY, query_params, new UserRowMapper());
    Assert.assertTrue(Objects.isNull(users) || users.size() == 0);
  }
}
