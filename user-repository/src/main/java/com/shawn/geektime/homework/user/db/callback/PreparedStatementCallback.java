package com.shawn.geektime.homework.user.db.callback;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {

  T doInPreparedStatement(PreparedStatement statement) throws SQLException;
}
