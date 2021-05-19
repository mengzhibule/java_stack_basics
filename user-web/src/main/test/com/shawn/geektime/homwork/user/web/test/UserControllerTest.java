package com.shawn.geektime.homwork.user.web.test;

import com.shawn.geektime.homework.user.controller.UserController;
import com.shawn.geektime.homework.user.dto.UserDTO;
import com.shawn.geektime.homework.web.mvc.annotation.RequestBody;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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

  @Test
  public void test_json_serialize() {
    Map<String, String> map = new HashMap<>();
    map.put("id", "1");
    map.put("name", "2");
    map.put("age", "3");
    String s = JsonUtil.toJson(map).get();
    UserDTO userDTO = JsonUtil.fromJson(s, UserDTO.class).get();
    Assert.assertEquals("1", String.valueOf(userDTO.getId()));
  }
}
