package com.shawn.geektime.homework.user.db.repository;

import java.util.List;

public interface CurdRepository<T, ID> {

  /**
   * find result by primary key
   *
   * @param id primary key
   * @return T
   */
  T findById(ID id);

  /**
   * select by Example, return one
   *
   * @param example sql parameter
   * @return T
   */
  T findOneByExample(Example example);

  /**
   * select all result set
   *
   * @return List<T>
   */
  List<T> findAll();

  /**
   * select all result sets that meet the Example
   *
   * @param example
   * @return List<T>
   */
  List<T> findAllByExample(Example example);

  /**
   * Paging all result sets
   *
   * @param pageRequest
   * @return Page<T>
   */
  Page<T> findAll(PageRequest pageRequest);

  /**
   * Paging all result sets that meet the Example
   *
   * @param example
   * @param pageRequest
   * @return Page<T>
   */
  Page<T> findAllByExample(Example example, PageRequest pageRequest);

  /**
   * insert a record
   *
   * @param t record
   * @return Number of successful inserts
   */
  int insert(T t);

  /**
   * insert all records
   *
   * @param entities
   * @return Number of successful inserts
   */
  int insertAll(List<T> entities);

  /**
   * insert a record, then return primary key
   *
   * @param t
   * @return Number of successful inserts
   */
  ID insertAndReturnId(T t);

  /**
   * update a record, if id is null and other properties are null, can not update
   *
   * @param t
   * @return Number of successful update
   */
  int update(T t);

  /**
   * delete a record
   *
   * @param t
   * @return Number of successful delete
   */
  int delete(T t);

  /**
   * delete a record by primary key
   *
   * @param id
   * @return Number of successful delete
   */
  int deleteById(ID id);
}
