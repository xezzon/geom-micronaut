package io.github.xezzon.geom.dict.service.impl;

import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.geom.dict.repository.wrapper.DictDAO;
import io.github.xezzon.geom.dict.service.DictService;
import io.github.xezzon.tao.exception.ClientException;
import io.github.xezzon.tao.retrieval.CommonQuery;
import io.github.xezzon.tao.tree.Tree;
import io.micronaut.data.model.Page;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xezzon
 */
@Singleton
public class DictServiceImpl implements DictService {

  protected final transient DictDAO dictDAO;

  public DictServiceImpl(DictDAO dictDAO) {
    this.dictDAO = dictDAO;
  }

  @Override
  public Page<Dict> dictTagPage(CommonQuery params) {
    return dictDAO.query(params);
  }

  @Override
  public void addDict(Dict dict) {
    Optional<Dict> exist = dictDAO.get().findByTagAndCode(dict.getTag(), dict.getCode());
    if (exist.isEmpty()) {
      throw new ClientException("字典码已存在");
    }
    dictDAO.get().save(dict);
  }

  @Override
  public List<Dict> dictListByTag(String tag) {
    List<Dict> dictList = dictDAO.get().findByTag(tag);
    return Tree.fold(dictList);
  }

  @Override
  public void removeDict(String id) {
    List<Dict> dictList = Tree.topDown(Collections.singleton(id), -1,
        dictDAO.get()::findByParentIdIn);
    Set<String> dictIdSet = dictList.parallelStream()
        .map(Dict::getId)
        .collect(Collectors.toSet());
    dictIdSet.add(id);
    dictDAO.get().deleteByIdIn(dictIdSet);
  }

  @Override
  public void modifyDict(Dict dict) {
    dictDAO.get().save(dict);
  }

  @Override
  public Dict dictByTagAndCode(String tag, String code) {
    return dictDAO.get().findByTagAndCode(tag, code).orElse(null);
  }
}
