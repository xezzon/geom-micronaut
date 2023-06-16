package io.github.xezzon.geom.auth.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.repository.GroupRepository;
import io.github.xezzon.tao.exception.ClientException;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GroupServiceTest {

  @Resource
  private transient GroupService service;
  @Resource
  private transient GroupRepository repository;

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

    Optional<Group> existGroup = repository.findById(group.getId());
    Assertions.assertTrue(existGroup.isPresent());
    Assertions.assertNotEquals(flakeId, existGroup.get().getId());

    Assertions.assertThrows(ClientException.class, () -> {
      Group group1 = new Group();
      group1.setCode(code);
      group1.setOwnerId(ownerId);
      service.addGroup(group1);
    });
  }

  @Test
  void joinGroup() {
    Group group = new Group();
    group.setCode(RandomUtil.randomString(6));
    group.setOwnerId(IdUtil.getSnowflakeNextIdStr());
    service.addGroup(group);
    List<String> usersId = IntStream.range(1, 100)
        .mapToObj(i -> IdUtil.getSnowflakeNextIdStr())
        .toList();
    service.joinGroup(group.getId(), usersId);
  }

  @Test
  void generateSecretKey() {
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

    String privateKey = service.generateSecretKey(group.getId());
    Optional<Group> optionalGroup = repository.findById(group.getId());
    String publicKey = optionalGroup.get().getPublicKey();

    final RSA rsa = new RSA(Base64.decode(privateKey), Base64.decode(publicKey));
    String message = RandomUtil.randomString(64);
    byte[] encrypt = rsa.encrypt(message, KeyType.PrivateKey);
    byte[] decrypt = rsa.decrypt(encrypt, KeyType.PublicKey);
    Assertions.assertEquals(message, new String(decrypt));
  }
}