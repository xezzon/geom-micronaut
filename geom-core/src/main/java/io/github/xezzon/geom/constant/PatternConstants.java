package io.github.xezzon.geom.constant;

/**
 * 正则表达式常量
 * @author xezzon
 */
public class PatternConstants {

  /**
   * 密码正则
   * 密码由至少8位有效字符构成，且包含 大小写字符/数字/特殊字符(@!#$%^&*) 中至少两类
   */
  public static final String PASSWORD_PATTERN
      = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![@!#$%^&*]+$)[0-9a-zA-Z@!#$%^&*]{8,}$";
}
