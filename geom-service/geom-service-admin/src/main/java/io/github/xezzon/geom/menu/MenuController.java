package io.github.xezzon.geom.menu;

import io.github.xezzon.geom.domain.Id;
import io.github.xezzon.geom.menu.domain.Menu;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import java.util.List;

/**
 * @author xezzon
 */
@Controller("/menu")
public class MenuController {

  private final transient MenuService menuService;

  @Inject
  MenuController(MenuService menuService) {
    this.menuService = menuService;
  }

  /**
   * 获取所有的菜单
   * @return 菜单（树形）
   */
  @Get()
  public List<Menu> menuTree() {
    return menuService.menuTree("0");
  }

  @Post()
  public Id addMenu(@Body Menu menu) {
    menuService.addMenu(menu);
    return Id.of(menu.getId());
  }

  @Put()
  public Id modifyMenu(@Body Menu menu) {
    menuService.modifyMenu(menu);
    return Id.of(menu.getId());
  }

  @Delete("/{id}")
  public void removeMenu(@PathVariable String id) {
    menuService.removeMenu(id);
  }
}
