package io.github.xezzon.geom.dict.service.impl;

import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.geom.dict.repository.wrapper.DictDAO;
import io.github.xezzon.geom.dict.service.DictService;
import io.github.xezzon.tao.exception.ClientException;
import io.github.xezzon.tao.retrieval.CommonQuery;
import io.github.xezzon.tao.tree.Tree;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author xezzon
 */
@Service
public class DictServiceImpl implements DictService {

  private final transient DictDAO dictDAO;

  public DictServiceImpl(DictDAO dictDAO) {
    this.dictDAO = dictDAO;
  }

  @Override
  public Page<Dict> dictTagPage(CommonQuery params) {
    return dictDAO.query(params);
  }

  @Override
  public void addDict(Dict dict) {
    Dict exist = dictDAO.get().findByTagAndCode(dict.getTag(), dict.getCode());
    if (exist != null) {
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
    dictDAO.get().deleteAllByIdInBatch(dictIdSet);
  }

  @Override
  public void modifyDict(Dict dict) {
    dictDAO.get().save(dict);
  }

  @Override
  public Dict dictByTagAndCode(String tag, String code) {
    return dictDAO.get().findByTagAndCode(tag, code);
  }
}
