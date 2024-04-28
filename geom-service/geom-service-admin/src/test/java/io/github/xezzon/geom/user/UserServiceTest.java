package io.github.xezzon.geom.user;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import io.github.xezzon.geom.exception.RepeatDataException;
import io.github.xezzon.geom.user.domain.User;
import io.github.xezzon.geom.user.repository.UserRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * @author xezzon
 */
@MicronautTest(transactional = false)
@TestInstance(Lifecycle.PER_CLASS)
class UserServiceTest {

  @Inject
  protected transient UserService userService;
  @Inject
  protected transient UserRepository userRepository;

  @BeforeAll
  void beforeAll() {
    for (int i = 0, cnt = Byte.MAX_VALUE; i < cnt; i++) {
      User user = new User();
      user.setUsername(RandomUtil.randomString(9));
      user.setNickname(RandomUtil.randomString(9));
      user.setPlaintext(RandomUtil.randomString(9));
      userRepository.save(user);
    }
  }

  @Test
  void addUser() {
    User user = new User()
        .setUsername(RandomUtil.randomString(6))
        .setNickname(RandomUtil.randomString(6))
        .setPlaintext(RandomUtil.randomString(6))
        .setCreateTime(Instant.now().minus(30, ChronoUnit.DAYS));
    User register = userService.addUser(user);
    /* 测试返回值 */
    Assertions.assertNotNull(register.getId());
    Assertions.assertNotNull(register.getNickname());
    Assertions.assertNull(register.getCipher());
    /* 测试结果 */
    Optional<User> existUser = userRepository.findByUsername(user.getUsername());
    Assertions.assertTrue(existUser.isPresent());
    Assertions.assertTrue(
        BCrypt.checkpw(user.getPlaintext(), existUser.get().getCipher())
    );
    /* 测试预期异常 */
    Assertions.assertThrows(RepeatDataException.class, () -> userService.addUser(user));
  }

  @Test
  void getUserByUsername() {
    final List<User> dataset = userRepository.findAll();
    /* 正常查询 */
    User exist = dataset.stream().findAny().get();
    User actual = userService.getUserByUsername(exist.getUsername());
    Assertions.assertEquals(exist.getId(), actual.getId());
    /* 查询不存在的用户 */
    User nonexistent = userService.getUserByUsername(RandomUtil.randomString(8));
    Assertions.assertNull(nonexistent);
  }

  @Test
  void getUserById() {
    final List<User> dataset = userRepository.findAll();
    /* 正常查询 */
    User exist = dataset.stream().findAny().get();
    User actual = userService.getUserById(exist.getId());
    Assertions.assertNotNull(actual);
    /* 查询不存在的用户 */
    User nonexistent = userService.getUserById("-1");
    Assertions.assertNull(nonexistent);
  }
}
