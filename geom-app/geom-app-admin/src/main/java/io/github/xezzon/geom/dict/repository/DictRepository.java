package io.github.xezzon.geom.dict.repository;

import io.github.xezzon.geom.dict.domain.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author xezzon
 */
public interface DictRepository
    extends JpaRepository<Dict, String>,
    QuerydslPredicateExecutor<Dict> {

}
