package com.shawn.geektime.homework.user.db.util;

import com.shawn.geektime.homework.user.db.entity.EntityColumn;
import com.shawn.geektime.homework.user.db.entity.EntityTable;
import com.shawn.geektime.homework.user.db.entity.SqlStatementParam;
import com.shawn.geektime.homework.user.db.enums.SQLScripts;
import java.util.List;
import javax.persistence.GenerationType;

public class SQLBuilder {

  public static SQLBuilder sqlBuilder;

  private SQLBuilder() {}

  public static SQLBuilder builder() {
    if (sqlBuilder == null) {
      sqlBuilder = new SQLBuilder();
    }
    return sqlBuilder;
  }

  public SqlStatementParam buildInsertSQL(EntityTable entityTable) {
    StringBuilder builder = new StringBuilder();
    String tableName = entityTable.getTableName();
    List<EntityColumn> entityColumns = entityTable.getEntityColumns();
    builder
        .append(SQLScripts.INSERT_INTO)
        .append(SQLScripts.SPACE)
        .append(tableName)
        .append(SQLScripts.SPACE)
        .append(SQLScripts.LEFT_PARENTHESES);
    for (int i = 0; i < entityColumns.size(); i++) {
      EntityColumn entityColumn = entityColumns.get(i);
      if (entityColumn.getGenerationType() == GenerationType.IDENTITY) {
        continue;
      }
      builder.append(entityColumn.getColumn());
      if (i != entityColumns.size() - 1) {
        builder.append(SQLScripts.COMMA);
      }
    }
    builder
        .append(SQLScripts.RIGHT_PARENTHESES)
        .append(SQLScripts.SPACE)
        .append(SQLScripts.VALUES)
        .append(SQLScripts.SPACE)
        .append(SQLScripts.LEFT_PARENTHESES);
    for (int i = 0; i < entityColumns.size(); i++) {
      EntityColumn entityColumn = entityColumns.get(i);
      if (entityColumn.getGenerationType() == GenerationType.IDENTITY) {
        continue;
      }
      builder.append(SQLScripts.PLACE_HOLDER);
      if (i != entityColumns.size() - 1) {
        builder.append(SQLScripts.COMMA);
      }
    }
    builder.append(SQLScripts.RIGHT_PARENTHESES);
    SqlStatementParam sqlStatementParam = new SqlStatementParam();
    sqlStatementParam.setSql(builder.toString());
    return sqlStatementParam;
  }
}
