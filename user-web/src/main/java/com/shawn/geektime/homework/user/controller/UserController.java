package com.shawn.geektime.homework.user.controller;

import com.shawn.geektime.homework.user.dto.UserDTO;
import com.shawn.geektime.homework.user.vo.UserVO;
import com.shawn.geektime.homework.web.mvc.annotation.Controller;
import com.shawn.geektime.homework.web.mvc.annotation.RequestBody;
import com.shawn.geektime.homework.web.mvc.annotation.RequestMapping;
import com.shawn.geektime.homework.web.mvc.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

  @RequestMapping("/test/forward")
  public String testForward() {
    return "forward:/user/say/hello";
  }

  @RequestMapping("/test/vo")
  @ResponseBody
  public UserVO testResponseBody() {
    return new UserVO(1, "shawn", 26);
  }

  @RequestMapping("/test/request/body")
  @ResponseBody
  public UserVO testRequestBody(@RequestBody UserDTO dto) {
    return new UserVO(dto.getId(), dto.getName(), dto.getAge());
  }
}
