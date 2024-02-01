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

  private final transient UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Post()
  public User register(@Body RegisterQuery user) {
    return userService.addUser(user.into());
  }
}
