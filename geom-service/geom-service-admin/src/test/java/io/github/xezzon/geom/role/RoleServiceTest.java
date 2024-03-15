package io.github.xezzon.geom.role;

import io.github.xezzon.geom.role.repository.RoleRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@MicronautTest
@TestInstance(Lifecycle.PER_CLASS)
class RoleServiceTest {

  @Inject
  transient RoleService service;
  @Inject
  transient RoleRepository repository;
}
