package io.github.xezzon.geom.exception;

import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 数据不存在
 */
public class NonexistentDataException extends ClientException {

  @Serial
  private static final long serialVersionUID = -849183459643954636L;

  public NonexistentDataException() {
    super(ErrorCode.NONEXISTENT_DATA.code(), ErrorCode.NONEXISTENT_DATA.message());
  }

  public NonexistentDataException(String message) {
    super(ErrorCode.NONEXISTENT_DATA.code(), message);
  }
}
