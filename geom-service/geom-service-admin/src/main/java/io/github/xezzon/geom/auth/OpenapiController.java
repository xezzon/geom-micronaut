package io.github.xezzon.geom.auth;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import java.time.Instant;

/**
 * @author xezzon
 */
@Controller("/openapi")
public class OpenapiController {

  private final AuthService authService;

  public OpenapiController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * 解密消息
   * @param date 时间戳，来自HTTP请求头X-TIMESTAMP
   * @param accessKey 账户标识，来自HTTP请求头X-ACCESS-KEY
   * @param checksum 校验和，来自HTTP请求头X-CHECKSUM
   * @param origin 加密后的消息，来自HTTP请求体
   * @return 解密后的消息
   */
  @Post("/decrypt")
  public byte[] decryptMessage(
      @Header("X-TIMESTAMP") Instant date,
      @Header("X-ACCESS-KEY") String accessKey,
      @Header("X-CHECKSUM") String checksum,
      @Body byte[] origin
  ) {
    return authService.decryptMessage(origin, accessKey, checksum, date);
  }
}
