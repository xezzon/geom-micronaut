package io.github.xezzon.geom.core.exception;

import io.github.xezzon.tao.exception.ServerException;
import java.io.Serial;

/**
 * @author xezzon
 */
public class InvalidPublicKeyException extends ServerException {

  @Serial
  private static final long serialVersionUID = 1519536950316029113L;

  public InvalidPublicKeyException() {
    super(ErrorCode.INVALID_PUBLIC_KEY.code(), ErrorCode.INVALID_PUBLIC_KEY.message());
  }
}
