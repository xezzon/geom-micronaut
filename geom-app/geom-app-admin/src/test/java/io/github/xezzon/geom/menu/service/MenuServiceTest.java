package io.github.xezzon.geom.menu.service;

import cn.hutool.core.util.RandomUtil;
import io.github.xezzon.geom.menu.domain.Menu;
import io.github.xezzon.geom.menu.repository.MenuRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * @author xezzon
 */
@MicronautTest
@TestInstance(Lifecycle.PER_CLASS)
class MenuServiceTest {

  private static final List<Menu> MENUS = new ArrayList<>();

  @Inject
  protected transient MenuService service;
  @Inject
  protected transient MenuRepository repository;

  @BeforeAll
  void setUp() {
    for (int i = 0; i < 8; i++) {
      // 第一层
      Menu menu = new Menu();
      menu.setPath(RandomUtil.randomString(4));
      menu.setName(RandomUtil.randomString(4));
      menu.setIcon(RandomUtil.randomString(4));
      menu.setOrdinal(i);
      menu.setHideInMenu(RandomUtil.randomBoolean());
      menu.setParentId("0");
      menu.setChildren(new ArrayList<>());
      MENUS.add(menu);
    }
    repository.saveAll(MENUS);
    List<Menu> partOfFirstLevel = RandomUtil.randomEleList(MENUS, MENUS.size() - 2);
    for (Menu parent : partOfFirstLevel) {
      // 第二层
      for (int i = 0, cnt = RandomUtil.randomInt(2, 8); i < cnt; i++) {
        Menu menu = new Menu();
        menu.setPath(RandomUtil.randomString(4));
        menu.setName(RandomUtil.randomString(4));
        menu.setComponent(RandomUtil.randomString(4));
        menu.setOrdinal(i);
        menu.setHideInMenu(RandomUtil.randomBoolean());
        menu.setParentId(parent.getId());
        menu.setParent(parent);
        menu.setChildren(new ArrayList<>());
        parent.getChildren().add(menu);
      }
    }
    List<Menu> secondLevel = MENUS.parallelStream()
        .map(Menu::getChildren)
        .flatMap(Collection::parallelStream)
        .toList();
    repository.saveAll(secondLevel);
    List<Menu> partOfSecondLevel = RandomUtil.randomEleList(secondLevel, secondLevel.size() / 3);
    for (Menu parent : partOfSecondLevel) {
      // 第三层
      for (int i = 0, cnt = RandomUtil.randomInt(2, 4); i < cnt; i++) {
        Menu menu = new Menu();
        menu.setPath(RandomUtil.randomString(4));
        menu.setName(RandomUtil.randomString(4));
        menu.setOrdinal(i);
        menu.setHideInMenu(RandomUtil.randomBoolean());
        menu.setParentId(parent.getId());
        menu.setParent(parent);
        menu.setChildren(new ArrayList<>());
        parent.getChildren().add(menu);
      }
    }
    List<Menu> thirdLevel = partOfSecondLevel.parallelStream()
        .map(Menu::getChildren)
        .flatMap(Collection::parallelStream)
        .toList();
    repository.saveAll(thirdLevel);
  }

  @Test
  void menuTree() {
    List<Menu> firstLevel = service.menuTree("0");
    Assertions.assertArrayEquals(MENUS.toArray(), firstLevel.toArray());
    Assertions.assertArrayEquals(
        MENUS.parallelStream()
            .map(Menu::getChildren)
            .flatMap(Collection::parallelStream)
            .toArray(),
        firstLevel.parallelStream()
            .map(Menu::getChildren)
            .filter(Objects::nonNull)
            .flatMap(Collection::parallelStream)
            .toArray()
    );
    Assertions.assertArrayEquals(
        MENUS.parallelStream()
            .map(Menu::getChildren)
            .flatMap(Collection::parallelStream)
            .map(Menu::getChildren)
            .flatMap(Collection::parallelStream)
            .toArray(),
        firstLevel.parallelStream()
            .map(Menu::getChildren)
            .filter(Objects::nonNull)
            .flatMap(Collection::parallelStream)
            .map(Menu::getChildren)
            .filter(Objects::nonNull)
            .flatMap(Collection::parallelStream)
            .toArray()
    );
  }
}