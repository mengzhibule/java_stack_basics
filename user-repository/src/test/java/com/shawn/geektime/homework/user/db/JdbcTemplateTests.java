package com.shawn.geektime.homework.user.db;

import org.junit.Before;

public class JdbcTemplateTests {

  private JdbcTemplate jdbcTemplate;
  private static final String URL = "";
  private static final String USERNAME = "";
  private static final String PASSWORD = "";
  private static final String DRIVER_CLASS_NAME = "";

  private static final String CREATE_USER_TABLE =
      new StringBuilder("CREATE TABLE t_user(")
          .append(" id INT PRIMARY KEY AUTO").toString();

  @Before
  public void test_create_connection(){
    jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.connect(URL, USERNAME, PASSWORD, DRIVER_CLASS_NAME);
  }

  public void test_create_table(){
    jdbcTemplate.execute(CREATE_USER_TABLE);
  }

}
