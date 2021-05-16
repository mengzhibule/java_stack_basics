package com.shawn.geektime.homework.user.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetConvertor<T> implements ResultSetConvertor<List<T>> {

  private final RowMapper<T> rowMapper;

  public RowMapperResultSetConvertor(RowMapper<T> rowMapper) {
    this.rowMapper = rowMapper;
  }

  @Override
  public List<T> convert(ResultSet rs) throws SQLException {
    List<T> results = new ArrayList<>();
    int rowNum = 0;
    while (rs.next()) {
      results.add(this.rowMapper.mapRow(rs, rowNum++));
    }
    return results;
  }
}
