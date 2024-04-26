package io.github.xezzon.geom.auth.event;

import io.github.xezzon.tao.observer.Observation;
import java.security.PublicKey;
import java.util.Base64;

/**
 * 公钥生成事件
 * @param publicKey 公钥
 */
public record PublicKeyGeneratedEvent(
    PublicKey publicKey
) implements Observation {

  public String getPublicKey() {
    return Base64.getEncoder().encodeToString(publicKey.getEncoded());
  }
}
