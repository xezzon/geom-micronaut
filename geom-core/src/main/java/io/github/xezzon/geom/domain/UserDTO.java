package io.github.xezzon.geom.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xezzon
 */
@Getter
@Setter
@ToString
public class UserDTO {

  /**
   * 用户主键
   */
  protected String id;
  /**
   * 用户名
   */
  protected String username;
  /**
   * 昵称
   */
  protected String nickname;
}
