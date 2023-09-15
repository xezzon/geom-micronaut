package io.github.xezzon.geom.auth.repository;

import io.github.xezzon.geom.auth.domain.GroupMember;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * @author xezzon
 */
@Repository
public interface GroupMemberRepository
    extends JpaRepository<GroupMember, String> {

  boolean existsByGroupIdAndUserId(String groupId, String userId);

  Page<GroupMember> findByGroupId(String groupId, Pageable pageable);

  List<GroupMember> findByUserId(String userId);

  @Transactional
  int deleteByIdInAndGroupIdAndUserIdNot(Collection<String> ids, String groupId, String userId);
}
