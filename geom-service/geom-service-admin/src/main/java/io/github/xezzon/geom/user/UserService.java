package io.github.xezzon.geom.user;

import io.github.xezzon.geom.user.domain.User;
import io.github.xezzon.geom.user.service.IUserService4Auth;
import io.github.xezzon.geom.user.service.IUserService4Group;
import io.github.xezzon.tao.exception.ClientException;
import io.micronaut.context.annotation.Bean;
import org.jetbrains.annotations.NotNull;

/**
 * @author xezzon
 */
@Bean
public class UserService implements IUserService4Auth, IUserService4Group {

  private final transient UserDAO userDAO;

  UserService(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  protected User addUser(@NotNull User user) {
    String username = user.getUsername();
    boolean exists = userDAO.get().existsByUsername(username);
    if (exists) {
      throw new ClientException("用户名" + username + "已注册");
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

  protected User getById(String id) {
    if (id == null) {
      return null;
    }
    return userDAO.get().findById(id).orElse(null);
  }

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
