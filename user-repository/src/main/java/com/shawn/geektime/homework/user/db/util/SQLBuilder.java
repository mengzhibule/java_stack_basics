package com.shawn.geektime.homework.user.db.util;

import static com.shawn.geektime.homework.user.db.enums.SQLScripts.COMMA;
import static com.shawn.geektime.homework.user.db.enums.SQLScripts.INSERT_INTO;
import static com.shawn.geektime.homework.user.db.enums.SQLScripts.LEFT_PARENTHESES;
import static com.shawn.geektime.homework.user.db.enums.SQLScripts.PLACE_HOLDER;
import static com.shawn.geektime.homework.user.db.enums.SQLScripts.RIGHT_PARENTHESES;
import static com.shawn.geektime.homework.user.db.enums.SQLScripts.SPACE;
import static com.shawn.geektime.homework.user.db.enums.SQLScripts.VALUES;
import static javax.persistence.GenerationType.IDENTITY;

import com.shawn.geektime.homework.user.db.entity.EntityColumn;
import com.shawn.geektime.homework.user.db.entity.EntityTable;
import com.shawn.geektime.homework.user.db.entity.SqlStatementParamCreator;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SQLBuilder {

  public static SQLBuilder sqlBuilder;

  private static final String INSERT_SQL_FORMAT =
      INSERT_INTO
          + SPACE
          + "%s"
          + SPACE
          + LEFT_PARENTHESES
          + "%s"
          + RIGHT_PARENTHESES
          + SPACE
          + VALUES
          + SPACE
          + LEFT_PARENTHESES
          + "%s"
          + RIGHT_PARENTHESES;

  private SQLBuilder() {}

  public static SQLBuilder builder() {
    if (sqlBuilder == null) {
      sqlBuilder = new SQLBuilder();
    }
    return sqlBuilder;
  }

  public SqlStatementParamCreator buildInsertSQL(EntityTable entityTable, Object obj) {
    String tableName = entityTable.getTableName();
    List<EntityColumn> entityColumns = entityTable.getEntityColumns();
    StringBuilder columnsBuilder = new StringBuilder();
    List<Object> params = new ArrayList<>();

    try {
      for (int i = 0; i < entityColumns.size(); i++) {
        EntityColumn entityColumn = entityColumns.get(i);
        if (entityColumn.getGenerationType() == IDENTITY) {
          continue;
        }
        Method readMethod = entityColumn.getReadMethod();
        params.add(readMethod.invoke(obj));
        columnsBuilder.append(entityColumn.getColumn());
        if (i != entityColumns.size() - 1) {
          columnsBuilder.append(COMMA);
        }
      }
      BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Object invoke = readMethod.invoke(obj);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    StringBuilder holderBuilder = new StringBuilder();
    for (int i = 0; i < entityColumns.size(); i++) {
      EntityColumn entityColumn = entityColumns.get(i);
      if (entityColumn.getGenerationType() == IDENTITY) {
        continue;
      }
      holderBuilder.append(PLACE_HOLDER);
      if (i != entityColumns.size() - 1) {
        holderBuilder.append(COMMA);
      }
    }
    SqlStatementParamCreator sqlStatementParamCreator = new SqlStatementParamCreator();
    sqlStatementParamCreator.setSql(
        String.format(
            INSERT_SQL_FORMAT, tableName, columnsBuilder.toString(), holderBuilder.toString()));
    sqlStatementParamCreator.setParams(params.toArray());
    return sqlStatementParamCreator;
  }
  //
  //  public <T> SqlStatementParamCreator buildSQLByExample(Example<T> example){
  //    example.
  //  }
}
