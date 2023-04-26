package io.github.xezzon.geom.auth.repository.wrapper;

import io.github.xezzon.geom.auth.domain.GroupMember;
import io.github.xezzon.geom.auth.domain.QGroupMember;
import io.github.xezzon.geom.auth.repository.GroupMemberRepository;
import io.github.xezzon.tao.jpa.BaseJpaWrapper;
import org.springframework.stereotype.Repository;

/**
 * @author xezzon
 */
@Repository
public class GroupMemberDAO extends
    BaseJpaWrapper<GroupMember, QGroupMember, GroupMemberRepository> {

  protected GroupMemberDAO(GroupMemberRepository repository) {
    super(repository);
  }

  @Override
  protected QGroupMember getQuery() {
    return QGroupMember.groupMember;
  }

  @Override
  protected Class<GroupMember> getBeanClass() {
    return GroupMember.class;
  }
}
