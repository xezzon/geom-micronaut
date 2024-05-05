package io.github.xezzon.geom.dict;

import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.geom.dict.domain.QDict;
import io.github.xezzon.geom.dict.repository.DictRepository;
import io.github.xezzon.tao.jpa.BaseJpaWrapper;
import io.micronaut.data.annotation.Repository;
import jakarta.transaction.Transactional;

/**
 * @author xezzon
 */
@Repository
public class DictDAO extends BaseJpaWrapper<Dict, QDict, DictRepository> {

  protected DictDAO(DictRepository dao) {
    super(dao);
  }

  @Override
  protected QDict getQuery() {
    return QDict.dict;
  }

  @Override
  protected Class<Dict> getBeanClass() {
    return Dict.class;
  }

  @Transactional()
  public void updateTag(String oldValue, String newValue) {
    this.getQueryFactory()
        .update(this.getQuery())
        .set(this.getQuery().tag, newValue)
        .where(this.getQuery().tag.eq(oldValue))
        .execute();
  }
}
