package io.github.xezzon.geom;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMember;
import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.repository.GroupMemberRepository;
import io.github.xezzon.geom.auth.repository.GroupRepository;
import io.github.xezzon.geom.auth.repository.UserRepository;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestData implements CommandLineRunner {

  @Deprecated
  private static int executeTimes = 1;

  public static final List<User> USERS = new ArrayList<>();
  static {
    for (int i = 0; i < Byte.MAX_VALUE; i++) {
      User user = new User();
      user.setId(IdUtil.getSnowflakeNextIdStr());
      user.setUsername(RandomUtil.randomString(6));
      user.setNickname(RandomUtil.randomString(6));
      user.setPlaintext(RandomUtil.randomString(6));
      USERS.add(user);
    }
  }

  public static final List<Group> GROUPS = new ArrayList<>();
  static {
    for (int i = 0; i < Byte.MAX_VALUE; i++) {
      Group group = new Group();
      group.setId(IdUtil.getSnowflakeNextIdStr());
      group.setCode(RandomUtil.randomString(6));
      group.setName(RandomUtil.randomString(6));
      group.setOwnerId(RandomUtil.randomEle(USERS).getId());
      GROUPS.add(group);
    }
  }

  public static final List<GroupMember> GROUP_MEMBERS = new ArrayList<>();
  static {
    List<GroupMember> members = new ArrayList<>();
    for (Group group : GROUPS) {
      GroupMember member = new GroupMember();
      member.setId(IdUtil.getSnowflakeNextIdStr());
      member.setGroupId(group.getId());
      member.setUserId(group.getOwnerId());
      members.add(member);
    }
    for (int i = 0; i < GROUPS.size() * USERS.size(); i++) {
      GroupMember member = new GroupMember();
      member.setId(IdUtil.getSnowflakeNextIdStr());
      member.setGroupId(RandomUtil.randomEle(GROUPS).getId());
      member.setUserId(RandomUtil.randomEle(USERS).getId());
      members.add(member);
    }
    Collection<GroupMember> distinctMembers = members.stream()
        .collect(Collectors.toMap(
            o -> o.getGroupId() + ":" + o.getUserId(),
            o -> o,
            (v1, v2) -> v1
        ))
        .values();
    GROUP_MEMBERS.addAll(distinctMembers);
  }

  @Resource
  private transient UserRepository userRepository;
  @Resource
  private transient GroupRepository groupRepository;
  @Resource
  private transient GroupMemberRepository memberRepository;

  @Override
  public void run(String... args) {
    if (executeTimes-- <= 0) {
      return;
    }

    userRepository.saveAllAndFlush(USERS);
    groupRepository.saveAllAndFlush(GROUPS);
    memberRepository.saveAllAndFlush(GROUP_MEMBERS);
  }
}
