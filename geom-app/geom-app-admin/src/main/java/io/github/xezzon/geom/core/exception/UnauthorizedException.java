package io.github.xezzon.geom.core.exception;

import cn.dev33.satoken.exception.NotLoginException;
import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 尚未登录
 * @author xezzon
 */
public class UnauthorizedException extends ClientException {

  @Serial
  private static final long serialVersionUID = 1587877116361597984L;
  private static final String ERROR_CODE = "A0230";
  private static final String ERROR_MESSAGE = "尚未登录";

  protected UnauthorizedException(NotLoginException cause) {
    super(ERROR_CODE, ERROR_MESSAGE, cause);
  }
}
