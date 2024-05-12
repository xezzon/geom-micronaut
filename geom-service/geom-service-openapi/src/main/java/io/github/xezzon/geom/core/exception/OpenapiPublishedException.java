package io.github.xezzon.geom.core.exception;

import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 接口已发布
 */
public class OpenapiPublishedException extends ClientException {

  @Serial
  private static final long serialVersionUID = -2297388189462227486L;

  public OpenapiPublishedException(String message) {
    super(ErrorCode.OPENAPI_PUBLISHED.code(), message);
  }
}
