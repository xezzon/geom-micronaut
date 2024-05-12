package io.github.xezzon.geom.menu;

import io.github.xezzon.geom.domain.Id;
import io.github.xezzon.geom.menu.domain.AddMenuReq;
import io.github.xezzon.geom.menu.domain.Menu;
import io.github.xezzon.geom.menu.domain.ModifyMenuReq;
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

  private final MenuService menuService;

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

  /**
   * 添加菜单
   * @param menu 要添加的菜单对象
   * @return 返回添加后菜单的ID
   */
  @Post()
  public Id addMenu(@Body AddMenuReq req) {
    Menu menu = req.into();
    menuService.addMenu(menu);
    return Id.of(menu.getId());
  }

  /**
   * 修改菜单
   * @param menu 待修改的菜单对象
   * @return 返回修改后的菜单ID
   */
  @Put()
  public Id modifyMenu(@Body ModifyMenuReq req) {
    Menu menu = req.into();
    menuService.modifyMenu(menu);
    return Id.of(menu.getId());
  }

  /**
   * 删除菜单项
   * @param id 菜单项ID
   */
  @Delete("/{id}")
  public void removeMenu(@PathVariable String id) {
    menuService.removeMenu(id);
  }
}
