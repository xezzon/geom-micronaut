package io.github.xezzon.geom.core.exception;

import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 不正确的口令
 * @author xezzon
 */
public class InvalidTokenException extends ClientException {

  @Serial
  private static final long serialVersionUID = 4676151668260963197L;

  public InvalidTokenException() {
    super(ErrorCode.INVALID_TOKEN.code(), ErrorCode.INVALID_TOKEN.message());
  }

  public InvalidTokenException(String message) {
    super(ErrorCode.INVALID_TOKEN.code(), message);
  }
}
