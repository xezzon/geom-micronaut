package io.github.xezzon.geom.user;

import io.github.xezzon.geom.user.domain.QUser;
import io.github.xezzon.geom.user.domain.User;
import io.github.xezzon.geom.user.repository.UserRepository;
import io.github.xezzon.tao.jpa.BaseJpaWrapper;
import io.micronaut.data.annotation.Repository;

/**
 * @author xezzon
 */
@Repository
public class UserDAO extends BaseJpaWrapper<User, QUser, UserRepository> {

  protected UserDAO(UserRepository dao) {
    super(dao);
  }

  @Override
  protected QUser getQuery() {
    return QUser.user;
  }

  @Override
  protected Class<User> getBeanClass() {
    return User.class;
  }
}
