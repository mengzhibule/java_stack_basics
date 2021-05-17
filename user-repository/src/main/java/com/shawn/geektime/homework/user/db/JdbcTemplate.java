package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.db.callback.PreparedStatementCallback;
import com.shawn.geektime.homework.user.db.callback.StatementCallback;
import com.shawn.geektime.homework.user.db.exception.JdbcTemplateException;
import com.shawn.geektime.homework.user.db.function.PreparedStatementCreator;
import com.shawn.geektime.homework.user.db.util.StatementCreatorUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

/**
 * All SQL operations
 *
 * @author Shawn
 * @since 1.0
 */
public class JdbcTemplate {

  private Connection connection;

  public void connect(String url, String username, String password, String driverClassName) {
    try {
      Class.forName(driverClassName);
      connection = DriverManager.getConnection(url, username, password);
    } catch (SQLException | ClassNotFoundException e) {
      throw new JdbcTemplateException(e);
    }
  }

  public void execute(final String sql) {
    execute(statement -> statement.execute(sql));
  }

  public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> callback) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = psc.createPreparedStatement(connection);
      return callback.doInPreparedStatement(preparedStatement);
    } catch (SQLException e) {
      throw new JdbcTemplateException("execute PreparedStatement callback function failed: ", e);
    } finally {
      close(preparedStatement);
    }
  }

  public <T> T execute(StatementCallback<T> callback) {
    Statement statement = null;
    try {
      statement = connection.createStatement();
      return callback.doInStatement(statement);
    } catch (SQLException e) {
      throw new JdbcTemplateException("execute statement callback function failed: ", e);
    } finally {
      close(statement);
    }
  }

  public <R> List<R> executeQuery(final String sql, RowMapper<R> rowMapper) {
    return executeQuery(sql, null, rowMapper);
  }

  public <R> List<R> executeQuery(final String sql, Object[] params, RowMapper<R> rowMapper) {
    return execute(
        con -> {
          PreparedStatement preparedStatement = con.prepareStatement(sql);
          if (!Objects.isNull(params) && params.length != 0) {
            for (int i = 0; i < params.length; i++) {
              StatementCreatorUtils.setSqlParamValue(preparedStatement, (i + 1), params[i]);
            }
          }
          return preparedStatement;
        },
        statement -> {
          ResultSet resultSet = statement.executeQuery();
          List<R> results = new RowMapperResultSetConvertor<>(rowMapper).convert(resultSet);
          close(resultSet);
          return results;
        });
  }

  public <R> List<R> queryForList(final String sql, Object[] params, RowMapper<R> rowMapper) {
    return executeQuery(sql, params, rowMapper);
  }

  public <R> R queryForObject(final String sql, Object[] params, RowMapper<R> rowMapper) {
    List<R> rs = queryForList(sql, params, rowMapper);
    if (Objects.isNull(rs) || rs.size() == 0) {
      throw new JdbcTemplateException(
          String.format("Incorrect result size: expected [%d], actual [%d]", 1, 0));
    } else if (rs.size() > 1) {
      throw new JdbcTemplateException(
          String.format("Incorrect result size: expected [%d], actual [%d]", 1, rs.size()));
    }
    return rs.iterator().next();
  }

  public int update(final String sql, Object[] params) {
    return executeForDML(
        con -> {
          PreparedStatement preparedStatement = con.prepareStatement(sql);
          if (!Objects.isNull(params) && params.length != 0) {
            for (int i = 0; i < params.length; i++) {
              StatementCreatorUtils.setSqlParamValue(preparedStatement, (i + 1), params[i]);
            }
          }
          return preparedStatement;
        },
        PreparedStatement::executeUpdate);
  }

  public int executeForDML(
      PreparedStatementCreator psc, PreparedStatementCallback<Integer> callback) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = psc.createPreparedStatement(connection);
      return callback.doInPreparedStatement(preparedStatement);
    } catch (SQLException e) {
      throw new JdbcTemplateException(
          "execute DML preparedStatement callback function failed, ", e);
    } finally {
      close(preparedStatement);
    }
  }

  public void close(AutoCloseable autoCloseable) {
    try {
      if (autoCloseable != null) {
        autoCloseable.close();
      }
    } catch (Exception e) {
      throw new JdbcTemplateException("close failed!", e);
    }
  }
}
