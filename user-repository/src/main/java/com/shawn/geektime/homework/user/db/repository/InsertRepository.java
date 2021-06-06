package com.shawn.geektime.homework.user.db.repository;

import java.util.Collection;

/**
 * common insert sql statement
 *
 * @param <T> type of entity
 * @param <ID> type of primary key
 * @author Shawn
 * @since 1.0
 */
public interface InsertRepository<T, ID> {

  /**
   * int a record into database, If the value is null, it will also be inserted
   *
   * @param t a record
   * @return return the number of successful executions
   * @since 1.0
   */
  int insert(T t);

  /**
   * int a record into database and return primary key. If the value is null, it will also be
   * inserted. if you want not return primary key, you should use {@link
   * InsertRepository#insert(java.lang.Object)}
   *
   * @param t a record
   * @return return primary key value
   * @since 1.0
   */
  ID insertReturnKey(T t);

  /**
   * insert a batch of record into the database.
   *
   * @param t a batch of record
   * @return return the number of successful executions
   * @since 1.0
   */
  int insertBatch(Collection<T> t);

  /**
   * int a batch of record into database and return primary keys. If the value is null, it will also
   * be inserted. if you want not return primary key。 you should use {@link
   * InsertRepository#insertBatch(java.util.Collection)}
   *
   * @param t a batch of record
   * @return return primary key value
   * @since 1.0
   */
  Collection<ID> insertBatchReturnKeys(Collection<T> t);
}
