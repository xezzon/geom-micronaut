package io.github.xezzon.geom.domain;

import io.github.xezzon.tao.trait.From;
import io.github.xezzon.tao.trait.Into;
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
public class UserDTO implements Into<JwtDTO> {

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


  @Override
  public JwtDTO into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<UserDTO, JwtDTO> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "subject", source = "id")
    @Override
    JwtDTO from(UserDTO userDTO);
  }
}
