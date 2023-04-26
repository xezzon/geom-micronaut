package io.github.xezzon.geom.auth.repository;

import io.github.xezzon.geom.auth.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author xezzon
 */
@Repository
public interface GroupMemberRepository
    extends JpaRepository<GroupMember, String>, QuerydslPredicateExecutor<GroupMember> {

}
