package io.github.xezzon.geom.user;

import io.github.xezzon.geom.exception.RepeatDataException;
import io.github.xezzon.geom.user.domain.User;
import io.github.xezzon.geom.user.service.IUserService4Auth;
import io.github.xezzon.geom.user.service.IUserService4Group;
import io.micronaut.context.annotation.Bean;
import org.jetbrains.annotations.NotNull;

/**
 * @author xezzon
 */
@Bean
public class UserService implements IUserService4Auth, IUserService4Group {

  private final UserDAO userDAO;

  UserService(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  /**
   * 添加用户
   * @param user 待添加的用户
   * @return 返回添加后的用户信息，包含用户ID和昵称
   * @throws RepeatDataException 如果用户名已存在，则抛出此异常
   */
  protected User addUser(@NotNull User user) {
    String username = user.getUsername();
    boolean exists = userDAO.get().existsByUsername(username);
    if (exists) {
      throw new RepeatDataException("用户名" + username + "已注册");
    }

    if (user.getNickname() == null) {
      user.setNickname(username);
    }
    user.setId(null);

    userDAO.get().save(user);

    return new User()
        .setId(user.getId())
        .setNickname(user.getNickname());
  }

  /**
   * 根据用户ID获取用户信息
   * @param id 用户ID
   * @return 用户信息，如果ID为空则返回null，如果ID不存在则返回Optional.empty()
   */
  protected User getById(String id) {
    if (id == null) {
      return null;
    }
    return userDAO.get().findById(id).orElse(null);
  }

  /**
   * 根据用户名获取用户信息
   * @param username 用户名
   * @return 返回与用户名对应的用户信息，若不存在则返回null
   */
  protected User getByUsername(String username) {
    if (username == null) {
      return null;
    }
    return userDAO.get().findByUsername(username).orElse(null);
  }

  @Override
  public User getUserByUsername(String username) {
    return this.getByUsername(username);
  }

  @Override
  public User getUserById(String userId) {
    return this.getById(userId);
  }
}
