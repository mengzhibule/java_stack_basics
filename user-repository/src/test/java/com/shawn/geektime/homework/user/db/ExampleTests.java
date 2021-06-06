package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.db.entity.User;
import com.shawn.geektime.homework.user.db.repository.Example;
import java.util.List;
import org.junit.Test;

public class ExampleTests {

  private static final String URL =
      "jdbc:mysql://127.0.0.1:3306/practice?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "123456";
  private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

  @Test
  public void test_query_sql() {
    Example<User> userExample = new Example<>(User.class);
    userExample.andIsNotNull("username").andIsNull("password").andEqualsTo("password", "123456");
    System.out.println(userExample.toQuerySQL());
  }

  @Test
  public void test_query_user_by_primary_key() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.connect(DRIVER_CLASS_NAME, URL, USERNAME, PASSWORD);
    Example<User> userExample = new Example<>(User.class);
    userExample.andEqualsTo("id", 2);
    String s = userExample.toQuerySQL();
    List<Object> paramList = userExample.getParamList();
    User u =
        jdbcTemplate.queryForObject(s, paramList.toArray(), new DefaultRowMapper<>(new User()));
    System.out.println(u.getUsername());
  }
}
