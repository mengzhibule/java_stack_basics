package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.db.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    User user = new User();
    int id = rs.getInt(1);
    String username = rs.getString(2);
    String password = rs.getString(3);
    int age = rs.getInt(4);
    String address = rs.getString(5);
    user.setId(id);
    user.setUsername(username);
    user.setPassword(password);
    user.setAge(age);
    user.setAddress(address);
    return user;
  }
}
