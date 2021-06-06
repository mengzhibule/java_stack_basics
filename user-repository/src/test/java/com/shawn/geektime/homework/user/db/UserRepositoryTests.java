package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.db.repository.UserRepository;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.junit.Test;

public class UserRepositoryTests {

  @Test
  public void test_generate_insert_sql() {
    Class<?> repositoryClass = UserRepository.class;
    Method[] methods = repositoryClass.getMethods();
    for (Method method : methods) {
      String name = method.getName();
      if ("insert".equals(name)) {
        Parameter[] parameters = method.getParameters();
        Parameter parameter = parameters[0];
        Class<?> type = parameter.getType();
        String simpleName = type.getSimpleName();
        Field[] fields = type.getDeclaredFields();
        String sql = "Insert into " + simpleName + " (";
        StringBuilder columnSQL = new StringBuilder();
        int i = 0;
        for (Field field : fields) {
          String column = field.getName() + ",";
          columnSQL.append(column);
          i++;
        }
        String s = columnSQL.deleteCharAt(columnSQL.lastIndexOf(",")).toString();
        sql = sql + columnSQL + ") VALUE (";
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < i; j++) {
          builder.append("?,");
        }
        sql = sql + builder.deleteCharAt(builder.lastIndexOf(",")).toString() + ")";
        System.out.println(sql);
      }
    }
  }
}
