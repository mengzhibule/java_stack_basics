package com.shawn.geektime.homework.test;

import java.util.Optional;
import org.apache.commons.lang.StringUtils;

public class BaseTest {

  public static void main(String[] args) {
    String s = "forward:/user/list";
    System.out.println(StringUtils.substring(s, "forward:".length()));

    String json = "test";
    Optional<String> s1 = JsonUtil.toJson(json);
    System.out.println(s1.get());
  }
}
