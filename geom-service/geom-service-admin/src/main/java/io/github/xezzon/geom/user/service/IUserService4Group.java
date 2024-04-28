package io.github.xezzon.geom.user.service;

import io.github.xezzon.geom.user.domain.User;
import org.jetbrains.annotations.NotNull;

/**
 * @author xezzon
 */
public interface IUserService4Group {

  /**
   * 获取用户信息
   * @param userId 用户主键
   * @return 用户信息
   */
  User getUserById(@NotNull String userId);
}
