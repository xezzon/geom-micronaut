package io.github.xezzon.geom.domain;

import io.github.xezzon.geom.trait.IConverter;
import io.github.xezzon.geom.trait.IQuery;
import io.github.xezzon.tao.retrieval.CommonQuery;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.QueryValue;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Getter
@Setter
@ToString
@Introspected
public class CommonQueryBean implements IQuery<CommonQuery> {

  @QueryValue
  protected Set<String> select = null;
  @QueryValue
  protected List<String> sort = Collections.emptyList();
  @QueryValue
  @Nullable
  protected String filter = null;
  @QueryValue
  @Nullable
  protected String searchKey = null;
  @QueryValue
  protected int pageNum = 0;
  @QueryValue
  protected int pageSize = 0;

  @Override
  public CommonQuery to() {
    return CommonQueryBeanConverter.INSTANCE.convert(this);
  }
}

@Mapper
interface CommonQueryBeanConverter extends IConverter<CommonQueryBean, CommonQuery> {

  CommonQueryBeanConverter INSTANCE = Mappers.getMapper(CommonQueryBeanConverter.class);

  @Override
  CommonQuery convert(CommonQueryBean source);
}
