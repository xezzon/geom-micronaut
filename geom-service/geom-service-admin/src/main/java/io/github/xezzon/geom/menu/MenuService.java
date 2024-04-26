package io.github.xezzon.geom.menu;

import io.github.xezzon.geom.exception.RepeatDataException;
import io.github.xezzon.geom.menu.domain.Menu;
import io.github.xezzon.tao.tree.Tree;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xezzon
 */
@Singleton
public class MenuService {

  private final MenuDAO menuDAO;

  MenuService(MenuDAO menuDAO) {
    this.menuDAO = menuDAO;
  }

  /**
   * 根据父级菜单ID获取菜单树
   * @param parentId 父级菜单ID
   * @return 包含所有子菜单的菜单列表
   */
  protected List<Menu> menuTree(String parentId) {
    List<Menu> menus = Tree.topDown(
        Collections.singleton(parentId), -1, menuDAO.get()::findByParentIdInOrderByOrdinalAsc
    );
    menus = Tree.fold(menus);
    List<Menu> children = menus;
    while (!children.isEmpty()) {
      children = children.stream()
          .filter(menu -> Objects.nonNull(menu.getChildren()))
          .peek(menu -> menu
              .getChildren()
              .forEach(child -> child.setParent(menu))
          )
          .map(Menu::getChildren)
          .flatMap(Collection::stream)
          .toList();
    }
    return menus;
  }

  /**
   * 向系统中添加一个菜单项
   * @param menu 待添加的菜单项
   * @throws RepeatDataException 如果已存在相同路径的同级菜单，则抛出此异常
   */
  @Transactional(rollbackFor = Exception.class)
  protected void addMenu(Menu menu) {
    checkRepeat(menu);
    menuDAO.get().save(menu);
  }

  /**
   * 修改菜单信息
   * @param menu 要修改的菜单对象
   * @throws RepeatDataException 如果已存在相同路径的同级菜单，则抛出此异常
   */
  protected void modifyMenu(Menu menu) {
    checkRepeat(menu);
    menuDAO.get().update(menu);
  }

  /**
   * 从菜单树中删除指定ID的菜单及其所有子菜单
   * @param id 待删除菜单的ID
   */
  protected void removeMenu(String id) {
    List<Menu> menus = Tree.topDown(
        Collections.singleton(id), -1, menuDAO.get()::findByParentIdInOrderByOrdinalAsc
    );
    Set<String> menuIdSet = menus.parallelStream()
        .map(Menu::getId)
        .collect(Collectors.toSet());
    menuIdSet.add(id);
    menuDAO.get().deleteByIdIn(menuIdSet);
  }

  /**
   * 检查菜单是否重复
   * @param menu 待检查的菜单对象
   * @throws RepeatDataException 如果已存在相同路径的同级菜单，则抛出此异常
   */
  private void checkRepeat(Menu menu) {
    Optional<Menu> optionalMenu = menuDAO.get()
        .findByParentIdAndPath(menu.getParentId(), menu.getPath());
    if (optionalMenu.isPresent()) {
      if (!Objects.equals(menu.getId(), optionalMenu.get().getId())) {
        throw new RepeatDataException("已存在相同路径的同级菜单");
      }
    }
  }
}
