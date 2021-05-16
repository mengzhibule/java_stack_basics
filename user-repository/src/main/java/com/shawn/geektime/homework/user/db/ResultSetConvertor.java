package com.shawn.geektime.homework.user.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetConvertor<T> {

  T convert(ResultSet rs) throws SQLException;
}
