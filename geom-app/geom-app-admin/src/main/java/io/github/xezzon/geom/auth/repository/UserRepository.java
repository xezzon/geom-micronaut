package io.github.xezzon.geom.auth.repository;

import io.github.xezzon.geom.auth.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

/**
 * @author xezzon
 */
@Repository
public interface UserRepository
    extends JpaRepository<User, String> {

  /**
   * 根据用户名查询
   * @param username 用户名
   * @return 查询结果
   */
  User findByUsername(String username);

  /**
   * 查询用户名是否已存在
   * @param username 用户名
   */
  boolean existsByUsername(String username);
}
