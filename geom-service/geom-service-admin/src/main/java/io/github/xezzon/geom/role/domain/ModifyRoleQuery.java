package io.github.xezzon.geom.role.domain;

import io.github.xezzon.tao.trait.From;
import io.github.xezzon.tao.trait.Into;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Data
public class ModifyRoleQuery implements Into<Role> {

  /**
   * 角色主键
   */
  String id;
  /**
   * 角色标识
   */
  String code;
  /**
   * 角色描述
   */
  String name;
  /**
   * 上级角色主键
   */
  String parentId;

  @Override
  public Role into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<ModifyRoleQuery, Role> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Mapping(target = "inheritable", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Override
    Role from(ModifyRoleQuery modifyRoleQuery);
  }
}
