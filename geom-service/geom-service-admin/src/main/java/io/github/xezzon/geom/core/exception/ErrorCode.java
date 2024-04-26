package io.github.xezzon.geom.core.exception;

/**
 * 错误码分配
 * @author xezzon
 */
public enum ErrorCode {

  INVALID_TOKEN("A0101", "口令不正确"),
  ROLE_CANNOT_INHERIT("A0102", "角色不允许继承"),
  INVALID_PUBLIC_KEY("A0104", "无效的公钥")
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
