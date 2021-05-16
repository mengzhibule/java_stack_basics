package com.shawn.geektime.homework.user.db;

import org.junit.Assert;
import org.junit.Test;

public class Testing {

  @Test
  public void test_array_class(){
    int[] arr = new int[5];
    Assert.assertTrue(arr.getClass().isArray());
  }

}
