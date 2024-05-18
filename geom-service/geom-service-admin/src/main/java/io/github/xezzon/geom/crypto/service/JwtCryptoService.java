package io.github.xezzon.geom.crypto.service;

import io.github.xezzon.geom.domain.JwtDTO;
import org.jetbrains.annotations.NotNull;

/**
 * JWT相关
 * @author xezzon
 */
public interface JwtCryptoService {

  /**
   * 签发JWT
   * @param dto JWT bean对象
   * @return JWT字符串
   */
  String sign(@NotNull JwtDTO dto);
}
