package io.github.xezzon.geom.auth.service.impl;

import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMember;
import io.github.xezzon.geom.auth.domain.GroupMemberUser;
import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.repository.wrapper.GroupDAO;
import io.github.xezzon.geom.auth.repository.wrapper.GroupMemberDAO;
import io.github.xezzon.geom.auth.service.GroupService;
import io.github.xezzon.geom.auth.service.UserService;
import io.github.xezzon.tao.exception.ClientException;
import io.github.xezzon.tao.exception.ServerException;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.KeyGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author xezzon
 */
@Singleton
public class GroupServiceImpl implements GroupService {

  protected final transient GroupDAO groupDAO;
  protected final transient GroupMemberDAO groupMemberDAO;
  protected final transient UserService userService;

  public GroupServiceImpl(
      GroupDAO groupDAO,
      GroupMemberDAO groupMemberDAO,
      UserService userService
  ) {
    this.groupDAO = groupDAO;
    this.groupMemberDAO = groupMemberDAO;
    this.userService = userService;
  }

  @Override
  public List<Group> listGroupByUserId(String userId) {
    if (userId == null) {
      return Collections.emptyList();
    }
    List<GroupMember> members = groupMemberDAO.get().findByUserId(userId);
    if (members.isEmpty()) {
      return Collections.emptyList();
    }
    Set<String> groupsId = members.parallelStream()
        .map(GroupMember::getGroupId)
        .collect(Collectors.toSet());
    return groupDAO.get().findByIdIn(groupsId);
  }

  @Override
  public void addGroup(Group group) {
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

  @Override
  public String generateSecretKey(String groupId) {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("SM4", new BouncyCastleProvider());
      keyGenerator.init(128);
      String secretKey = Base64.getEncoder().encodeToString(
          keyGenerator.generateKey().getEncoded()
      );
      Group group = new Group();
      group.setId(groupId);
      group.setSecretKey(secretKey);
      boolean updated = groupDAO.update(group);
      if (!updated) {
        throw new ServerException("用户组不存在");
      }
      return secretKey;
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Page<GroupMemberUser> listGroupMember(String groupId, int pageNum, short pageSize) {
    Page<GroupMember> page = groupMemberDAO.get()
        .findByGroupId(groupId, Pageable.from(pageNum, pageSize));
    List<GroupMemberUser> memberUsers = page.getContent().parallelStream()
        .map(member -> {
          User user = userService.getById(member.getUserId());
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

  @Override
  public int removeMember(String groupId, Collection<String> membersId) {
    Group group = groupDAO.get().findById(groupId)
        .orElseThrow(() -> new ClientException("用户组不存在"));
    return groupMemberDAO.get().deleteByIdInAndGroupIdAndUserIdNot(
        membersId, groupId, group.getOwnerId()
    );
  }
}
