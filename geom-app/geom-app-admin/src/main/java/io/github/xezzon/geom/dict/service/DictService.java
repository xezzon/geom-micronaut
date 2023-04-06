package io.github.xezzon.geom.dict.service;

import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.tao.retrieval.CommonQuery;
import io.github.xezzon.tao.tree.TreeNode;
import java.util.List;
import org.springframework.data.domain.Page;

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
  List<? extends TreeNode<Dict, ?>> dictListByTag(String tag);

  /**
   * 递归删除字典及其子级
   * @param id 字典ID
   */
  void removeDict(String id);
}
