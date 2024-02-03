package io.github.xezzon.geom.openapi.exception;

import io.github.xezzon.geom.core.ErrorCode;
import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 接口已发布
 */
public class OpenapiPublishedException extends ClientException {

  @Serial
  private static final long serialVersionUID = -2297388189462227486L;
  private static final String ERROR_CODE = ErrorCode.OPENAPI_PUBLISHED.code();

  public OpenapiPublishedException(String message) {
    super(ERROR_CODE, message);
  }
}
