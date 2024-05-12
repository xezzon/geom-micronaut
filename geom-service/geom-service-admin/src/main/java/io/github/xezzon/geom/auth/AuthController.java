package io.github.xezzon.geom.auth;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import io.github.xezzon.geom.domain.UserDTO;
import io.github.xezzon.tao.logger.LogRecord;
import io.micronaut.http.BasicAuth;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

/**
 * @author xezzon
 */
@Controller
public class AuthController {

  private final AuthService authService;

  @Inject
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * 用户登录
   * @param basicAuth 用户名 密码
   */
  @Post("/login")
  @LogRecord
  public SaTokenInfo login(BasicAuth basicAuth) {
    authService.login(basicAuth.getUsername(), basicAuth.getPassword());
    return StpUtil.getTokenInfo();
  }

  /**
   * 查询当前登录用户
   * @return 当前用户账号信息
   */
  @Get("/me")
  @SaCheckLogin
  public UserDTO getCurrentUser() {
    return authService.getCurrentUser();
  }

  /**
   * 验证登录状态
   * 若未登录则返回401状态
   * 若已登录则返回JWT
   * @return JWT
   */
  @Get("/validate")
  public SaTokenInfo validate() {
    String tokenValue = authService.signJwt();
    SaTokenInfo tokenInfo = new SaTokenInfo();
    tokenInfo.setTokenName(HttpHeaders.AUTHORIZATION);
    tokenInfo.setTokenValue(tokenValue);
    return tokenInfo;
  }

  /**
   * 退出登录
   */
  @Post("/logout")
  public void logout() {
    StpUtil.logout();
  }
}
