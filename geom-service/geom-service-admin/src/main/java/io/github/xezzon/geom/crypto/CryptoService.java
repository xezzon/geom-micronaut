package io.github.xezzon.geom.crypto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.xezzon.geom.config.GeomConfig.GeomJwtConfig;
import io.github.xezzon.geom.crypto.service.DigestCryptoService;
import io.github.xezzon.geom.crypto.service.JwtCryptoService;
import io.github.xezzon.geom.crypto.service.SymmetricCryptoService;
import io.micronaut.context.annotation.Bean;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import javax.crypto.KeyGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 加密、散列相关
 * @author xezzon
 */
@Bean
public class CryptoService implements JwtCryptoService, SymmetricCryptoService,
    DigestCryptoService {

  public static final String SYMMETRIC_ALGORITHM = "AES";
  public static final String DIGEST_ALGORITHM = "SHA256";

  private final GeomJwtConfig geomJwtConfig;
  private final KeyManager keyManager;

  public CryptoService(GeomJwtConfig geomJwtConfig, KeyManager keyManager) {
    this.geomJwtConfig = geomJwtConfig;
    this.keyManager = keyManager;
  }

  @Override
  public String sign(Object subject) {
    return JWT.create()
        .withIssuer(geomJwtConfig.getIssuer())
        .withIssuedAt(new Date())
        .withClaim("sub", BeanUtil.beanToMap(subject))
        .sign(Algorithm.ECDSA256(keyManager.getPrivateKey()));
  }

  @Override
  public String generateSymmetricSecretKey() {
    try {
      KeyGenerator keyGenerator =
          KeyGenerator.getInstance(SYMMETRIC_ALGORITHM, new BouncyCastleProvider());
      keyGenerator.init(128);
      byte[] secretKey = keyGenerator.generateKey().getEncoded();
      return Base64.getEncoder().encodeToString(secretKey);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] symmetricDecrypt(byte[] origin, String secretKeyStr) {
    byte[] secretKey = Base64.getDecoder().decode(secretKeyStr);
    SymmetricCrypto sm4 = new SymmetricCrypto(SYMMETRIC_ALGORITHM, secretKey);
    return sm4.decrypt(origin);
  }

  @Override
  public boolean verifyDigest(byte[] data, String digest) {
    Digester digester = DigestUtil.digester(DIGEST_ALGORITHM);
    String digested = digester.digestHex(data);
    return Objects.equals(digest, digested);
  }

  @Override
  public boolean verifyDigest(byte[] data, String digest, String salt) {
    final HmacAlgorithm algorithm = HmacAlgorithm.valueOf("Hmac" + DIGEST_ALGORITHM);
    HMac hmac = DigestUtil.hmac(algorithm, salt.getBytes(StandardCharsets.UTF_8));
    String digested = hmac.digestHex(data);
    return Objects.equals(digest, digested);
  }
}
