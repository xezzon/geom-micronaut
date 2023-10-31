package io.github.xezzon.geom.auth.service;

import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMemberUser;
import io.micronaut.data.model.Page;
import java.util.Collection;
import java.util.List;

/**
 * @author xezzon
 */
public interface GroupService {

  /**
   * 根据用户ID查询其用户组
   * @param userId 用户ID
   * @return 用户组集合
   */
  List<Group> listGroupByUserId(String userId);

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

  /**
   * 查询用户组成员
   * @param groupId 用户组主键
   * @param pageNum 页码 从0开始
   * @param pageSize 页大小
   * @return 用户组成员
   */
  Page<GroupMemberUser> listGroupMember(String groupId, int pageNum, short pageSize);

  /**
   * 批量移除用户组成员
   * @param groupId 用户组主键
   * @param membersId 用户组成员主键
   * @return 移除数量
   */
  int removeMember(String groupId, Collection<String> membersId);
}
