package io.github.xezzon.geom.domain;

import io.github.xezzon.tao.retrieval.CommonQuery;
import io.github.xezzon.tao.trait.From;
import io.github.xezzon.tao.trait.Into;
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
 * 查询条件
 * @author xezzon
 */
@Getter
@Setter
@ToString
@Introspected
public class CommonQueryBean implements Into<CommonQuery> {

  /**
   * 查询的列
   */
  @QueryValue
  protected Set<String> select = null;
  /**
   * 排序
   */
  @QueryValue
  protected List<String> sort = Collections.emptyList();
  /**
   * 筛选条件
   */
  @QueryValue
  @Nullable
  protected String filter = null;
  /**
   * 搜索条件
   */
  @QueryValue
  @Nullable
  protected String searchKey = null;
  /**
   * 页码 从1开始
   */
  @QueryValue
  protected int pageNum = 0;
  /**
   * 每页大小
   */
  @QueryValue
  protected int pageSize = 0;

  @Override
  public CommonQuery into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<CommonQueryBean, CommonQuery> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Override
    CommonQuery from(CommonQueryBean source);
  }
}
