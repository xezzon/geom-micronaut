package io.github.xezzon.geom.auth.service.impl;

import io.github.xezzon.geom.auth.domain.QUser;
import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.repository.wrapper.UserDAO;
import io.github.xezzon.geom.auth.service.UserService;
import io.github.xezzon.tao.exception.ClientException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xezzon
 */
@Service
public class UserServiceImpl implements UserService {

  private final transient UserDAO userDAO;

  @Autowired
  public UserServiceImpl(
      UserDAO userDAO
  ) {
    this.userDAO = userDAO;
  }

  @Override
  public User register(@NotNull User user) {
    String username = user.getUsername();
    boolean exists = userDAO.get().exists(QUser.user.username.eq(username));
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
  public User getByUsername(String username) {
    if (username == null) {
      return null;
    }
    return userDAO.get().findByUsername(username);
  }
}
