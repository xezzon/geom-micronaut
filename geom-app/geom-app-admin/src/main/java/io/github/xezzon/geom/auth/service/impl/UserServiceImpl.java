package io.github.xezzon.geom.auth.service.impl;

import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.repository.wrapper.UserDAO;
import io.github.xezzon.geom.auth.service.UserService;
import io.github.xezzon.tao.exception.ClientException;
import io.micronaut.context.annotation.Bean;
import org.jetbrains.annotations.NotNull;

/**
 * @author xezzon
 */
@Bean
public class UserServiceImpl implements UserService {

  protected final transient UserDAO userDAO;

  public UserServiceImpl(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @Override
  public User register(@NotNull User user) {
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

  @Override
  public User getById(String id) {
    if (id == null) {
      return null;
    }
    return userDAO.get().findById(id).orElse(null);
  }

  @Override
  public User getByUsername(String username) {
    if (username == null) {
      return null;
    }
    return userDAO.get().findByUsername(username).orElse(null);
  }
}
