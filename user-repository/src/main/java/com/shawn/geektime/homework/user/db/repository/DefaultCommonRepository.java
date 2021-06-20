package com.shawn.geektime.homework.user.db.repository;

import com.shawn.geektime.homework.user.db.JdbcTemplate;
import com.shawn.geektime.homework.user.db.entity.EntityTable;
import com.shawn.geektime.homework.user.db.entity.SqlStatementParamCreator;
import com.shawn.geektime.homework.user.db.exception.JdbcTemplateException;
import com.shawn.geektime.homework.user.db.util.EntityUtils;
import com.shawn.geektime.homework.user.db.util.SQLBuilder;

public abstract class DefaultCommonRepository<T, ID> implements CommonRepository<T, ID> {

  private final JdbcTemplate jdbcTemplate;

  private final Class<?> entityClass;

  private final EntityTable entityTable;

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
    SqlStatementParamCreator sqlStatementParamCreator =
        SQLBuilder.builder().buildInsertSQL(entityTable, t);
    return jdbcTemplate.update(
        sqlStatementParamCreator.getSql(), sqlStatementParamCreator.getParams());
  }

  @Override
  public int delete(Example<T> t) {
    return 0;
  }
}
