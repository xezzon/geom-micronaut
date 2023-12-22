package io.github.xezzon.geom.openapi;

import io.github.xezzon.geom.openapi.repository.OpenapiRepository;
import io.github.xezzon.tao.jpa.BaseJpaWrapper;
import io.micronaut.data.annotation.Repository;

/**
 * @author xezzon
 */
@Repository
public class OpenapiDAO extends BaseJpaWrapper<Openapi, QOpenapi, OpenapiRepository> {

  protected OpenapiDAO(OpenapiRepository repository) {
    super(repository);
  }

  @Override
  protected QOpenapi getQuery() {
    return QOpenapi.openapi;
  }

  @Override
  protected Class<Openapi> getBeanClass() {
    return Openapi.class;
  }
}
