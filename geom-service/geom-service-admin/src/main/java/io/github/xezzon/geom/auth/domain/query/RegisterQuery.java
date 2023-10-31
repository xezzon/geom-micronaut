package io.github.xezzon.geom.auth.domain.query;

import static io.github.xezzon.geom.constant.PatternConstants.PASSWORD_PATTERN;

import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.trait.IConverter;
import io.github.xezzon.geom.trait.IQuery;
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
public class RegisterQuery implements IQuery<User> {

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

  public User to() {
    return RegisterQueryConverter.INSTANCE.convert(this);
  }
}

@Mapper
interface RegisterQueryConverter extends IConverter<RegisterQuery, User> {

  RegisterQueryConverter INSTANCE = Mappers.getMapper(RegisterQueryConverter.class);

  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "cipher", ignore = true)
  @Mapping(target = "plaintext", source = "cipher")
  @Mapping(target = "createTime", ignore = true)
  @Mapping(target = "updateTime", ignore = true)
  User convert(RegisterQuery source);
}
