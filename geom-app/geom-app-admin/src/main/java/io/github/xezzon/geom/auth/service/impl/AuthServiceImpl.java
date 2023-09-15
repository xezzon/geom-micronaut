package io.github.xezzon.geom.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.service.AuthService;
import io.github.xezzon.geom.auth.service.UserService;
import io.github.xezzon.tao.exception.ClientException;
import io.micronaut.context.annotation.Bean;

/**
 * @author xezzon
 */
@Bean
public class AuthServiceImpl implements AuthService {

  protected final transient UserService userService;

  public AuthServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void login(String username, String cipher) {
    if (StpUtil.isLogin()) {
      return;
    }
    User user = userService.getByUsername(username);
    if (user == null) {
      throw new ClientException("用户名或密码错误");
    }
    if (!BCrypt.checkpw(cipher, user.getCipher())) {
      throw new ClientException("用户名或密码错误");
    }
    /* 执行主流程 */
    StpUtil.login(user.getId());
  }
}
