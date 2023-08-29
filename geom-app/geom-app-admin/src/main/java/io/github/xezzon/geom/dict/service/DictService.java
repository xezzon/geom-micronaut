package io.github.xezzon.geom.dict.service;

import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.tao.retrieval.CommonQuery;
import io.micronaut.data.model.Page;
import java.util.List;

/**
 * @author xezzon
 */
public interface DictService {

  /**
   * 分页查询字典目列表
   * @param params 查询参数
   * @return 字典目列表
   */
  Page<Dict> dictTagPage(CommonQuery params);

  /**
   * 新增字典
   * @param dict 字典信息
   */
  void addDict(Dict dict);

  /**
   * 查询字典目下的字典集合
   * @param tag 字典目
   * @return 字典信息集合（树形结构）
   */
  List<Dict> dictListByTag(String tag);

  /**
   * 递归删除字典及其子级
   * @param id 字典ID
   */
  void removeDict(String id);

  /**
   * 修改字典
   * @param dict 新的字典信息
   */
  void modifyDict(Dict dict);

  /**
   * 定位字典
   * @param tag 字典目
   * @param code 字典码
   * @return 字典信息
   */
  Dict dictByTagAndCode(String tag, String code);
}
