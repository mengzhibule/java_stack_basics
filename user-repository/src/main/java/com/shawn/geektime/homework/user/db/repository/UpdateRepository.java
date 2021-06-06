package com.shawn.geektime.homework.user.db.repository;

/**
 * common update sql statement
 *
 * @param <T> type of entity
 * @param <ID> type of primary key
 * @author Shawn
 * @since 1.0
 */
public interface UpdateRepository<T, ID> {

  /**
   * update a record by primary key, if value is null, it will also be updated.
   *
   * @param t entity
   * @param id the primary key of the entity
   * @return return the number of successful executions
   */
  int updateById(Example<T> example, ID id);
}
