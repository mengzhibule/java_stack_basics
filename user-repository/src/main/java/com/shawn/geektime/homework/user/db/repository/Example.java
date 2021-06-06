package com.shawn.geektime.homework.user.db.repository;

import com.shawn.geektime.homework.user.db.exception.JdbcTemplateException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

public class Example<T> {

  private final Class<? extends T> entityClass;

  private final String tableName;

  private final Map<String, String> propertyColumnMap = new HashMap<>();

  private final Map<String, String> columnPropertyMap = new HashMap<>();

  private final List<String> columnList = new ArrayList<>();

  private final List<Object> paramList = new ArrayList<>();

  private final List<WhereSQL> whereSQLs = new ArrayList<>();

  public Example(Class<? extends T> entityClass) {
    this.entityClass = entityClass;
    checkEntity();
    this.tableName = entityTable();
    processField();
  }

  private void checkEntity() {
    if (!entityClass.isAnnotationPresent(Entity.class)) {
      throw new JdbcTemplateException("This class is not an entity class");
    }
  }

  private String entityTable() {
    Table tableAnnotation = entityClass.getAnnotation(Table.class);
    if (tableAnnotation == null || StringUtils.isBlank(tableAnnotation.name())) {
      return entityClass.getSimpleName();
    }
    return tableAnnotation.name();
  }

  private void processField() {
    Field[] fields = entityClass.getDeclaredFields();
    for (Field field : fields) {
      // exclude static and transient fields
      if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
        continue;
      }
      Column column = field.getAnnotation(Column.class);
      String columnName;
      String propertyName = field.getName();
      if (Objects.isNull(column) || StringUtils.isBlank(column.name())) {
        columnName = field.getName();
      } else {
        columnName = column.name();
      }
      propertyColumnMap.put(propertyName, columnName);
      columnPropertyMap.put(columnName, propertyName);
      columnList.add(columnName);
    }
  }

  public Example<T> andIsNull(String property) {
    String column = propertyColumnMap.get(property);
    if (StringUtils.isBlank(column)) {
      throw new RuntimeException(String.format("The property [%s] do not map to column", property));
    }
    addWhere(column + " is null", false);
    return this;
  }

  public Example<T> andIsNotNull(String property) {
    String column = propertyColumnMap.get(property);
    if (StringUtils.isBlank(column)) {
      throw new RuntimeException(String.format("The property [%s] do not map to column", property));
    }
    addWhere(column + " is not null", false);
    return this;
  }

  public Example<T> andEqualsTo(String property, Object value) {
    String column = propertyColumnMap.get(property);
    if (StringUtils.isBlank(column) || !columnList.contains(column)) {
      throw new RuntimeException(String.format("The property [%s] do not map to column", property));
    }
    addWhere(column + " = ?", value, false);
    return this;
  }

  public Example<T> orIsNull(String property) {
    String column = propertyColumnMap.get(property);
    if (StringUtils.isBlank(column)) {
      throw new RuntimeException(String.format("The property [%s] do not map to column", property));
    }
    addWhere(column + " is null", true);
    return this;
  }

  public Example<T> orIsNotNull(String property) {
    String column = propertyColumnMap.get(property);
    if (StringUtils.isBlank(column)) {
      throw new RuntimeException(String.format("The property [%s] do not map to column", property));
    }
    addWhere(column + " is not null", true);
    return this;
  }

  public Example<T> orEqualsTo(String property, Object value) {
    String column = propertyColumnMap.get(property);
    if (StringUtils.isBlank(column) || !columnList.contains(column)) {
      throw new RuntimeException(String.format("The property [%s] do not map to column", property));
    }
    addWhere(column + " = ?", value, true);
    return this;
  }

  public void addWhere(String condition, boolean isOr) {
    whereSQLs.add(new WhereSQL(condition, isOr));
  }

  public void addWhere(String condition, Object value, boolean isOr) {
    whereSQLs.add(new WhereSQL(condition, value, isOr));
    paramList.add(value);
  }

  public String toQuerySQL() {
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT ");
    columnList.forEach(column -> sqlBuilder.append(column).append(","));
    sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(" FROM ");
    sqlBuilder.append(tableName);
    if (!whereSQLs.isEmpty()) {
      // Where statement or not
      boolean hasWhereStatement = (sqlBuilder.indexOf(" where ") != -1);
      // if not
      if (!hasWhereStatement) {
        sqlBuilder.append(" where ");
      }
      StringBuilder whereSQLBuilder = new StringBuilder();
      for (int i = 0; i < whereSQLs.size(); i++) {
        WhereSQL whereSQL = whereSQLs.get(i);
        if (i == 0 && !hasWhereStatement) {
          boolean isOr = whereSQL.isOr;
          if (isOr) {
            whereSQLBuilder.append(whereSQL.toWhereSQL().substring(" or ".length()));
          } else {
            whereSQLBuilder.append(whereSQL.toWhereSQL().substring(" and ".length()));
          }
        } else {
          whereSQLBuilder.append(whereSQL.toWhereSQL());
        }
      }
      sqlBuilder.append(whereSQLBuilder);
    }
    return sqlBuilder.toString();
  }

  public List<WhereSQL> getWhereSQLs() {
    return whereSQLs;
  }

  public List<Object> getParamList() {
    return paramList;
  }

  private static class WhereSQL {
    private String condition;
    private Object value;
    private boolean listValue;
    private boolean isOr;

    public WhereSQL(String condition, boolean isOr) {
      this.condition = condition;
      this.isOr = isOr;
    }

    public WhereSQL(String condition, Object value, boolean isOr) {
      this.condition = condition;
      this.value = value;
      this.isOr = isOr;
      this.listValue = value instanceof Collection;
    }

    public String getCondition() {
      return condition;
    }

    public void setCondition(String condition) {
      this.condition = condition;
    }

    public Object getValue() {
      return value;
    }

    public void setValue(Object value) {
      this.value = value;
    }

    public boolean isOr() {
      return isOr;
    }

    public void setOr(boolean or) {
      isOr = or;
    }

    public boolean isListValue() {
      return listValue;
    }

    public void setListValue(boolean listValue) {
      this.listValue = listValue;
    }

    public String toWhereSQL() {
      if (isOr) {
        return " or " + condition;
      } else {
        return " and " + condition;
      }
    }
  }
}
