package io.github.xezzon.geom.openapi.model;

import io.github.xezzon.geom.openapi.Openapi;
import io.github.xezzon.tao.trait.From;
import io.github.xezzon.tao.trait.Into;
import java.time.Instant;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Data
public class PublishOpenapiQuery implements Into<Openapi> {

  /**
   * 待发布的对外接口ID
   */
  private String id;
  /**
   * 发布时间
   */
  private Instant publishTime;

  @Override
  public Openapi into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<PublishOpenapiQuery, Openapi> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Mapping(target = "path", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleteTime", ignore = true)
    @Override
    Openapi from(PublishOpenapiQuery publishOpenapiQuery);
  }
}
