package com.shawn.geektime.homework.user.db.repository;

import com.shawn.geektime.homework.user.db.exception.JdbcTemplateException;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

public class Example<T> {

  private final Class<? extends T> entityClass;

  private final String tableName;

  public Example(Class<? extends T> entityClass) {
    checkEntity();
    this.entityClass = entityClass;
    this.tableName = entityTable();
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

}
