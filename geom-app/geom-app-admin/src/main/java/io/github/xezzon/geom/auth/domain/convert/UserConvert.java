package io.github.xezzon.geom.auth.domain.convert;

import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.domain.query.RegisterQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Mapper
public interface UserConvert {

  UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

  /**
   * 转换注册参数
   * @param registerQuery 注册参数
   * @return 用户模型
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "cipher", ignore = true)
  @Mapping(target = "plaintext", source = "cipher")
  @Mapping(target = "createTime", ignore = true)
  @Mapping(target = "updateTime", ignore = true)
  User from(RegisterQuery registerQuery);
}
