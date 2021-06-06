package com.shawn.geektime.homework.user.db.repository;

/**
 * common delete sql statement
 *
 * @param <T> type of entity
 * @param <ID> type of primary key
 * @author Shawn
 * @since 1.0
 */
public interface DeleteRepository<T, ID> {

  /**
   * delete a record by primary key
   *
   * @param id primary key
   * @return return the number of successful executions
   * @since 1.0
   */
  int deleteById(ID id);

  /**
   * delete data that meets the criteria
   *
   * @param t criteria
   * @return return the number of successful executions
   * @since 1.0
   */
  int delete(Example<T> t);
}
