package io.github.xezzon.geom.auth.adaptor;

import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.domain.convert.UserConvert;
import io.github.xezzon.geom.auth.domain.query.RegisterQuery;
import io.github.xezzon.geom.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xezzon
 */
@RestController
@RequestMapping
public class AuthController {

  private final transient UserService userService;

  @Autowired
  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public User register(@RequestBody RegisterQuery user) {
    return userService.register(UserConvert.INSTANCE.from(user));
  }
}
