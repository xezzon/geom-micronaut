package io.github.xezzon.geom.menu.service.impl;

import io.github.xezzon.geom.menu.repository.wrapper.MenuDAO;
import io.github.xezzon.geom.menu.service.MenuService;
import jakarta.inject.Singleton;

/**
 * @author xezzon
 */
@Singleton
public class MenuServiceImpl implements MenuService {

  protected final MenuDAO menuDAO;

  MenuServiceImpl(MenuDAO menuDAO) {
    this.menuDAO = menuDAO;
  }
}
