package io.github.xezzon.geom.core.exception;

import io.github.xezzon.tao.exception.ClientException;
import java.io.Serial;

/**
 * 角色不能被继承
 * @author xezzon
 */
public class RoleCannotInheritException extends ClientException {

  @Serial
  private static final long serialVersionUID = 3529123931163100719L;

  public RoleCannotInheritException() {
    super(ErrorCode.ROLE_CANNOT_INHERIT.code(), ErrorCode.ROLE_CANNOT_INHERIT.message());
  }
}
