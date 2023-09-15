package io.github.xezzon.geom.auth.repository;

import io.github.xezzon.geom.auth.domain.Group;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

/**
 * @author xezzon
 */
@Repository
public interface GroupRepository
    extends JpaRepository<Group, String> {

  List<Group> findByIdIn(Collection<String> ids);

  boolean existsByCodeAndOwnerId(String code, String ownerId);
}
