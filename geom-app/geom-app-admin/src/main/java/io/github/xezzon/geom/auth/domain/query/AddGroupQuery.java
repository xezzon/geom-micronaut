package io.github.xezzon.geom.auth.domain.query;

import io.github.xezzon.geom.auth.domain.Group;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Data
public class AddGroupQuery {

  private String code;
  private String name;

  public Group to() {
    return AddGroupQueryConverter.INSTANCE.to(this);
  }
}

@Mapper
interface AddGroupQueryConverter {

  AddGroupQueryConverter INSTANCE = Mappers.getMapper(AddGroupQueryConverter.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "updateTime", ignore = true)
  @Mapping(target = "secretKey", ignore = true)
  @Mapping(target = "ownerId", ignore = true)
  @Mapping(target = "deleteTime", ignore = true)
  @Mapping(target = "createTime", ignore = true)
  @Mapping(target = "accessKey", ignore = true)
  Group to(AddGroupQuery query);
}
