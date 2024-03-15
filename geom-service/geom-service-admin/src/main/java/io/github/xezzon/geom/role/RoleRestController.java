package io.github.xezzon.geom.role;

import io.github.xezzon.geom.domain.Id;
import io.github.xezzon.geom.role.domain.AddRoleQuery;
import io.github.xezzon.geom.role.domain.Role;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

@Controller("/role")
public class RoleRestController {

  private final RoleService roleService;

  public RoleRestController(RoleService roleService) {
    this.roleService = roleService;
  }

  @Post()
  public Id addRole(@Body AddRoleQuery query) {
    Role role = query.into();
    roleService.addRole(role);
    return Id.of(role.getId());
  }
}
