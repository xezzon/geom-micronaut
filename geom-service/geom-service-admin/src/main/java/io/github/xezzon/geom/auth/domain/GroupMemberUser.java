package io.github.xezzon.geom.auth.domain;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xezzon
 */
@Data
public class GroupMemberUser {

  private String groupId;
  private String userId;
  private String username;
  private String nickname;

  public static GroupMemberUser build(@NotNull Group group, @NotNull User user) {
    return GroupMemberUserConverter.INSTANCE.convert(group, user);
  }
}

@Mapper
interface GroupMemberUserConverter {

  GroupMemberUserConverter INSTANCE = Mappers.getMapper(GroupMemberUserConverter.class);

  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "groupId", source = "group.id")
  GroupMemberUser convert(Group group, User user);
}
