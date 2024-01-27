package io.github.xezzon.geom.user.domain;

import io.github.xezzon.geom.user.UserDTO;
import io.github.xezzon.tao.rs.From;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Mapper
public interface UserDTOConverter extends From<User, UserDTO> {

  UserDTOConverter INSTANCE = Mappers.getMapper(UserDTOConverter.class);

  @Override
  UserDTO from(User source);
}
