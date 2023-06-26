package io.github.xezzon.geom.auth.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.repository.GroupRepository;
import io.github.xezzon.tao.exception.ClientException;
import jakarta.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.bouncycastle.util.encoders.Hex;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
class GroupServiceTest {

  private static final Group GROUP;

  static {
    GROUP = new Group();
    GROUP.setId(IdUtil.getSnowflakeNextIdStr());
    GROUP.setCode(RandomUtil.randomString(6));
    GROUP.setName(RandomUtil.randomString(6));
    GROUP.setOwnerId(IdUtil.getSnowflakeNextIdStr());
  }

  @Resource
  private transient GroupService service;
  @Resource
  private transient GroupRepository repository;

  @BeforeAll
  void beforeAll() {
    repository.save(GROUP);
  }

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
  void generateSecretKey() throws GeneralSecurityException {
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

    String message = RandomUtil.randomString(64);
    // 加密与解密
    String secretKeyString = service.generateSecretKey(group.getId());
    SecretKeySpec secretKeySpec = new SecretKeySpec(Hex.decode(secretKeyString), "SM4");
    Cipher encrypt = Cipher.getInstance("SM4/ECB/NoPadding", new BouncyCastleProvider());
    encrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    byte[] encrypted = encrypt.doFinal(message.getBytes());
    Cipher decrypt = Cipher.getInstance("SM4/ECB/NoPadding", new BouncyCastleProvider());
    decrypt.init(Cipher.DECRYPT_MODE, secretKeySpec);
    String decrypted = new String(decrypt.doFinal(encrypted));
    Assertions.assertEquals(message, decrypted);
  }
}