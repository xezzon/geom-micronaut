package io.github.xezzon.geom.crypto;

import cn.hutool.crypto.KeyUtil;
import io.github.xezzon.geom.auth.event.PublicKeyGeneratedEvent;
import io.github.xezzon.geom.config.GeomConfig.GeomJwtConfig;
import io.github.xezzon.geom.crypto.service.KeyLoader;
import io.github.xezzon.tao.observer.ObserverContext;
import io.micronaut.context.annotation.Context;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

/**
 * TODO 优化扩展性 以便兼容其他加密算法
 */
@Singleton
@Context
@Slf4j
public class KeyManager {

  public static final String ALGORITHM = "EC";
  private final GeomJwtConfig geomJwtConfig;
  private PrivateKey privateKey;
  private final KeyLoader keyLoader;

  public KeyManager(GeomJwtConfig geomJwtConfig, KeyLoader keyLoader) {
    this.geomJwtConfig = geomJwtConfig;
    this.keyLoader = keyLoader;
    ObserverContext.register(PublicKeyGeneratedEvent.class, this::printPublicKey);
    ObserverContext.register(PublicKeyGeneratedEvent.class, this::savePublicKeyToClasspath);
  }

  /**
   * 在应用启动后，加载私钥。 如果无法找到私钥文件或解析失败，则生成新的密钥对并保存私钥文件。 加载成功后，将公钥广播出去。
   */
  @PostConstruct
  public void loadPrivateKey() {
    /* 获取私钥文件 */
    String privateKeyFilename = geomJwtConfig.getIssuer() + ".pri";
    PublicKey publicKey = null;
    try {
      byte[] pemContent = keyLoader.read(privateKeyFilename);
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      KeySpec keySpec = new X509EncodedKeySpec(pemContent);
      this.privateKey = keyFactory.generatePrivate(keySpec);
      /* 从私钥中提取公钥 */
      ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
      ECPoint qPoint = ecSpec.getG().multiply(((ECPrivateKey) this.privateKey).getD());
      ECPublicKeySpec pubSpec = new ECPublicKeySpec(qPoint, ecSpec);
      publicKey = KeyUtil.generatePublicKey(ALGORITHM, pubSpec);
    } catch (Exception ignored) {
      log.debug("Cannot load private key.");
    }
    if (this.privateKey == null) {
      /* 获取不到文件或解析不了 则生成一对密钥 */
      KeyPair ecc = KeyUtil.generateKeyPair(ALGORITHM);
      this.privateKey = ecc.getPrivate();
      publicKey = ecc.getPublic();
      /* 保存私钥 */
      keyLoader.write(privateKeyFilename, this.privateKey.getEncoded(), "PRIVATE KEY");
    }
    /* 广播公钥 */
    ObserverContext.post(new PublicKeyGeneratedEvent(publicKey));
  }

  /**
   * 获取私钥
   * @return 返回ECPrivateKey类型的私钥
   */
  public java.security.interfaces.ECPrivateKey getPrivateKey() {
    return (java.security.interfaces.ECPrivateKey) this.privateKey;
  }

  /**
   * 打印公钥到控制台
   * @param event 公钥
   */
  public void printPublicKey(PublicKeyGeneratedEvent event) {
    log.info("Current JWT Public Key is: {}", event.getPublicKey());
  }

  /**
   * 将公钥保存到文件中（PKCS8）
   * @param event 公钥
   */
  public void savePublicKeyToClasspath(PublicKeyGeneratedEvent event) {
    String publicKeyFilename = geomJwtConfig.getIssuer() + ".pub";
    keyLoader.write(publicKeyFilename, event.publicKey().getEncoded(), "PUBLIC KEY");
  }
}
