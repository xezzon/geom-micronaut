package io.github.xezzon.geom.dict;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xezzon
 */
@Getter
@Setter
@ToString
public class DictDTO {

  private String tag;
  private String code;
  private String label;
  private Integer ordinal;
  private List<DictDTO> children;
}
