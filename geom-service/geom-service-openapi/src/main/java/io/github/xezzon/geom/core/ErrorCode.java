package io.github.xezzon.geom.core;

/**
 * 错误码分配
 * @author xezzon
 * @deprecated 这是一个临时方案
 */
public enum ErrorCode {

  OPENAPI_PUBLISHED("A0101"),
  ;

  private final String code;

  ErrorCode(String code) {
    this.code = code;
  }

  public String code() {
    return this.code;
  }
}
