package io.github.xezzon.geom.auth;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import io.github.xezzon.geom.user.UserService;
import io.github.xezzon.geom.user.domain.User;
import io.github.xezzon.geom.user.service.IUserService4Auth;
import io.github.xezzon.tao.exception.ClientException;
import io.micronaut.context.annotation.Bean;

/**
 * @author xezzon
 */
@Bean
public class AuthService {

  private final transient IUserService4Auth userService;

  public AuthService(UserService userService) {
    this.userService = userService;
  }

  protected void login(String username, String cipher) {
    if (StpUtil.isLogin()) {
      return;
    }
    User user = userService.getUserByUsername(username);
    if (user == null) {
      throw new ClientException("用户名或密码错误");
    }
    if (!BCrypt.checkpw(cipher, user.getCipher())) {
      throw new ClientException("用户名或密码错误");
    }
    /* 执行主流程 */
    StpUtil.login(user.getId());
  }

  public User getCurrentUser() {
    return userService.getUserById(StpUtil.getLoginIdAsString());
  }
}
