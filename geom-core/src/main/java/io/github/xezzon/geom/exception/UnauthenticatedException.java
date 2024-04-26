package io.github.xezzon.geom.exception;

import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 尚未登录
 * @author xezzon
 */
public class UnauthenticatedException extends ClientException {

  @Serial
  private static final long serialVersionUID = 1587877116361597984L;

  public UnauthenticatedException() {
    super(ErrorCode.UNAUTHENTICATED.code(), ErrorCode.UNAUTHENTICATED.message());
  }

  public UnauthenticatedException(Throwable cause) {
    super(ErrorCode.UNAUTHENTICATED.code(), ErrorCode.UNAUTHENTICATED.message(), cause);
  }
}
