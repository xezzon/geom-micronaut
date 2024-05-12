package io.github.xezzon.geom.core.exception;

/**
 * 错误码分配
 * @author xezzon
 */
public enum ErrorCode {

  OPENAPI_PUBLISHED("A0201", "接口已发布"),
  OPENAPI_UNPUBLISHED("A0202", "接口未发布"),
  ;

  private final String code;
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String code() {
    return this.code;
  }

  public String message() {
    return this.message;
  }
}
