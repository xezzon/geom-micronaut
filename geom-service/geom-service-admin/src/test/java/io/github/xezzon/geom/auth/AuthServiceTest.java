package io.github.xezzon.geom.auth;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import io.github.xezzon.geom.crypto.service.SymmetricCryptoService;
import io.github.xezzon.geom.exception.ExpiredTimestampException;
import io.github.xezzon.geom.exception.UnmatchedChecksumException;
import io.github.xezzon.geom.group.domain.Group;
import io.github.xezzon.geom.group.repository.GroupRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * @author xezzon
 */
@MicronautTest(transactional = false)
@TestInstance(Lifecycle.PER_CLASS)
class AuthServiceTest {

  private static final Group GROUP = new Group();

  static {
    GROUP.setCode(RandomUtil.randomString(6));
    GROUP.setName(RandomUtil.randomString(6));
    GROUP.setOwnerId(RandomUtil.randomString(6));
  }

  @Inject
  private AuthService authService;
  @Inject
  private SymmetricCryptoService symmetricCryptoService;
  @Inject
  private GroupRepository groupRepository;

  @BeforeAll
  void setUp() {
    String secretKey = symmetricCryptoService.generateSymmetricSecretKey();
    GROUP.setSecretKey(secretKey);
    groupRepository.save(GROUP);
  }

  @Test
  void decryptMessage() {
    byte[] secretKey = Base64.getDecoder().decode(GROUP.getSecretKey());
    SymmetricCrypto crypto = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey);
    final byte[] origin = RandomUtil.randomBytes(RandomUtil.randomInt(Short.MAX_VALUE));
    byte[] encrypted = crypto.encrypt(origin);
    final Instant instant = Instant.now();
    String checksum = DigestUtil.hmac(
        HmacAlgorithm.HmacSHA256,
        String.valueOf(instant.getEpochSecond()).getBytes(StandardCharsets.UTF_8)
    ).digestHex(origin);
    byte[] decrypted = authService.decryptMessage(
        encrypted, GROUP.getAccessKey(), checksum, instant
    );
    Assertions.assertArrayEquals(origin, decrypted);
  }

  @Test
  void decryptMessage_expired() {
    byte[] secretKey = Base64.getDecoder().decode(GROUP.getSecretKey());
    SymmetricCrypto crypto = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey);
    final byte[] origin = RandomUtil.randomBytes(RandomUtil.randomInt(Short.MAX_VALUE));
    byte[] encrypted = crypto.encrypt(origin);
    final Instant instant = Instant.now().minus(61, ChronoUnit.SECONDS);
    String checksum = DigestUtil.hmac(
        HmacAlgorithm.HmacSHA256,
        String.valueOf(instant.getEpochSecond()).getBytes(StandardCharsets.UTF_8)
    ).digestHex(origin);
    Assertions.assertThrows(ExpiredTimestampException.class, () -> authService.decryptMessage(
        encrypted, GROUP.getAccessKey(), checksum, instant
    ));
  }

  @Test
  void decryptMessage_checksum() {
    byte[] secretKey = Base64.getDecoder().decode(GROUP.getSecretKey());
    SymmetricCrypto crypto = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey);
    final byte[] origin = RandomUtil.randomBytes(RandomUtil.randomInt(Short.MAX_VALUE));
    byte[] encrypted = crypto.encrypt(origin);
    final Instant instant = Instant.now();
    String checksum = DigestUtil.hmac(
        HmacAlgorithm.HmacSHA256,
        String.valueOf(instant.getEpochSecond()).getBytes(StandardCharsets.UTF_8)
    ).digestHex(origin);
    Assertions.assertThrows(UnmatchedChecksumException.class, () -> authService.decryptMessage(
        encrypted, GROUP.getAccessKey(), checksum + RandomUtil.randomString(1), instant
    ));
  }
}
