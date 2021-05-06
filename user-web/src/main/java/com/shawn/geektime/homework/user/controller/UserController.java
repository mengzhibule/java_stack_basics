package com.shawn.geektime.homework.user.controller;

import com.shawn.geektime.homework.web.mvc.annotation.Controller;
import com.shawn.geektime.homework.web.mvc.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

  @RequestMapping("/say/hello")
  public String sayHello(){
    return "hello world";
  }

  @RequestMapping("/test/forward")
  public String testForward(){
    return "forward:/user/say/hello";
  }

}
