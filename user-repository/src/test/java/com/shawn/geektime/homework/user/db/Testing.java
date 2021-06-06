package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.entity.User;
import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Test;

public class Testing {

  @Test
  public void test_array_class() {
    int[] arr = new int[5];
    Assert.assertTrue(arr.getClass().isArray());
  }

  @Test
  public void test_find_query_sql_provider() {
    User user = new User();
    Class<?> type = user.getClass();
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
    builder.append(" WHERE ");
    for (Field field : fields) {
      builder.append(field.getName()).append(" = ? and ");
    }
    System.out.println(builder.substring(0, builder.lastIndexOf(" and ")));
  }

  @Test
  public void test_insert_sql_provider() {
    Class<?> type = User.class;
    String tableName = type.getSimpleName();
    Field[] fields = type.getDeclaredFields();
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO ");
    builder.append(tableName);
    builder.append(" (");
    for (Field field : fields) {
      builder.append(field.getName()).append(",");
    }
    builder.deleteCharAt(builder.lastIndexOf(","));
    builder.append(") ");
    builder.append("VALUES ");
    builder.append("(");
    for (int i = 0; i < fields.length; i++) {
      if (i != fields.length - 1) {
        builder.append("?,");
      } else {
        builder.append("?");
      }
    }
    builder.append(")");
    System.out.println(builder.toString());
  }
}
