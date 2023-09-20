package io.github.xezzon.geom.menu.adaptor;

import io.github.xezzon.geom.menu.service.MenuService;
import io.micronaut.http.annotation.Controller;
import jakarta.inject.Inject;

/**
 * @author xezzon
 */
@Controller("/menu")
public class MenuController {

  protected final MenuService menuService;

  @Inject
  MenuController(MenuService menuService) {
    this.menuService = menuService;
  }
}
