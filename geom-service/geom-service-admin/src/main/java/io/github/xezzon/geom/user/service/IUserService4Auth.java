package io.github.xezzon.geom.user.service;

import io.github.xezzon.geom.user.domain.User;
import org.jetbrains.annotations.NotNull;

/**
 * @author xezzon
 */
public interface IUserService4Auth {

  /**
   * 根据用户名获取用户信息
   * @param username 用户名
   * @return 用户信息
   */
  User getUserByUsername(@NotNull String username);
}
