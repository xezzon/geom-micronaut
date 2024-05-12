package io.github.xezzon.geom.user;

import io.github.xezzon.geom.user.domain.RegisterQuery;
import io.github.xezzon.geom.user.domain.User;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

/**
 * @author xezzon
 */
@Controller("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 注册新用户
   * @param user 用户注册信息
   * @return 注册成功后的用户信息
   */
  @Post()
  public User register(@Body RegisterQuery user) {
    return userService.addUser(user.into());
  }
}
