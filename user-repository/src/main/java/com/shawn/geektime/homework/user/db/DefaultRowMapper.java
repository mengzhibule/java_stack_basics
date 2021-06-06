package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.db.util.StatementCreatorUtils;
import com.shawn.geektime.homework.user.entity.User;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultRowMapper<T> implements RowMapper<T> {

  private T t;

  public DefaultRowMapper(T t) {
    this.t = t;
  }

  @Override
  public T mapRow(ResultSet rs, int rowNum) throws SQLException {
    if (t == null) {
      return null;
    }
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(User.class, Object.class);
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        String name = propertyDescriptor.getName();
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        String resultSetMethod = StatementCreatorUtils.resultSetMethod(propertyType);
        Method method = rs.getClass().getMethod(resultSetMethod, String.class);
        Object resultValue = method.invoke(rs, name);
        Method writeMethod = propertyDescriptor.getWriteMethod();
        writeMethod.invoke(t, resultValue);
      }
    } catch (Exception e) {
      throw new RuntimeException("Field property mappings do not match");
    }
    return t;
  }
}
