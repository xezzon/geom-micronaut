package io.github.xezzon.geom.exception;

import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;
import org.springframework.validation.BindException;

/**
 * 用户输入参数不符合校验规则
 * @author xezzon
 */
public class IllegalParameterException extends ClientException {

  @Serial
  private static final long serialVersionUID = 1587877116361597984L;
  private static final String ERROR_CODE = "A0400";

  protected IllegalParameterException(BindException cause) {
    super(ERROR_CODE, cause.getBindingResult().getFieldError().getDefaultMessage(), cause);
  }
}
