package io.github.xezzon.geom.openapi;

import io.github.xezzon.geom.openapi.repository.OpenapiRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@MicronautTest
@TestInstance(Lifecycle.PER_CLASS)
class OpenapiServiceTest {

  @Inject
  protected transient OpenapiService service;
  @Inject
  protected transient OpenapiRepository repository;
}
