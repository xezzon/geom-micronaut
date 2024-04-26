package io.github.xezzon.geom.core;

/**
 * 错误码分配
 * @author xezzon
 */
public enum ErrorCode {

  OPENAPI_PUBLISHED("A0101"),
  OPENAPI_UNPUBLISHED("A0004"),
  ;

  private final String code;

  ErrorCode(String code) {
    this.code = code;
  }

  public String code() {
    return this.code;
  }
}
