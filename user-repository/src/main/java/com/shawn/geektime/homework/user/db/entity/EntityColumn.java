package com.shawn.geektime.homework.user.db.entity;

import java.lang.reflect.Method;
import java.sql.JDBCType;
import javax.persistence.GenerationType;

public class EntityColumn {

  private boolean isPk;
  private String column;
  private Method readMethod;
  private Method writeMethod;
  private String property;
  private Class<?> javaType;
  private JDBCType jdbcType;
  private GenerationType generationType;

  public boolean isPk() {
    return isPk;
  }

  public void setPk(boolean pk) {
    isPk = pk;
  }

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public Method getReadMethod() {
    return readMethod;
  }

  public void setReadMethod(Method readMethod) {
    this.readMethod = readMethod;
  }

  public Method getWriteMethod() {
    return writeMethod;
  }

  public void setWriteMethod(Method writeMethod) {
    this.writeMethod = writeMethod;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public Class<?> getJavaType() {
    return javaType;
  }

  public void setJavaType(Class<?> javaType) {
    this.javaType = javaType;
  }

  public JDBCType getJdbcType() {
    return jdbcType;
  }

  public void setJdbcType(JDBCType jdbcType) {
    this.jdbcType = jdbcType;
  }

  public GenerationType getGenerationType() {
    return generationType;
  }

  public void setGenerationType(GenerationType generationType) {
    this.generationType = generationType;
  }
}
