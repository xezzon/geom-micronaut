package io.github.xezzon.geom.menu.service;

import io.github.xezzon.geom.menu.domain.Menu;
import java.util.List;

/**
 * @author xezzon
 */
public interface MenuService {

  /**
   * 获取菜单树
   * @param parentId 上级菜单ID
   * @return 菜单（树形结构）
   */
  List<Menu> menuTree(String parentId);

  /**
   * 新增菜单
   * @param menu 菜单
   */
  void addMenu(Menu menu);

  /**
   * 修改菜单
   * @param menu 菜单
   */
  void modifyMenu(Menu menu);

  /**
   * 删除菜单主键
   * @param id 菜单主键
   */
  void removeMenu(String id);
}
