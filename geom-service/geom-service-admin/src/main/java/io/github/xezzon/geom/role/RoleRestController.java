package io.github.xezzon.geom.role;

import io.micronaut.http.annotation.Controller;

@Controller("/role")
public class RoleRestController {

  private final RoleService roleService;

  public RoleRestController(RoleService roleService) {
    this.roleService = roleService;
  }
}
