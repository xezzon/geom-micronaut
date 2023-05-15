package io.github.xezzon.geom.auth.service;

import io.github.xezzon.geom.auth.domain.Group;

/**
 * @author xezzon
 */
public interface GroupService {

  /**
   * 新增用户组
   * @param group 用户组信息
   */
  void addGroup(Group group);
}
