package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.db.exception.JdbcTemplateException;
import com.shawn.geektime.homework.user.db.util.StatementCreatorUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

  public void execute(final String sql) throws SQLException {
    Statement statement = connection.createStatement();
    statement.execute(sql);
    close(statement);
  }

  public void execute(final String sql, Object[] params) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    if (!Objects.isNull(params) && params.length != 0) {
      for (int i = 0; i < params.length; i++) {
        StatementCreatorUtils.setSqlParamValue(preparedStatement, (i + 1), params[i]);
      }
    }
    preparedStatement.execute();
    close(preparedStatement);
  }

  public <R> List<R> executeQuery(final String sql, RowMapper<R> rowMapper) throws SQLException {
    return executeQuery(sql, null, rowMapper);
  }

  public <R> List<R> executeQuery(final String sql, Object[] params, RowMapper<R> rowMapper)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    if (!Objects.isNull(params) && params.length != 0) {
      for (int i = 0; i < params.length; i++) {
        StatementCreatorUtils.setSqlParamValue(preparedStatement, (i + 1), params[i]);
      }
    }
    ResultSet resultSet = preparedStatement.executeQuery();
    List<R> results = new RowMapperResultSetConvertor<>(rowMapper).convert(resultSet);
    close(resultSet);
    close(preparedStatement);
    return results;
  }

  public <R> List<R> queryForList(final String sql, Object[] params, RowMapper<R> rowMapper)
      throws SQLException {
    return executeQuery(sql, params, rowMapper);
  }

  public <R> R queryForObject(final String sql, Object[] params, RowMapper<R> rowMapper)
      throws SQLException {
    return executeQuery(sql, params, rowMapper).get(0);
  }

  public int executeForDML(final String sql, Object[] params) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    if (!Objects.isNull(params) && params.length != 0) {
      for (int i = 0; i < params.length; i++) {
        StatementCreatorUtils.setSqlParamValue(preparedStatement, (i + 1), params[i]);
      }
    }
    return preparedStatement.executeUpdate();
  }

  public void close(AutoCloseable autoCloseable) {
    try {
      autoCloseable.close();
    } catch (Exception e) {
      throw new JdbcTemplateException("close failed!", e);
    }
  }

  public <R> List<R> extractData(ResultSet rs, RowMapper<R> rowMapper) throws SQLException {
    List<R> results = new ArrayList<>();
    int rowNum = 0;
    while (rs.next()) {
      results.add(rowMapper.mapRow(rs, rowNum++));
    }
    return results;
  }
}
