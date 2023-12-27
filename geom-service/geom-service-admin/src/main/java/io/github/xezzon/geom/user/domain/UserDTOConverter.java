package io.github.xezzon.geom.user.domain;

import io.github.xezzon.geom.trait.IConverter;
import io.github.xezzon.geom.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Mapper
public interface UserDTOConverter extends IConverter<User, UserDTO> {

  UserDTOConverter INSTANCE = Mappers.getMapper(UserDTOConverter.class);

  @Override
  UserDTO convert(User source);
}
