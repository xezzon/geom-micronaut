package io.github.xezzon.geom.auth.repository;

import io.github.xezzon.geom.auth.domain.Group;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author xezzon
 */
@Repository
public interface GroupRepository
    extends JpaRepository<Group, String>, QuerydslPredicateExecutor<Group> {

  List<Group> findByIdIn(Collection<String> ids);

  boolean existsByCodeAndOwnerId(String code, String ownerId);
}
