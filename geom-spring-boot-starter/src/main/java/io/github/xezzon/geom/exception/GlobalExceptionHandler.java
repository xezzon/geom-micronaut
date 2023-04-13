package io.github.xezzon.geom.exception;

import io.github.xezzon.tao.exception.BaseExceptionHandler;
import io.github.xezzon.tao.exception.ServerException;
import io.github.xezzon.tao.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xezzon
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  public GlobalExceptionHandler() {
    super("服务器开小差了，请稍后重试");
  }

  /**
   * 参数校验失败
   */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public Result<Void> handleBindException(BindException e) {
    log.warn("参数校验异常", e);
    IllegalParameterException ipe = new IllegalParameterException(e);
    return Result.fail(ipe.getCode(), ipe.getMessage());
  }

  /**
   * 拦截未知异常
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public Result<Void> handleRuntimeException(RuntimeException e) {
    log.error("预期外异常", e);
    ServerException se = new ServerException(e.getMessage(), e);
    return Result.fail(se.getCode(), errorMessage);
  }
}
