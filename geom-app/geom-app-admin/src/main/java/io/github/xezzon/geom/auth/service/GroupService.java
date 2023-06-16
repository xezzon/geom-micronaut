package io.github.xezzon.geom.auth.service;

import io.github.xezzon.geom.auth.domain.Group;
import java.util.Collection;

/**
 * @author xezzon
 */
public interface GroupService {

  /**
   * 新增用户组
   * @param group 用户组信息
   */
  void addGroup(Group group);

  /**
   * 将用户加入用户组
   * @param groupId 用户组主键
   * @param usersId 用户主键
   */
  void joinGroup(String groupId, Collection<String> usersId);

  /**
   * 生成用户组的密钥对
   * @param groupId 用户组ID
   * @return 私钥（BASE64编码）
   */
  String generateSecretKey(String groupId);
}
