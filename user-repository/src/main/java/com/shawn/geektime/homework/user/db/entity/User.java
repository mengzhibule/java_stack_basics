package com.shawn.geektime.homework.user.db.entity;

import com.shawn.geektime.homework.user.db.annotation.PrimaryKey;

public class User {

  @PrimaryKey private int id;

  private String username;

  private String password;

  private int age;

  private String address;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
