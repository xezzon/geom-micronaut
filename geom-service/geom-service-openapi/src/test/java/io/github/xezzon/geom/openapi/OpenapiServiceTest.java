package io.github.xezzon.geom.openapi;

import cn.hutool.core.util.RandomUtil;
import io.github.xezzon.geom.exception.RepeatDataException;
import io.github.xezzon.geom.core.exception.OpenapiPublishedException;
import io.github.xezzon.geom.core.exception.OpenapiUnpublishedException;
import io.github.xezzon.geom.openapi.model.AddOpenapiQuery;
import io.github.xezzon.geom.openapi.model.ModifyOpenapiQuery;
import io.github.xezzon.geom.openapi.model.PublishOpenapiQuery;
import io.github.xezzon.geom.openapi.repository.OpenapiInstanceRepository;
import io.github.xezzon.geom.openapi.repository.OpenapiRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@MicronautTest(transactional = false)
@TestInstance(Lifecycle.PER_CLASS)
class OpenapiServiceTest {

  private static final List<Openapi> OPENAPI_LIST = new ArrayList<>();

  static {
    for (int i = 0, cnt = Byte.MAX_VALUE; i < cnt; i++) {
      Openapi openapi = new Openapi();
      openapi.setName(RandomUtil.randomString(6));
      openapi.setCode(RandomUtil.randomString(8));
      openapi.setPath(RandomUtil.randomString(8));
      OPENAPI_LIST.add(openapi);
    }
    for (int i = 0, cnt = Byte.MAX_VALUE; i < cnt; i++) {
      Openapi openapi = new Openapi();
      openapi.setName(RandomUtil.randomString(6));
      openapi.setCode(RandomUtil.randomString(8));
      openapi.setPath(RandomUtil.randomString(8));
      openapi.setPublishTime(
          Instant.now().plus(RandomUtil.randomInt(120), ChronoUnit.HOURS)
      );
      OPENAPI_LIST.add(openapi);
    }
    for (int i = 0, cnt = Byte.MAX_VALUE; i < cnt; i++) {
      Openapi openapi = new Openapi();
      openapi.setName(RandomUtil.randomString(6));
      openapi.setCode(RandomUtil.randomString(8));
      openapi.setPath(RandomUtil.randomString(8));
      // 已发布
      openapi.setPublishTime(
          Instant.now().minus(RandomUtil.randomInt(120), ChronoUnit.HOURS)
      );
      OPENAPI_LIST.add(openapi);
    }
  }

  @Inject
  private transient OpenapiService service;
  @Inject
  private transient OpenapiRepository repository;
  @Inject
  private transient OpenapiInstanceRepository instanceRepository;

  @BeforeAll
  void setUp() {
    repository.saveAll(OPENAPI_LIST);
  }

  @Test
  void addOpenapi() {
    List<Openapi> before = repository.findAll();
    AddOpenapiQuery query = new AddOpenapiQuery();
    query.setName(RandomUtil.randomString(6));
    query.setCode(RandomUtil.randomString(6));
    query.setPath(RandomUtil.randomString(6));
    Openapi openapi = query.into();
    service.addOpenapi(openapi);
    List<Openapi> after = repository.findAll();
    Assertions.assertEquals(before.size() + 1, after.size());
    Optional<Openapi> exist = repository.findById(openapi.getId());
    Assertions.assertTrue(exist.isPresent());
  }

  @Test
  void addOpenapi_repeat() {
    AddOpenapiQuery query = new AddOpenapiQuery();
    query.setName(RandomUtil.randomString(6));
    query.setCode(RandomUtil.randomEle(OPENAPI_LIST).getCode());
    query.setPath(RandomUtil.randomString(6));
    Assertions.assertThrows(RepeatDataException.class, () ->
        service.addOpenapi(query.into())
    );
  }

  @Test
  void modifyOpenapi() {
    Openapi before = OPENAPI_LIST.parallelStream()
        .filter((o) -> !o.isPublished())
        .findAny().get();
    ModifyOpenapiQuery query = new ModifyOpenapiQuery();
    query.setId(before.getId());
    query.setName(RandomUtil.randomString(6));
    query.setCode(RandomUtil.randomString(6));
    query.setPath(RandomUtil.randomString(6));
    Openapi openapi = query.into();
    service.modifyOpenapi(openapi);
    Optional<Openapi> after = repository.findById(query.getId());
    Assertions.assertTrue(after.isPresent());
    Assertions.assertEquals(query.getName(), after.get().getName());
    Assertions.assertEquals(query.getCode(), after.get().getCode());
    Assertions.assertEquals(query.getPath(), after.get().getPath());
    before.setName(query.getName());
    before.setCode(query.getCode());
    before.setPath(query.getPath());
  }

  @Test
  void modifyOpenapi_notRepeat() {
    Openapi before = OPENAPI_LIST.parallelStream()
        .filter((o) -> !o.isPublished())
        .findAny().get();
    ModifyOpenapiQuery query = new ModifyOpenapiQuery();
    query.setId(before.getId());
    query.setName(RandomUtil.randomString(6));
    query.setCode(before.getCode());
    query.setPath(RandomUtil.randomString(6));
    Openapi openapi = query.into();
    service.modifyOpenapi(openapi);
    Optional<Openapi> after = repository.findById(query.getId());
    Assertions.assertTrue(after.isPresent());
    Assertions.assertEquals(query.getName(), after.get().getName());
    Assertions.assertEquals(query.getCode(), after.get().getCode());
    Assertions.assertEquals(query.getPath(), after.get().getPath());
    before.setName(query.getName());
    before.setCode(query.getCode());
    before.setPath(query.getPath());
  }

  @Test
  void modifyOpenapi_repeat() {
    Openapi before = OPENAPI_LIST.parallelStream()
        .filter((o) -> !o.isPublished())
        .findAny().get();
    Openapi before2 = OPENAPI_LIST.parallelStream()
        .filter((o) -> !o.isPublished())
        .filter((o) -> !Objects.equals(o.getId(), before.getId()))
        .findAny().get();
    ModifyOpenapiQuery query = new ModifyOpenapiQuery();
    query.setId(before.getId());
    query.setName(RandomUtil.randomString(6));
    query.setCode(before2.getCode());
    query.setPath(RandomUtil.randomString(6));
    Assertions.assertThrows(RepeatDataException.class, () ->
        service.modifyOpenapi(query.into())
    );
  }

  @Test
  void modifyOpenapi_published() {
    Openapi before = OPENAPI_LIST.parallelStream()
        .filter(Openapi::isPublished)
        .findAny().get();
    ModifyOpenapiQuery query = new ModifyOpenapiQuery();
    query.setId(before.getId());
    query.setName(RandomUtil.randomString(6));
    query.setCode(RandomUtil.randomString(6));
    query.setPath(RandomUtil.randomString(6));
    Assertions.assertThrows(OpenapiPublishedException.class, () ->
        service.modifyOpenapi(query.into())
    );
  }

  @Test
  void removeOpenapi() {
    Openapi openapi = OPENAPI_LIST.parallelStream()
        .filter(o -> !o.isPublished())
        .findAny().get();
    List<Openapi> before = repository.findAll();
    service.removeOpenapi(openapi.getId());
    List<Openapi> after = repository.findAll();
    Assertions.assertEquals(before.size() - 1, after.size());
    Optional<Openapi> exist = repository.findById(openapi.getId());
    Assertions.assertTrue(exist.isEmpty());
    OPENAPI_LIST.remove(openapi);
  }

  @Test
  void removeOpenapi_published() {
    String id = OPENAPI_LIST.parallelStream()
        .filter(Openapi::isPublished)
        .map(Openapi::getId)
        .findAny().get();
    Assertions.assertThrows(OpenapiPublishedException.class, () ->
        service.removeOpenapi(id)
    );
  }

  @Test
  void publishOpenapi() {
    Openapi before = OPENAPI_LIST.parallelStream()
        .filter(o -> !o.isPublished())
        .findAny().get();
    PublishOpenapiQuery query = new PublishOpenapiQuery();
    query.setId(before.getId());
    query.setPublishTime(Instant.now());
    Openapi openapi = query.into();
    service.modifyOpenapi(openapi);
    Optional<Openapi> after = repository.findById(query.getId());
    Assertions.assertTrue(after.isPresent());
    Assertions.assertTrue(after.get().isPublished());
    before.setPublishTime(query.getPublishTime());
  }

  @Test
  void publishOpenapi_published() {
    Openapi before = OPENAPI_LIST.parallelStream()
        .filter(Openapi::isPublished)
        .findAny().get();
    PublishOpenapiQuery query = new PublishOpenapiQuery();
    query.setId(before.getId());
    query.setPublishTime(Instant.now());
    Assertions.assertThrows(OpenapiPublishedException.class, () ->
        service.modifyOpenapi(query.into())
    );
  }

  @Test
  void subscribeOpenapi() {
    Openapi openapi = OPENAPI_LIST.parallelStream()
        .filter(Openapi::isPublished)
        .findAny().get();
    String ownerId = RandomUtil.randomString(32);
    OpenapiInstance openapiInstance = new OpenapiInstance();
    openapiInstance.setApiId(openapi.getId());
    openapiInstance.setOwnerId(ownerId);
    service.subscribeOpenapi(openapiInstance);
    Optional<OpenapiInstance> after = instanceRepository
        .findByApiIdAndOwnerId(openapi.getId(), ownerId);
    Assertions.assertTrue(after.isPresent());
  }

  @Test
  void subscribeOpenapi_unpublished() {
    Openapi openapi = OPENAPI_LIST.parallelStream()
        .filter(o -> !o.isPublished())
        .findAny().get();
    String ownerId = RandomUtil.randomString(32);
    OpenapiInstance openapiInstance = new OpenapiInstance();
    openapiInstance.setApiId(openapi.getId());
    openapiInstance.setOwnerId(ownerId);
    Assertions.assertThrows(
        OpenapiUnpublishedException.class,
        () -> service.subscribeOpenapi(openapiInstance)
    );
  }
}
