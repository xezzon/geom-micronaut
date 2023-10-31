package io.github.xezzon.geom.auth.adaptor;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.domain.query.RegisterQuery;
import io.github.xezzon.geom.auth.service.AuthService;
import io.github.xezzon.geom.auth.service.UserService;
import io.github.xezzon.tao.logger.LogRecord;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

/**
 * @author xezzon
 */
@Controller
public class AuthController {

  protected final transient UserService userService;
  protected final transient AuthService authService;

  @Inject
  public AuthController(
      UserService userService,
      AuthService authService
  ) {
    this.userService = userService;
    this.authService = authService;
  }

  @Post("/register")
  public User register(@Body RegisterQuery user) {
    return userService.register(user.to());
  }

  /**
   * 用户登录
   * @param user 用户名 密码
   */
  @Post("/login")
  @LogRecord
  public SaTokenInfo login(@Body User user) {
    authService.login(user.getUsername(), user.getPlaintext());
    return StpUtil.getTokenInfo();
  }

  /**
   * 查询当前登录用户
   * @return 当前用户账号信息
   */
  @Get("/me")
  @SaCheckLogin
  public User getCurrentUser() {
    return userService.getById(StpUtil.getLoginIdAsString());
  }

  /**
   * 退出登录
   */
  @Post("/logout")
  public void logout() {
    StpUtil.logout();
  }
}
