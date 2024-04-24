package io.github.xezzon.geom.role;

import io.github.xezzon.geom.role.domain.QRole;
import io.github.xezzon.geom.role.domain.Role;
import io.github.xezzon.geom.role.repository.RoleRepository;
import io.github.xezzon.tao.jpa.BaseJpaWrapper;
import io.micronaut.data.annotation.Repository;

@Repository
public class RoleDAO extends BaseJpaWrapper<Role, QRole, RoleRepository> {

  protected RoleDAO(RoleRepository repository) {
    super(repository);
  }

  @Override
  protected QRole getQuery() {
    return QRole.role;
  }

  @Override
  protected Class<Role> getBeanClass() {
    return Role.class;
  }
}
