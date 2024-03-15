package io.github.xezzon.geom.role.domain;

import io.github.xezzon.tao.trait.From;
import io.github.xezzon.tao.trait.Into;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 新增角色的请求
 */
@Data
public class AddRoleQuery implements Into<Role> {

  String code;
  /**
   * 角色描述
   */
  String name;
  /**
   * 可继承的
   */
  Boolean inheritable;
  /**
   * 上级角色主键
   */
  String parentId;

  @Override
  public Role into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<AddRoleQuery, Role> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Override
    Role from(AddRoleQuery addRoleQuery);
  }
}
