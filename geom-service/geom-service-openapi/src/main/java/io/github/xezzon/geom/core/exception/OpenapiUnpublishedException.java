package io.github.xezzon.geom.core.exception;

import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 接口未发布
 * @author xezzon
 */
public class OpenapiUnpublishedException extends ClientException {

  @Serial
  private static final long serialVersionUID = 6467140230402622372L;

  public OpenapiUnpublishedException() {
    super(ErrorCode.OPENAPI_UNPUBLISHED.code(), ErrorCode.OPENAPI_UNPUBLISHED.message());
  }
}
