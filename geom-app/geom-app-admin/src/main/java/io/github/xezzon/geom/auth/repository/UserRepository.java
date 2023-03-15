package io.github.xezzon.geom.auth.repository;

import io.github.xezzon.geom.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author xezzon
 */
public interface UserRepository
    extends JpaRepository<User, String>, QuerydslPredicateExecutor<User> {

}
