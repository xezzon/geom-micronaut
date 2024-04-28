package io.github.xezzon.geom.group;

import io.github.xezzon.geom.core.exception.InvalidTokenException;
import io.github.xezzon.geom.crypto.service.SymmetricCryptoService;
import io.github.xezzon.geom.exception.NonexistentDataException;
import io.github.xezzon.geom.exception.RepeatDataException;
import io.github.xezzon.geom.group.domain.Group;
import io.github.xezzon.geom.group.domain.GroupMember;
import io.github.xezzon.geom.group.domain.GroupMemberUser;
import io.github.xezzon.geom.group.service.IGroupService4Auth;
import io.github.xezzon.geom.user.domain.User;
import io.github.xezzon.geom.user.service.IUserService4Group;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * @author xezzon
 */
@Singleton
public class GroupService implements IGroupService4Auth {

  private final GroupDAO groupDAO;
  private final GroupMemberDAO groupMemberDAO;
  private final IUserService4Group userService;
  private final SymmetricCryptoService symmetricCryptoService;

  public GroupService(
      GroupDAO groupDAO,
      GroupMemberDAO groupMemberDAO,
      IUserService4Group userService,
      SymmetricCryptoService symmetricCryptoService
  ) {
    this.groupDAO = groupDAO;
    this.groupMemberDAO = groupMemberDAO;
    this.userService = userService;
    this.symmetricCryptoService = symmetricCryptoService;
  }

  /**
   * 根据用户ID获取用户所在的所有分组列表
   * @param userId 用户ID
   * @return 用户所在的所有分组列表，如果用户ID为空或用户不在任何分组中，返回空列表
   */
  protected List<Group> listGroupByUserId(@NotNull String userId) {
    List<GroupMember> members = groupMemberDAO.get().findByUserId(userId);
    if (members.isEmpty()) {
      return Collections.emptyList();
    }
    Set<String> groupsId = members.parallelStream()
        .map(GroupMember::getGroupId)
        .collect(Collectors.toSet());
    return groupDAO.get().findByIdIn(groupsId);
  }

  /**
   * 添加用户组
   * @param group 用户组对象
   * @throws RepeatDataException 如果已存在相同编号和所有者的用户组，则抛出此异常
   */
  protected void addGroup(Group group) {
    // 检查重复项
    if (groupDAO.get().existsByCodeAndOwnerId(group.getCode(), group.getOwnerId())) {
      throw new RepeatDataException("用户组" + group.getCode() + "已存在");
    }

    groupDAO.get().save(group);
    /* 后置处理 */
    this.joinGroup(group.getId(), Collections.singleton(group.getOwnerId()));
  }

  /**
   * 将一组用户加入指定的用户组
   * @param groupId 用户组ID
   * @param usersId 用户ID集合
   */
  protected void joinGroup(String groupId, Collection<String> usersId) {
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

  /**
   * 生成用户组的对称加密密钥
   * @param groupId 用户组ID
   * @return 对称加密密钥
   * @throws NonexistentDataException 如果用户组不存在，则抛出该异常
   */
  protected String generateSecretKey(String groupId) {
    String secretKey = symmetricCryptoService.generateSymmetricSecretKey();
    Group group = new Group();
    group.setId(groupId);
    group.setSecretKey(secretKey);
    groupDAO.update(group);
    return secretKey;
  }

  /**
   * 根据用户组ID、页码和每页大小获取用户组中的成员列表
   * @param groupId 用户组ID
   * @param pageNum 页码，从0开始
   * @param pageSize 每页大小
   * @return 用户组成员列表的分页信息
   */
  protected Page<GroupMemberUser> listGroupMember(String groupId, int pageNum, short pageSize) {
    Page<GroupMember> page = groupMemberDAO.get()
        .findByGroupId(groupId, Pageable.from(pageNum, pageSize));
    List<GroupMemberUser> memberUsers = page.getContent().parallelStream()
        .map(member -> {
          User user = userService.getUserById(member.getUserId());
          if (user == null) {
            return null;
          }
          Group group = new Group();
          group.setId(member.getGroupId());
          return GroupMemberUser.build(group, user);
        })
        .filter(Objects::nonNull)
        .toList();
    return Page.of(memberUsers, page.getPageable(), page.getTotalSize());
  }

  /**
   * 从用户组中移除指定成员
   * @param groupId 用户组ID
   * @param membersId 要移除的成员ID集合
   * @return 成功移除的成员数量
   * @throws NonexistentDataException 如果用户组不存在，则抛出此异常
   */
  protected int removeMember(String groupId, Collection<String> membersId) {
    Group group = groupDAO.get().findById(groupId)
        .orElseThrow(() -> new NonexistentDataException("用户组不存在"));
    return groupMemberDAO.get().deleteByIdInAndGroupIdAndUserIdNot(
        membersId, groupId, group.getOwnerId()
    );
  }

  @Override
  public String getSecretKey(String groupId) {
    return groupDAO.get().findById(groupId)
        .map(Group::getSecretKey)
        .orElseThrow(() -> new InvalidTokenException("获取不到密钥"));
  }
}
