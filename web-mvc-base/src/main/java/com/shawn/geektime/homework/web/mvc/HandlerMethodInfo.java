package com.shawn.geektime.homework.web.mvc;

import java.lang.reflect.Method;
import java.util.Set;

/** handler method info */
public class HandlerMethodInfo {

  private String requestPath;

  private Method method;

  private Set<HttpMethod> supportedHttpMethods;

  public HandlerMethodInfo(
      String requestPath, Method method, Set<HttpMethod> supportedHttpMethods) {
    this.requestPath = requestPath;
    this.method = method;
    this.supportedHttpMethods = supportedHttpMethods;
  }

  public String getRequestPath() {
    return requestPath;
  }

  public void setRequestPath(String requestPath) {
    this.requestPath = requestPath;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public Set<HttpMethod> getSupportedHttpMethods() {
    return supportedHttpMethods;
  }

  public void setSupportedHttpMethods(Set<HttpMethod> supportedHttpMethods) {
    this.supportedHttpMethods = supportedHttpMethods;
  }
}
