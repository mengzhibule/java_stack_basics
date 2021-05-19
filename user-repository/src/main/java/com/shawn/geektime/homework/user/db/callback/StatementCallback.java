package com.shawn.geektime.homework.user.db.callback;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface StatementCallback<T> {

  T doInStatement(Statement statement) throws SQLException;
}
