package io.github.xezzon.geom.openapi.model;

import io.github.xezzon.geom.openapi.Openapi;
import io.github.xezzon.tao.trait.From;
import io.github.xezzon.tao.trait.Into;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Data
public class ModifyOpenapiQuery implements Into<Openapi> {

  @NotNull
  private String id;
  /**
   * 接口名称
   */
  @NotBlank
  private String name;
  /**
   * 调用路径
   */
  @Pattern(regexp = "^[\\w-]+$")
  private String code;
  /**
   * 转发路径
   */
  @NotBlank
  private String path;

  @Override
  public Openapi into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<ModifyOpenapiQuery, Openapi> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Mapping(target = "publishTime", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleteTime", ignore = true)
    @Override
    Openapi from(ModifyOpenapiQuery modifyOpenapiQuery);
  }
}
