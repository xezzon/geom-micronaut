package io.github.xezzon.geom.auth.service.impl;

import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMember;
import io.github.xezzon.geom.auth.repository.wrapper.GroupDAO;
import io.github.xezzon.geom.auth.repository.wrapper.GroupMemberDAO;
import io.github.xezzon.geom.auth.service.GroupService;
import io.github.xezzon.tao.exception.ClientException;
import io.github.xezzon.tao.exception.ServerException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.crypto.KeyGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
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

  @Override
  public String generateSecretKey(String groupId) {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("SM4", new BouncyCastleProvider());
      keyGenerator.init(128);
      String secretKey = Hex.toHexString(keyGenerator.generateKey().getEncoded());
      Group group = new Group();
      group.setId(groupId);
      group.setPublicKey(secretKey);
      boolean updated = groupDAO.update(group);
      if (!updated) {
        throw new ServerException("用户组不存在");
      }
      return secretKey;
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }
}
