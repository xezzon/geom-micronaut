package io.github.xezzon.geom.auth.service.impl;

import io.github.xezzon.geom.auth.repository.wrapper.GroupDAO;
import io.github.xezzon.geom.auth.repository.wrapper.GroupMemberDAO;
import io.github.xezzon.geom.auth.service.GroupService;
import org.springframework.stereotype.Service;

/**
 * @author xezzon
 */
@Service
public class GroupServiceImpl implements GroupService {

  private final transient GroupDAO groupDAO;
  private final transient GroupMemberDAO groupMemberDAO;

  public GroupServiceImpl(GroupDAO groupDAO, GroupMemberDAO groupMemberDAO) {
    this.groupDAO = groupDAO;
    this.groupMemberDAO = groupMemberDAO;
  }
}
