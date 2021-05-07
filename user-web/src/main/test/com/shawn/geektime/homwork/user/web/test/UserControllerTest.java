package com.shawn.geektime.homwork.user.web.test;

import com.shawn.geektime.homework.user.controller.UserController;
import com.shawn.geektime.homework.web.mvc.annotation.RequestBody;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Test;

public class UserControllerTest {

  @Test
  public void test_request_body() {
    Method[] methods = UserController.class.getMethods();
    for (Method method : methods) {
      Annotation[][] parameterAnnotations = method.getParameterAnnotations();
      if (parameterAnnotations.length != 0) {
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
          for (Annotation annotation : parameterAnnotation) {
            if (annotation instanceof RequestBody) {
              Assert.assertEquals("testRequestBody", method.getName());
            }
          }
        }
      }
    }
  }
}
