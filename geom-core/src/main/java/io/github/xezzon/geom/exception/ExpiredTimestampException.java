package io.github.xezzon.geom.exception;

import io.github.xezzon.tao.exception.ServerException;
import java.io.Serial;

/**
 * 内容已过期
 * @author xezzon
 */
public class ExpiredTimestampException extends ServerException {

  @Serial
  private static final long serialVersionUID = 6810511703943612460L;

  public ExpiredTimestampException() {
    super(ErrorCode.EXPIRED_TIMESTAMP.code(), ErrorCode.EXPIRED_TIMESTAMP.message());
  }
}
