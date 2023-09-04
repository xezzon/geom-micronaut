package io.github.xezzon.geom;

import cn.hutool.core.util.RandomUtil;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMember;
import io.github.xezzon.geom.auth.domain.User;
import io.github.xezzon.geom.auth.repository.GroupMemberRepository;
import io.github.xezzon.geom.auth.repository.GroupRepository;
import io.github.xezzon.geom.auth.repository.UserRepository;
import io.micronaut.context.annotation.Bean;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Bean
public class TestData {

  public static final List<User> USERS = new ArrayList<>();

  public static final List<Group> GROUPS = new ArrayList<>();

  public static final List<GroupMember> GROUP_MEMBERS = new ArrayList<>();

  @Inject
  protected transient UserRepository userRepository;
  @Inject
  protected transient GroupRepository groupRepository;
  @Inject
  protected transient GroupMemberRepository memberRepository;

  @EventListener
  public void init(ApplicationStartupEvent event) {
    for (int i = 0; i < Byte.MAX_VALUE; i++) {
      User user = new User();
      user.setUsername(RandomUtil.randomString(6));
      user.setNickname(RandomUtil.randomString(6));
      user.setPlaintext(RandomUtil.randomString(6));
      USERS.add(user);
    }
    userRepository.saveAll(USERS);
    for (int i = 0; i < Byte.MAX_VALUE; i++) {
      Group group = new Group();
      group.setCode(RandomUtil.randomString(6));
      group.setName(RandomUtil.randomString(6));
      group.setOwnerId(RandomUtil.randomEle(USERS).getId());
      GROUPS.add(group);
    }
    groupRepository.saveAll(GROUPS);
    List<GroupMember> members = new ArrayList<>();
    for (Group group : GROUPS) {
      GroupMember member = new GroupMember();
      member.setGroupId(group.getId());
      member.setUserId(group.getOwnerId());
      members.add(member);
    }
    for (int i = 0; i < GROUPS.size() * USERS.size(); i++) {
      GroupMember member = new GroupMember();
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
    memberRepository.saveAll(GROUP_MEMBERS);
  }
}
