package io.github.xezzon.geom.crypto;

import static org.junit.jupiter.api.Assertions.*;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xezzon
 */
@MicronautTest
class CryptoServiceTest {

  @Inject
  private CryptoService cryptoService;

  @Test
  void verifyDigest() {
    final byte[] data = RandomUtil.randomBytes(RandomUtil.randomInt(Short.MAX_VALUE));
    String digest = DigestUtil.sha256Hex(data);
    Assertions.assertTrue(cryptoService.verifyDigest(data, digest));
    Assertions.assertFalse(cryptoService.verifyDigest(data, digest + RandomUtil.randomString(1)));
  }
}
