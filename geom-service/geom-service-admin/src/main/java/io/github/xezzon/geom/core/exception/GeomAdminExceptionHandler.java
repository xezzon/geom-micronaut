package io.github.xezzon.geom.core.exception;

import cn.dev33.satoken.exception.NotLoginException;
import io.github.xezzon.tao.web.Result;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Status;

/**
 * @author xezzon
 */
@Controller
public class GeomAdminExceptionHandler {

  /**
   * 未认证异常
   */
  @Error(NotLoginException.class)
  @Status(HttpStatus.UNAUTHORIZED)
  public Result<Void> handleNotLoginException(NotLoginException e) {
    UnauthorizedException ue = new UnauthorizedException(e);
    return Result.fail(ue.getCode(), ue.getMessage());
  }
}
