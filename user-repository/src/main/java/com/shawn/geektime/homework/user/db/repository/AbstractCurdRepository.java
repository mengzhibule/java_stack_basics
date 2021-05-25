package com.shawn.geektime.homework.user.db.repository;

import com.shawn.geektime.homework.user.db.annotation.PrimaryKey;
import com.shawn.geektime.homework.user.db.enums.IdGeneratePolicy;
import java.lang.reflect.Field;
import java.util.Objects;

public abstract class AbstractCurdRepository<T, K> implements CurdRepository<T, K> {

  protected String findAllSQlProvider(T t, boolean conditional) {
    Class<?> type = t.getClass();
    String tableName = type.getSimpleName();
    Field[] fields = type.getDeclaredFields();
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    for (Field field : fields) {
      builder.append(field.getName()).append(",");
    }
    builder.deleteCharAt(builder.lastIndexOf(","));
    builder.append(" FROM ");
    builder.append(tableName);
    if (conditional) {
      for (Field field : fields) {
        builder.append(field.getName()).append(" = ? and ");
      }
    }
    return builder.substring(0, builder.lastIndexOf(" and "));
  }

  protected String insertSQLProvider(T t) {
    Class<?> type = t.getClass();
    String tableName = type.getSimpleName();
    Field[] fields = type.getDeclaredFields();
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO ");
    builder.append(tableName);
    builder.append(" (");
    int size = 0;
    for (Field field : fields) {
      PrimaryKey pk = field.getAnnotation(PrimaryKey.class);
      if (!Objects.isNull(pk)) {
        IdGeneratePolicy policy = pk.policy();
        if (!policy.equals(IdGeneratePolicy.AUTO_INCREMENT)) {
          size++;
          builder.append(field.getName()).append(",");
        }
      } else {
        size++;
        builder.append(field.getName()).append(",");
      }
    }
    builder.deleteCharAt(builder.lastIndexOf(","));
    builder.append(") ");
    builder.append("VALUES ");
    builder.append("(");
    for (int i = 0; i < size; i++) {
      if (i != size - 1) {
        builder.append("?,");
      } else {
        builder.append("?");
      }
    }
    builder.append(")");
    return builder.toString();
  }
}
