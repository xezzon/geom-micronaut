package io.github.xezzon.geom.dict.domain;

import io.github.xezzon.tao.trait.From;
import io.github.xezzon.tao.trait.Into;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Getter
@Setter
@ToString
public class ModifyDictReq implements Into<Dict> {

  String id;
  /**
   * 字典值
   */
  @NotNull(message = "字典编码不能为空")
  @Pattern(regexp = "[\\w-]+", message = "字典编码只允许英文字母、下划线、短横线")
  String code;
  /**
   * 字典描述
   */
  String label;
  /**
   * 排序号
   */
  Integer ordinal;
  /**
   * 上级字典ID
   */
  String parentId;

  @Override
  public Dict into() {
    return Converter.INSTANCE.from(this);
  }

  @Mapper
  interface Converter extends From<ModifyDictReq, Dict> {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    @Mapping(target = "tag", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Override
    Dict from(ModifyDictReq modifyDictReq);
  }
}
