package io.github.xezzon.geom.auth.adaptor;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.domain.query.RegisterQuery;
import io.github.xezzon.geom.auth.service.AuthService;
import io.github.xezzon.geom.auth.service.UserService;
import io.github.xezzon.tao.logger.LogRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
  private final transient AuthService authService;

  @Autowired
  public AuthController(
      UserService userService,
      AuthService authService
  ) {
    this.userService = userService;
    this.authService = authService;
  }

  @PostMapping("/register")
  public User register(@RequestBody RegisterQuery user) {
    return userService.register(user.to());
  }

  /**
   * 用户登录
   * @param user 用户名 密码
   */
  @PostMapping("/login")
  @LogRecord
  public SaTokenInfo login(@RequestBody User user) {
    authService.login(user.getUsername(), user.getPlaintext());
    return StpUtil.getTokenInfo();
  }

  /**
   * 查询当前登录用户
   * @return 当前用户账号信息
   */
  @GetMapping("/me")
  @SaCheckLogin
  public User getCurrentUser() {
    return userService.getById(StpUtil.getLoginId(null));
  }

  /**
   * 退出登录
   */
  @PostMapping("/logout")
  public void logout() {
    StpUtil.logout();
  }
}
