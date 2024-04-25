package io.github.xezzon.geom.openapi.exception;

import io.github.xezzon.geom.core.ErrorCode;
import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 接口未发布
 * @author xezzon
 */
public class OpenapiUnpublishedException extends ClientException {

  @Serial
  private static final long serialVersionUID = 6467140230402622372L;
  private static final String ERROR_CODE = ErrorCode.OPENAPI_UNPUBLISHED.code();
  private static final String ERROR_MESSAGE = "接口未发布";

  public OpenapiUnpublishedException() {
    super(ERROR_CODE, ERROR_MESSAGE);
  }
}
