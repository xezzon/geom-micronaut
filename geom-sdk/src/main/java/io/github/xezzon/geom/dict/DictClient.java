package io.github.xezzon.geom.dict;

import java.util.List;

/**
 * @author xezzon
 */
public interface DictClient {

  /**
   * 查询字典目下的字典集合
   * @param tag 字典目
   * @return 字典信息集合（树形结构）
   */
  List<DictDTO> dictListByTag(String tag);

  /**
   * 定位字典
   * @param tag 字典目
   * @param code 字典码
   * @return 字典信息
   */
  DictDTO dictByTagAndCode(String tag, String code);
}
