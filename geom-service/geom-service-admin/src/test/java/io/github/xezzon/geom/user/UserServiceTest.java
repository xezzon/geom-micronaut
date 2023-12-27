package io.github.xezzon.geom.user;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import io.github.xezzon.geom.user.domain.User;
import io.github.xezzon.geom.user.repository.UserRepository;
import io.github.xezzon.tao.exception.BaseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xezzon
 */
@MicronautTest
class UserServiceTest {

  @Inject
  protected transient UserService userService;
  @Inject
  protected transient UserRepository userRepository;

  @Test
  void addUser() {
    User user = new User()
        .setUsername(RandomUtil.randomString(6))
        .setNickname(RandomUtil.randomString(6))
        .setPlaintext(RandomUtil.randomString(6))
        .setCreateTime(LocalDateTime.now().minusMonths(1));
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
    Assertions.assertThrows(BaseException.class, () -> userService.addUser(user));
  }
}
