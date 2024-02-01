package io.github.xezzon.geom.user.domain;

import static io.github.xezzon.geom.constant.PatternConstants.PASSWORD_PATTERN;

import io.github.xezzon.tao.trait.From;
import io.github.xezzon.tao.trait.Into;
import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Getter
@Setter
@ToString
@Introspected
public class RegisterQuery implements Into<User> {

  /**
   * 用户名
   */
  @Pattern(
      regexp = "^[a-z0-9_-]{3,15}$",
      message = "用户名必须是3~15位 小写字母/数字/下划线 组成的字符串"
  )
  private String username;
  /**
   * 密码
   */
  @Pattern(
      regexp = PASSWORD_PATTERN,
      message = "密码由至少8位有效字符构成，且包含 大小写字符/数字/特殊字符(@!#$%^&*) 中至少两类"
  )
  private String cipher;
  /**
   * 用户昵称
   */
  private String nickname;

  @Override
  public User into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<RegisterQuery, User> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cipher", ignore = true)
    @Mapping(target = "plaintext", source = "cipher")
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    User from(RegisterQuery source);
  }
}
