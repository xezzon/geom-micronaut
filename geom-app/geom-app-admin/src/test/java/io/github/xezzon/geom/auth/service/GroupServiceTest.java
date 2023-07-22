package io.github.xezzon.geom.auth.service;

import static io.github.xezzon.geom.TestData.GROUPS;
import static io.github.xezzon.geom.TestData.GROUP_MEMBERS;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMember;
import io.github.xezzon.geom.auth.domain.GroupMemberUser;
import io.github.xezzon.geom.auth.repository.GroupMemberRepository;
import io.github.xezzon.geom.auth.repository.GroupRepository;
import io.github.xezzon.tao.exception.ClientException;
import jakarta.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
class GroupServiceTest {

  @Resource
  private transient GroupService service;
  @Resource
  private transient GroupRepository groupRepository;
  @Resource
  private transient GroupMemberRepository memberRepository;

  @Test
  void addGroup() {
    Group group = new Group();
    String flakeId = IdUtil.getSnowflakeNextIdStr();
    String code = RandomUtil.randomString(6);
    String name = RandomUtil.randomString(6);
    String ownerId = IdUtil.getSnowflakeNextIdStr();
    group.setId(flakeId);
    group.setCode(code);
    group.setName(name);
    group.setOwnerId(ownerId);
    service.addGroup(group);

    Optional<Group> entity = groupRepository.findById(group.getId());
    Assertions.assertTrue(entity.isPresent());
  }

  @Test
  void addGroup_repeat() {
    final Group group = RandomUtil.randomEle(GROUPS);

    Assertions.assertThrows(ClientException.class, () -> {
      Group group1 = new Group();
      group1.setCode(group.getCode());
      group1.setName(RandomUtil.randomString(8));
      group1.setOwnerId(group.getOwnerId());
      service.addGroup(group1);
    });
  }

  @RepeatedTest(2)
  void joinGroup() {
    final Group group = RandomUtil.randomEle(GROUPS);
    List<String> usersId = IntStream.range(1, 100)
        .mapToObj(i -> IdUtil.getSnowflakeNextIdStr())
        .toList();
    service.joinGroup(group.getId(), usersId);
    // 校验是否新增成功
    String groupId = group.getId();
    String userId = RandomUtil.randomEle(usersId);
    boolean existed = memberRepository.existsByGroupIdAndUserId(groupId, userId);
    Assertions.assertTrue(existed);
  }

  @Test
  void generateSecretKey() throws GeneralSecurityException {
    final Group group = RandomUtil.randomEle(GROUPS);

    String message = RandomUtil.randomString(64);
    // 加密与解密
    String secretKeyString = service.generateSecretKey(group.getId());
    SecretKeySpec secretKeySpec = new SecretKeySpec(
        Base64.getDecoder().decode(secretKeyString), "SM4"
    );
    Cipher encrypt = Cipher.getInstance("SM4/ECB/NoPadding", new BouncyCastleProvider());
    encrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    byte[] encrypted = encrypt.doFinal(message.getBytes());
    Cipher decrypt = Cipher.getInstance("SM4/ECB/NoPadding", new BouncyCastleProvider());
    decrypt.init(Cipher.DECRYPT_MODE, secretKeySpec);
    String decrypted = new String(decrypt.doFinal(encrypted));
    Assertions.assertEquals(message, decrypted);
  }

  @Test
  void refreshSecretKey() {
    final Group group = RandomUtil.randomEle(GROUPS);
    RSA rsa = new RSA();
    byte[] decryptedSecretKey = service.refreshSecretKey(
        group.getId(), PemUtil.toPem(KeyType.PublicKey.name(), rsa.getPublicKey().getEncoded())
    );
    String secretKey = new String(rsa.decrypt(decryptedSecretKey, KeyType.PrivateKey));
    Group entity = groupRepository.findById(group.getId()).get();
    Assertions.assertEquals(entity.getSecretKey(), secretKey);
  }

  @Test
  void listGroupByUserId() {
    final String userId = RandomUtil.randomEle(GROUP_MEMBERS).getUserId();
    List<Group> groups = service.listGroupByUserId(userId);
    String[] groupsId = groups.parallelStream()
        .map(Group::getId)
        .sorted()
        .toArray(String[]::new);
    String[] exceptIds = GROUP_MEMBERS.parallelStream()
        .filter(o -> Objects.equals(o.getUserId(), userId))
        .map(GroupMember::getGroupId)
        .sorted()
        .toArray(String[]::new);
    Assertions.assertArrayEquals(exceptIds, groupsId);
  }

  @Test
  void listGroupMember() {
    final Group group = RandomUtil.randomEle(GROUPS);
    Page<GroupMemberUser> groupMemberUsers = service.listGroupMember(
        group.getId(), 0, Short.MAX_VALUE
    );
    String[] usersId = groupMemberUsers.stream().parallel()
        .map(GroupMemberUser::getUserId)
        .sorted()
        .toArray(String[]::new);
    String[] exceptsId = GROUP_MEMBERS.parallelStream()
        .filter(o -> Objects.equals(o.getGroupId(), group.getId()))
        .map(GroupMember::getUserId)
        .sorted()
        .toArray(String[]::new);
    Assertions.assertArrayEquals(exceptsId, usersId);
  }

  @Test
  void removeMember() {
    // 获取某一用户组部分人员、另一用户组随机一位用户
    Group group = RandomUtil.randomEle(GROUPS);
    List<GroupMember> members = new ArrayList<>();
    while (members.size() <= 3) {
      group = RandomUtil.randomEle(GROUPS);
      final String groupId = group.getId();
      members = GROUP_MEMBERS.stream()
          .filter(o -> Objects.equals(o.getGroupId(), groupId))
          .collect(Collectors.toList());
    }
    for (int i = members.size() - 2; i >= 0; i--) {
      GroupMember member = members.get(i);
      if (Objects.equals(member.getUserId(), group.getOwnerId())) {
        continue;
      }
      if (RandomUtil.randomBoolean()) {
        continue;
      }
      members.remove(i);
    }
    members.add(members.get(0));
    String ownerId = group.getOwnerId();
    GroupMember testMember = members.stream()
        .filter(o -> !Objects.equals(o.getUserId(), ownerId))
        .findAny().get();
    String groupId = group.getId();
    GroupMember otherMember = GROUP_MEMBERS.stream()
        .filter(o -> !Objects.equals(o.getGroupId(), groupId))
        .findAny().get();
    members.add(otherMember);
    List<String> membersId = members.parallelStream()
        .map(GroupMember::getId)
        .toList();

    int removed = service.removeMember(group.getId(), membersId);

    Assertions.assertEquals(members.size() - 3, removed);
    // 普通成员被移除
    boolean existed = memberRepository.findById(testMember.getId()).isPresent();
    Assertions.assertFalse(existed);
    // 所属人不被移除
    boolean ownerExisted = memberRepository.existsByGroupIdAndUserId(groupId, group.getOwnerId());
    Assertions.assertTrue(ownerExisted);
    // 其他组的人员不被移除
    boolean otherExisted = memberRepository.findById(otherMember.getId()).isPresent();
    Assertions.assertTrue(otherExisted);
  }
}
