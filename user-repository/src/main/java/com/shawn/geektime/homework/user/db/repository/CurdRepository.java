package com.shawn.geektime.homework.user.db.repository;

import java.util.List;

public interface CurdRepository<T, K> {

  int insert(T t);

  int update(T t);

  int delete(T t);

  List<T> findAll();

  List<T> findAll(T t);

  T findById(K id);

  int count();

  int deleteById(K id);
}
