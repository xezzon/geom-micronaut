package io.github.xezzon.geom.core.exception;

import cn.dev33.satoken.exception.NotLoginException;
import io.github.xezzon.tao.web.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xezzon
 */
@RestControllerAdvice
public class GeomAdminExceptionHandler {

  /**
   * 未认证异常
   */
  @ExceptionHandler(NotLoginException.class)
  @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
  public Result<Void> handleNotLoginException(NotLoginException e) {
    UnauthorizedException ue = new UnauthorizedException(e);
    return Result.fail(ue.getCode(), ue.getMessage());
  }
}
