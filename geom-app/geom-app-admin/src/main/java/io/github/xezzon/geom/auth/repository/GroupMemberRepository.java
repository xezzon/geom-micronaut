package io.github.xezzon.geom.auth.repository;

import io.github.xezzon.geom.auth.domain.GroupMember;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xezzon
 */
@Repository
public interface GroupMemberRepository
    extends JpaRepository<GroupMember, String>, QuerydslPredicateExecutor<GroupMember> {

  boolean existsByGroupIdAndUserId(String groupId, String userId);

  Page<GroupMember> findByGroupId(String groupId, Pageable pageable);

  @Transactional
  int deleteByIdInAndGroupIdAndUserIdNot(Collection<String> ids, String groupId, String userId);
}
