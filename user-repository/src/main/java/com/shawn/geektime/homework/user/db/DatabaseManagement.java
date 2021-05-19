package com.shawn.geektime.homework.user.db;

import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManagement {

  private static final String URL = "";
  private static final String USERNAME = "";
  private static final String PASSWORD = "";

  public static void main(String[] args) throws SQLException {
    Driver driver = new Driver();
    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
  }
}
