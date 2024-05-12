package io.github.xezzon.geom.exception;

import io.github.xezzon.tao.exception.ServerException;
import java.io.Serial;

/**
 * 校验和校验失败
 * @author xezzon
 */
public class UnmatchedChecksumException extends ServerException {

  @Serial
  private static final long serialVersionUID = -3879992045659024324L;

  public UnmatchedChecksumException() {
    super(ErrorCode.UNMATCHED_CHECKSUM.code(), ErrorCode.UNMATCHED_CHECKSUM.message());
  }
}
