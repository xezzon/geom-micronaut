package io.github.xezzon.geom.exception;

/**
 * 错误码分配
 * @author xezzon
 */
public enum ErrorCode {

  REPEAT_DATA("A0002", "数据重复"),
  NONEXISTENT_DATA("A0003", "数据不存在或已删除"),
  EXPIRED_TIMESTAMP("A0004", "内容已过期"),
  UNMATCHED_CHECKSUM("A0005", "校验和与内容不匹配失败"),
  UNAUTHENTICATED("A0006", "尚未登录"),
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
