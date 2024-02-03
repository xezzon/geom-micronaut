package io.github.xezzon.geom.exception;

import io.github.xezzon.geom.constant.ErrorCode;
import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 数据不存在
 */
public class NonexistentDataException extends ClientException {

  @Serial
  private static final long serialVersionUID = -849183459643954636L;
  private static final String ERROR_CODE = ErrorCode.NONEXISTENT_DATA.code();
  private static final String ERROR_MESSAGE = "数据不存在或已删除";

  public NonexistentDataException() {
    super(ERROR_CODE, ERROR_MESSAGE);
  }
}
