package io.github.xezzon.geom.auth.adaptor;

import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.service.GroupService;
import java.util.Collection;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xezzon
 */
@RestController
@RequestMapping("/group")
public class GroupController {

  private final transient GroupService groupService;

  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }


  /**
   * 新增用户组
   * @param group 用户组信息
   */
  @PostMapping()
  public void addGroup(@RequestBody Group group) {
    groupService.addGroup(group);
  }

  /**
   * 将用户加入用户组
   * @param groupId 用户组主键
   * @param usersId 用户主键
   */
  @PostMapping("/{groupId}")
  public void joinGroup(@PathVariable String groupId, @RequestBody Collection<String> usersId) {
    groupService.joinGroup(groupId, usersId);
  }
}
