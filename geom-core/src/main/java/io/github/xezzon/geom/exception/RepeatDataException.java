package io.github.xezzon.geom.exception;

import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 数据重复
 */
public class RepeatDataException extends ClientException {

  @Serial
  private static final long serialVersionUID = 6769766767159212550L;

  public RepeatDataException(String message) {
    super(ErrorCode.REPEAT_DATA.code(), message);
  }
}
