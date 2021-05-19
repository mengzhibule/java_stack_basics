package com.shawn.geektime.homework.user.db.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCreator {

  PreparedStatement createPreparedStatement(Connection con) throws SQLException;
}
