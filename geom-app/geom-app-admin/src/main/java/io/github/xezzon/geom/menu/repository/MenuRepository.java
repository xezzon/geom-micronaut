package io.github.xezzon.geom.menu.repository;

import io.github.xezzon.geom.menu.domain.Menu;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

/**
 * @author xezzon
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {

}
