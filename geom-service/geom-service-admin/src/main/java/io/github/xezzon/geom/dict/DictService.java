package io.github.xezzon.geom.dict;

import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.geom.exception.NonexistentDataException;
import io.github.xezzon.geom.exception.RepeatDataException;
import io.github.xezzon.tao.retrieval.CommonQuery;
import io.github.xezzon.tao.tree.Tree;
import io.micronaut.data.model.Page;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xezzon
 */
@Singleton
public class DictService {

  private final DictDAO dictDAO;

  DictService(DictDAO dictDAO) {
    this.dictDAO = dictDAO;
  }

  /**
   * 根据查询条件获取字典标签分页信息
   * @param params 查询条件
   * @return 分页对象，包含字典标签信息
   */
  protected Page<Dict> dictTagPage(CommonQuery params) {
    return dictDAO.query(params);
  }

  /**
   * 添加字典项
   * @param dict 要添加的字典项
   * @throws RepeatDataException 如果字典标签和字典码已存在，则抛出此异常
   */
  protected void addDict(Dict dict) {
    checkRepeat(dict);
    dictDAO.get().save(dict);
  }

  /**
   * 根据字典标签获取字典列表
   * @param tag 字典标签
   * @return 字典列表
   */
  protected List<Dict> dictListByTag(String tag) {
    List<Dict> dictList = dictDAO.get().findByTag(tag);
    return Tree.fold(dictList);
  }

  /**
   * 根据给定的字典ID，删除该字典及其所有子字典
   * @param id 字典ID
   */
  protected void removeDict(String id) {
    List<Dict> dictList = Tree.topDown(Collections.singleton(id), -1,
        dictDAO.get()::findByParentIdIn);
    Set<String> dictIdSet = dictList.parallelStream()
        .map(Dict::getId)
        .collect(Collectors.toSet());
    dictIdSet.add(id);
    dictDAO.get().deleteByIdIn(dictIdSet);
  }

  /**
   * 修改字典项
   * @param dict 要修改的字典项
   */
  protected void modifyDict(Dict dict) {
    Optional<Dict> exist = dictDAO.get().findById(dict.getId());
    if (exist.isEmpty()) {
      throw new NonexistentDataException("字典已被删除: " + dict.getCode());
    }
    dict.setTag(exist.get().getTag());
    checkRepeat(dict);
    /* 持久化 */
    dictDAO.update(dict);
    if (Objects.equals(exist.get().getParentId(), Dict.ROOT_ID)) {
      dictDAO.updateTag(exist.get().getCode(), dict.getCode());
    }
  }

  /**
   * 根据标签和编码获取字典项
   * @param tag 字典目
   * @param code 字典码
   * @return 匹配到的字典项，若未找到则返回null
   */
  protected Dict dictByTagAndCode(String tag, String code) {
    return dictDAO.get().findByTagAndCode(tag, code).orElse(null);
  }

  private void checkRepeat(Dict dict) {
    Dict exist = this.dictByTagAndCode(dict.getTag(), dict.getCode());
    if (exist != null && !Objects.equals(dict.getId(), exist.getId())) {
      throw new RepeatDataException("字典码已存在");
    }
  }
}
