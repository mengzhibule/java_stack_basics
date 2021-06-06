package com.shawn.geektime.homework.user.db.repository;

import com.shawn.geektime.homework.user.db.JdbcTemplate;
import com.shawn.geektime.homework.user.db.entity.EntityTable;
import com.shawn.geektime.homework.user.db.exception.JdbcTemplateException;
import com.shawn.geektime.homework.user.db.util.EntityUtils;
import com.shawn.geektime.homework.user.db.util.SQLBuilder;

public abstract class DefaultCommonRepository<T, ID> implements CommonRepository<T, ID> {

  private JdbcTemplate jdbcTemplate;

  private Class<?> entityClass;

  private EntityTable entityTable;

  public DefaultCommonRepository(JdbcTemplate jdbcTemplate, Class<?> entityClass) {
    this.jdbcTemplate = jdbcTemplate;
    this.entityClass = entityClass;
    boolean isEntity = EntityUtils.checkIsEntity(entityClass);
    if (!isEntity) {
      throw new JdbcTemplateException(
          String.format(
              "This type is not an entity instance type, this type is [%s]",
              entityClass.getName()));
    }
    this.entityTable = EntityUtils.getEntityTable(entityClass);
  }

  @Override
  public int insert(T t) {
    String sql = SQLBuilder.builder().buildInsertSQL(entityTable).getSql();
    return jdbcTemplate.update(sql, null);
  }
}
