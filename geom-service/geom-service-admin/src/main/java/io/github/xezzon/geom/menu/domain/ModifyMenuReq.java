package io.github.xezzon.geom.menu.domain;

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
public class ModifyMenuReq implements Into<Menu> {

  String id;
  /**
   * 菜单路径
   */
  String path;
  /**
   * 菜单的名字
   */
  String name;
  /**
   * 组件路径
   */
  String component;
  /**
   * 图标标识
   * @see <a href="https://iconify.design/">Iconify</a>
   */
  String icon;
  /**
   * 排序
   */
  Integer ordinal;
  /**
   * 是否隐藏
   */
  Boolean hideInMenu;
  /**
   * 上级菜单主键
   */
  String parentId;

  @Override
  public Menu into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<ModifyMenuReq, Menu> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Override
    Menu from(ModifyMenuReq addMenuReq);
  }
}
