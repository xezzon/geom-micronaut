package io.github.xezzon.geom.auth.service;

/**
 * @author xezzon
 */
public interface AuthService {

  /**
   * 用户登录
   * @param username 用户名
   * @param cipher 密码
   */
  void login(String username, String cipher);
}
