package io.github.xezzon.geom.auth.repository.wrapper;

import io.github.xezzon.geom.auth.domain.GroupMember;
import io.github.xezzon.geom.auth.domain.QGroupMember;
import io.github.xezzon.geom.auth.repository.GroupMemberRepository;
import io.github.xezzon.tao.jpa.BaseJpaWrapper;
import io.micronaut.data.annotation.Repository;
import java.util.Collection;

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

  public int insertIfNotExisted(Collection<GroupMember> members) {
    int count = 0;
    for (GroupMember member : members) {
      // TODO: 分布式锁
      boolean exist = this.get()
          .existsByGroupIdAndUserId(member.getGroupId(), member.getUserId());
      if (exist) {
        continue;
      }
      this.get().save(member);
      count = count + 1;
    }
    return count;
  }
}
