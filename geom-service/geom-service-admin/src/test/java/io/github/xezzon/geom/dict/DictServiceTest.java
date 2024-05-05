package io.github.xezzon.geom.dict;

import cn.hutool.core.util.RandomUtil;
import io.github.xezzon.geom.dict.domain.Dict;
import io.github.xezzon.geom.dict.repository.DictRepository;
import io.github.xezzon.geom.exception.RepeatDataException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * @author xezzon
 */
@MicronautTest(rollback = false)
@TestInstance(Lifecycle.PER_CLASS)
class DictServiceTest {

  @Inject
  private DictService service;
  @Inject
  private DictRepository repository;

  @BeforeAll
  void setUp() {
    final List<Dict> dictTags = new ArrayList<>();
    for (int i = 0, cnt = Byte.MAX_VALUE; i < cnt; i++) {
      Dict dict = new Dict();
      dict.setCode(RandomUtil.randomString(9));
      dict.setLabel(RandomUtil.randomString(9));
      dict.setParentId(Dict.ROOT_ID);
      dict.setTag(Dict.ROOT_CODE);
      dict.setOrdinal(RandomUtil.randomInt());
      repository.save(dict);
      dictTags.add(dict);
    }
    for (Dict dictTag : dictTags) {
      for (int i = 0, cnt = RandomUtil.randomInt(2, 16); i < cnt; i++) {
        Dict dict = new Dict();
        dict.setCode(RandomUtil.randomString(9));
        dict.setLabel(RandomUtil.randomString(9));
        dict.setParentId(dictTag.getId());
        dict.setTag(dictTag.getCode());
        dict.setOrdinal(RandomUtil.randomInt());
        repository.save(dict);
      }
    }
  }

  @Test
  void addDict() {
    final List<Dict> dataset = repository.findAll();
    Dict dictTag = dataset.parallelStream()
        .filter((o) -> Objects.equals(o.getParentId(), Dict.ROOT_ID))
        .findAny().get();
    Dict dict = new Dict();
    dict.setCode(RandomUtil.randomString(6));
    dict.setLabel(RandomUtil.randomString(9));
    dict.setTag(dictTag.getCode());
    dict.setParentId(dictTag.getParentId());
    dict.setOrdinal(RandomUtil.randomInt());
    service.addDict(dict);
    Assertions.assertEquals(dataset.size() + 1, repository.count());
  }

  @Test
  void addDictTag() {
    final List<Dict> dataset = repository.findAll();
    Dict dict = new Dict();
    dict.setCode(RandomUtil.randomString(6));
    dict.setLabel(RandomUtil.randomString(9));
    dict.setTag(Dict.ROOT_CODE);
    dict.setParentId(Dict.ROOT_CODE);
    dict.setOrdinal(RandomUtil.randomInt());
    service.addDict(dict);
    Assertions.assertEquals(dataset.size() + 1, repository.count());
  }

  @Test
  void addDict_exist() {
    final List<Dict> dataset = repository.findAll();
    Dict exist = dataset.parallelStream()
        .findAny().get();
    Dict dict = new Dict();
    dict.setCode(exist.getCode());
    dict.setLabel(RandomUtil.randomString(9));
    dict.setTag(exist.getTag());
    dict.setParentId(exist.getParentId());
    dict.setOrdinal(RandomUtil.randomInt());
    Assertions.assertThrows(RepeatDataException.class, () -> service.addDict(dict));
  }

  void modifyDict() {
    final List<Dict> dataset = repository.findAll();
    Dict exist = dataset.parallelStream()
        .filter((o) -> !Objects.equals(o.getParentId(), Dict.ROOT_ID))
        .findAny().get();
    Dict dict = new Dict();
    dict.setId(exist.getId());
    dict.setTag(exist.getTag());
    dict.setCode(RandomUtil.randomString(6));
    dict.setLabel(RandomUtil.randomString(9));
    dict.setParentId(exist.getParentId());
    dict.setOrdinal(RandomUtil.randomInt());
    service.modifyDict(dict);
    Optional<Dict> after = repository.findByTagAndCode(exist.getTag(), dict.getCode());
    Assertions.assertTrue(after.isPresent());
    Assertions.assertEquals(dict.getCode(), after.get().getCode());
    Assertions.assertEquals(dict.getLabel(), after.get().getLabel());
    Assertions.assertEquals(dict.getParentId(), exist.getParentId());
    Assertions.assertEquals(dict.getTag(), exist.getTag());
    Assertions.assertEquals(dict.getOrdinal(), after.get().getOrdinal());
  }

  void modifyDictTag() {
    final List<Dict> dataset = repository.findAll();
    Dict exist = dataset.parallelStream()
        .filter((o) -> Objects.equals(o.getParentId(), Dict.ROOT_ID))
        .findAny().get();
    Dict dictTag = new Dict();
    dictTag.setId(exist.getId());
    dictTag.setTag(exist.getTag());
    dictTag.setCode(RandomUtil.randomString(6));
    dictTag.setLabel(RandomUtil.randomString(9));
    dictTag.setOrdinal(RandomUtil.randomInt());
    service.modifyDict(dictTag);
    Optional<Dict> after = repository.findByTagAndCode(Dict.ROOT_CODE, dictTag.getCode());
    Assertions.assertTrue(after.isPresent());
    Assertions.assertEquals(dictTag.getCode(), after.get().getCode());
    Assertions.assertEquals(dictTag.getLabel(), after.get().getLabel());
    Assertions.assertEquals(dictTag.getOrdinal(), after.get().getOrdinal());
    List<Dict> children = repository.findByTag(dictTag.getCode());
    Assertions.assertFalse(children.isEmpty());
    Assertions.assertEquals(dictTag.getCode(), children.get(0).getTag());
  }

  @Test
  void modifyDict_exist() {
    final List<Dict> dataset = repository.findAll();
    Dict exist = dataset.parallelStream()
        .findAny().get();
    Dict another = dataset.parallelStream()
        .filter((o) -> Objects.equals(o.getParentId(), exist.getParentId()))
        .filter((o) -> !Objects.equals(o.getId(), exist.getId()))
        .findAny().get();
    Dict dict = new Dict();
    dict.setId(exist.getId());
    dict.setTag(another.getTag());
    dict.setCode(another.getCode());
    Assertions.assertThrows(RepeatDataException.class, () -> service.modifyDict(dict));
  }

  @Test
  void removeDict() {
    final List<Dict> dataset = repository.findAll();
    Dict dict = dataset.parallelStream()
        .findAny().get();
    service.removeDict(dict.getId());
    Assertions.assertEquals(dataset.size() - 1, repository.count());
  }
}
