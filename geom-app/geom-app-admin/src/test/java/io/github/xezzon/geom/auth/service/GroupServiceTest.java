package io.github.xezzon.geom.auth.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.repository.GroupRepository;
import io.github.xezzon.tao.exception.ClientException;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GroupServiceTest {

  @Resource
  private transient GroupService service;
  @Resource
  private transient GroupRepository repository;

  @Test
  void addGroup() {
    Group group = new Group();
    String flakeId = IdUtil.getSnowflakeNextIdStr();
    String code = RandomUtil.randomString(6);
    String name = RandomUtil.randomString(6);
    String ownerId = IdUtil.getSnowflakeNextIdStr();
    group.setId(flakeId);
    group.setCode(code);
    group.setName(name);
    group.setOwnerId(ownerId);
    service.addGroup(group);

    Optional<Group> existGroup = repository.findById(group.getId());
    Assertions.assertTrue(existGroup.isPresent());
    Assertions.assertNotEquals(flakeId, existGroup.get().getId());

    Assertions.assertThrows(ClientException.class, () -> {
      Group group1 = new Group();
      group1.setCode(code);
      group1.setOwnerId(ownerId);
      service.addGroup(group1);
    });
  }

  @Test
  void joinGroup() {
    Group group = new Group();
    group.setCode(RandomUtil.randomString(6));
    group.setOwnerId(IdUtil.getSnowflakeNextIdStr());
    service.addGroup(group);
    List<String> usersId = IntStream.range(1, 100)
        .mapToObj(i -> IdUtil.getSnowflakeNextIdStr())
        .toList();
    service.joinGroup(group.getId(), usersId);
  }
}