package io.github.xezzon.geom.role.repository;

import io.github.xezzon.geom.role.domain.Role;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
