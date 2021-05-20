package com.shawn.geektime.homework.user.db.repository;

import com.shawn.geektime.homework.user.db.entity.User;
import java.util.List;

public class UserRepository implements CurdRepository<User, Integer> {

  @Override
  public int insert(User user) {
    return 0;
  }

  @Override
  public int update(User user) {
    return 0;
  }

  @Override
  public int delete(User user) {
    return 0;
  }

  @Override
  public List<User> findAll() {
    return null;
  }

  @Override
  public List<User> findAll(User user) {
    return null;
  }

  @Override
  public User findById(Integer id) {
    return null;
  }

  @Override
  public int count() {
    return 0;
  }

  @Override
  public int deleteById(Integer id) {
    return 0;
  }
}
