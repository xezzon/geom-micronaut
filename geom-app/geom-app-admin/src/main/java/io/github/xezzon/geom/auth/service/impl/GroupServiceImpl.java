package io.github.xezzon.geom.auth.service.impl;

import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
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
import java.io.StringReader;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author xezzon
 */
@Service
public class GroupServiceImpl implements GroupService {

  private final transient GroupDAO groupDAO;
  private final transient GroupMemberDAO groupMemberDAO;
  private final transient UserService userService;

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
    GroupMember probe = new GroupMember();
    probe.setUserId(userId);
    List<GroupMember> members = groupMemberDAO.get().findAll(Example.of(probe));
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
  public byte[] refreshSecretKey(String groupId, String publicKey) {
    /* TODO: 校验用户是否可以刷新密钥 */
    /* 生成密钥并保存 */
    String secretKey = this.generateSecretKey(groupId);
    /* 将密钥进行非对称加密 */
    RSA rsa = new RSA(
        null, PemUtil.readPemObject(new StringReader(publicKey)).getContent()
    );
    return rsa.encrypt(secretKey, KeyType.PublicKey);
  }

  @Override
  public Page<GroupMemberUser> listGroupMember(String groupId, int pageNum, short pageSize) {
    Page<GroupMember> page = groupMemberDAO.get()
        .findByGroupId(groupId, Pageable.ofSize(pageSize).withPage(pageNum));
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
    return new PageImpl<>(memberUsers, page.getPageable(), page.getTotalElements());
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
