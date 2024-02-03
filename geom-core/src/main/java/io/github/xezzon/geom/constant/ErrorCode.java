package io.github.xezzon.geom.constant;

/**
 * 错误码分配
 * @author xezzon
 * @deprecated 这是一个临时方案
 */
public enum ErrorCode {

  REPEAT_DATA("A0002"),
  NONEXISTENT_DATA("A0003"),
  ;

  private final String code;

  ErrorCode(String code) {
    this.code = code;
  }

  public String code() {
    return this.code;
  }
}
