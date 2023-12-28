package io.github.xezzon.geom.auth.event;

import cn.hutool.crypto.KeyUtil;
import io.github.xezzon.tao.observer.Observation;
import java.security.PublicKey;

/**
 * 公钥生成事件
 * @param publicKey 公钥
 */
public record PublicKeyGeneratedEvent(
    PublicKey publicKey
) implements Observation {

  public String getPublicKey() {
    return KeyUtil.toBase64(publicKey);
  }
}
