package io.github.xezzon.geom.exception;

import io.github.xezzon.tao.exception.BaseExceptionHandler;
import io.github.xezzon.tao.exception.ServerException;
import io.github.xezzon.tao.web.Result;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局异常处理器
 * @author xezzon
 */
@Controller
public class GlobalExceptionHandler extends BaseExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  public GlobalExceptionHandler() {
    super("服务器开小差了，请稍后重试");
  }

  /**
   * 拦截未知异常
   */
  @Error(value = Exception.class, global = true)
  @Status(HttpStatus.INTERNAL_SERVER_ERROR)
  public Result<Void> handleRuntimeException(RuntimeException e) {
    log.error("预期外异常", e);
    ServerException se = new ServerException(e.getMessage(), e);
    return Result.fail(se, errorMessage);
  }
}
