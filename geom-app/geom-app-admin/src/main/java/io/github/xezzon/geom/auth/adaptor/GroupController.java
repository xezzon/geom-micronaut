package io.github.xezzon.geom.auth.adaptor;

import cn.hutool.core.util.HexUtil;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.service.GroupService;
import io.github.xezzon.tao.exception.ServerException;
import java.util.Collection;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xezzon
 */
@RestController
@RequestMapping("/user-group")
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

  @PatchMapping("/{id}/secret-key")
  public String refreshSecretKey(@PathVariable String id, String publicKey) {
    if (publicKey == null) {
      throw new ServerException("缺少参数，无法生成密钥");
    }
    byte[] bytes = groupService.refreshSecretKey(id, publicKey);
    return HexUtil.encodeHexStr(bytes);
  }
}
