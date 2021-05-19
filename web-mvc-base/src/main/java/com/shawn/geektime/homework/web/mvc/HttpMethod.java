package com.shawn.geektime.homework.web.mvc;

import java.util.Arrays;

/**
 * http request支持的类型
 *
 * @author shawn
 */
public enum HttpMethod {
  GET,
  POST,
  DELETE,
  PUT,
  HEAD,
  OPTIONS,
  TRACE,
  ;

  public static HttpMethod toHttpMethod(String method) {
    return Arrays.stream(values())
        .filter(httpMethod -> httpMethod.name().equalsIgnoreCase(method))
        .findFirst()
        .orElse(null);
  }
}
