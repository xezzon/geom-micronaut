package io.github.xezzon.geom.openapi.repository;

import io.github.xezzon.geom.openapi.Openapi;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @author xezzon
 */
@Repository
public interface OpenapiRepository
    extends JpaRepository<Openapi, String> {

  Optional<Openapi> findByCode(String code);
}
