package io.github.xezzon.geom.dict.repository;

import io.github.xezzon.geom.dict.domain.Dict;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xezzon
 */
@Repository
public interface DictRepository
    extends JpaRepository<Dict, String> {

  Optional<Dict> findByTagAndCode(String tag, String code);

  List<Dict> findByTag(String tag);

  List<Dict> findByParentIdIn(Collection<String> parentIds);

  int deleteByIdIn(Collection<String> ids);
}
