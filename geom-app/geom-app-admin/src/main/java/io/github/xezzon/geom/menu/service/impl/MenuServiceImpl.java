package io.github.xezzon.geom.menu.service.impl;

import io.github.xezzon.geom.menu.domain.Menu;
import io.github.xezzon.geom.menu.repository.wrapper.MenuDAO;
import io.github.xezzon.geom.menu.service.MenuService;
import io.github.xezzon.tao.exception.ClientException;
import io.github.xezzon.tao.tree.Tree;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xezzon
 */
@Singleton
public class MenuServiceImpl implements MenuService {

  protected final MenuDAO menuDAO;

  MenuServiceImpl(MenuDAO menuDAO) {
    this.menuDAO = menuDAO;
  }

  @Override
  public List<Menu> menuTree(String parentId) {
    List<Menu> menus = Tree.topDown(
        Collections.singleton(parentId), -1, menuDAO.get()::findByParentIdInOrderByOrdinalAsc
    );
    menus = Tree.fold(menus);
    List<Menu> children = menus;
    while (!children.isEmpty()) {
      children = children.stream()
          .filter((menu) -> Objects.nonNull(menu.getChildren()))
          .peek((menu) -> menu
              .getChildren()
              .forEach((child) -> child.setParent(menu))
          )
          .map(Menu::getChildren)
          .flatMap(Collection::stream)
          .toList();
    }
    return menus;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void addMenu(Menu menu) {
    Optional<Menu> optionalMenu = menuDAO.get()
        .findByParentIdAndPath(menu.getParentId(), menu.getPath());
    if (optionalMenu.isPresent()) {
      throw new ClientException("已存在相同路径的同级菜单");
    }
    menuDAO.get().save(menu);
  }
}
