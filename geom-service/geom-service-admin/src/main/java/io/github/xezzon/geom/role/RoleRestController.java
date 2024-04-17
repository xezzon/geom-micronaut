package io.github.xezzon.geom.role;

import io.github.xezzon.geom.domain.Id;
import io.github.xezzon.geom.role.domain.AddRoleQuery;
import io.github.xezzon.geom.role.domain.ModifyRoleQuery;
import io.github.xezzon.geom.role.domain.Role;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

@Controller("/role")
public class RoleRestController {

  private final RoleService roleService;

  public RoleRestController(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * 新增角色
   * @param query 角色
   * @return 角色ID
   */
  @Post()
  public Id addRole(@Body AddRoleQuery query) {
    Role role = query.into();
    roleService.addRole(role);
    return Id.of(role.getId());
  }

  /**
   * 修改角色
   * @param query 角色
   */
  @Put()
  public void modifyRole(@Body ModifyRoleQuery query) {
    roleService.modifyRole(query.into());
  }

  /**
   * 删除角色以及所有子级角色
   * @param id 角色ID
   */
  @Delete("{id}")
  public void removeRole(@PathVariable String id) {
    roleService.removeRole(id);
  }
}
