package io.github.xezzon.geom.role;

import jakarta.inject.Singleton;

@Singleton
public class RoleService {

  private final RoleDAO roleDAO;

  public RoleService(RoleDAO roleDAO) {
    this.roleDAO = roleDAO;
  }
}
