package io.github.xezzon.geom.menu.repository.wrapper;

import io.github.xezzon.geom.menu.domain.Menu;
import io.github.xezzon.geom.menu.domain.QMenu;
import io.github.xezzon.geom.menu.repository.MenuRepository;
import io.github.xezzon.tao.jpa.BaseJpaWrapper;
import io.micronaut.data.annotation.Repository;

/**
 * @author xezzon
 */
@Repository
public class MenuDAO extends BaseJpaWrapper<Menu, QMenu, MenuRepository> {

  protected MenuDAO(MenuRepository repository) {
    super(repository);
  }

  @Override
  protected QMenu getQuery() {
    return QMenu.menu;
  }

  @Override
  protected Class<Menu> getBeanClass() {
    return Menu.class;
  }
}
