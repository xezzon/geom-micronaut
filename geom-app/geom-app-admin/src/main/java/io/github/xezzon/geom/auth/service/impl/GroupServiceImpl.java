package io.github.xezzon.geom.auth.service.impl;

import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.repository.wrapper.GroupDAO;
import io.github.xezzon.geom.auth.repository.wrapper.GroupMemberDAO;
import io.github.xezzon.geom.auth.service.GroupService;
import io.github.xezzon.tao.exception.ClientException;
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
  }
}
