package com.shawn.geektime.homework.user.db.repository;

import java.util.List;
import java.util.Optional;

/**
 * common select sql statement
 *
 * @param <T> type of entity
 * @param <ID> type of primary key
 * @author Shawn
 * @since 1.0
 */
public interface ReadRepository<T, ID> {

  /**
   * Find all records
   *
   * @return all instances of the type.
   */
  List<T> findAll();

  /**
   * Find all the data that matches the criteria
   *
   * @param example criteria
   * @return all instances that matches the criteria
   */
  List<T> findAllByExample(Example<T> example);

  /**
   * Retrieves an entity by its id.
   *
   * @param id primary key, must not be null.
   * @return the entity with the given id or Optional#empty() if none found.
   */
  Optional<T> findById(ID id);

  /**
   * Returns the number of entities available.
   *
   * @return the number of entities.
   */
  long count();
}
