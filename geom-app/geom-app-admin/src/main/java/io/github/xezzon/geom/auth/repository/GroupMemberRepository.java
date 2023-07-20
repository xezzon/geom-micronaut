package io.github.xezzon.geom.auth.repository;

import io.github.xezzon.geom.auth.domain.GroupMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author xezzon
 */
@Repository
public interface GroupMemberRepository
    extends JpaRepository<GroupMember, String>, QuerydslPredicateExecutor<GroupMember> {

  boolean existsByGroupIdAndUserId(String groupId, String userId);

  Page<GroupMember> findByGroupId(String groupId, Pageable pageable);
}
