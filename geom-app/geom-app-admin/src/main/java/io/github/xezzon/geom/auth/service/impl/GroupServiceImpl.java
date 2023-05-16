package io.github.xezzon.geom.auth.service.impl;

import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMember;
import io.github.xezzon.geom.auth.repository.wrapper.GroupDAO;
import io.github.xezzon.geom.auth.repository.wrapper.GroupMemberDAO;
import io.github.xezzon.geom.auth.service.GroupService;
import io.github.xezzon.tao.exception.ClientException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author xezzon
 */
@Service
public class GroupServiceImpl implements GroupService {

  private final transient GroupDAO groupDAO;
  private final transient GroupMemberDAO groupMemberDAO;

  public GroupServiceImpl(
      GroupDAO groupDAO,
      GroupMemberDAO groupMemberDAO
  ) {
    this.groupDAO = groupDAO;
    this.groupMemberDAO = groupMemberDAO;
  }

  @Override
  public void addGroup(Group group) {
    /* 前置处理 */
    group.setId(null);
    // 检查重复项
    if (groupDAO.get().existsByCodeAndOwnerId(group.getCode(), group.getOwnerId())) {
      throw new ClientException("用户组" + group.getCode() + "已存在");
    }

    groupDAO.get().save(group);
    /* 后置处理 */
    this.joinGroup(group.getId(), Collections.singleton(group.getOwnerId()));
  }

  @Override
  public void joinGroup(String groupId, Collection<String> usersId) {
    List<GroupMember> members = usersId.parallelStream()
        .map(userId -> {
          GroupMember member = new GroupMember();
          member.setGroupId(groupId);
          member.setUserId(userId);
          return member;
        })
        .toList();
    groupMemberDAO.insertIfNotExisted(members);
  }
}
