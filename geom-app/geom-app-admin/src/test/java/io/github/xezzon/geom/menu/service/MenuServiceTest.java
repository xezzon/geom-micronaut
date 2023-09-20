package io.github.xezzon.geom.menu.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * @author xezzon
 */
@MicronautTest
@TestInstance(Lifecycle.PER_CLASS)
class MenuServiceTest {

  @Inject
  protected transient MenuService service;
}