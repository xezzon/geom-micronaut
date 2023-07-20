package io.github.xezzon.geom.auth.adaptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.HexUtil;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMemberUser;
import io.github.xezzon.geom.auth.domain.query.AddGroupQuery;
import io.github.xezzon.geom.auth.service.GroupService;
import io.github.xezzon.tao.exception.ServerException;
import io.github.xezzon.tao.retrieval.CommonQuery;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
   * 获取当前用户所在用户组集合
   * @return 用户组集合
   */
  @GetMapping()
  public List<Group> getMyGroup() {
    return groupService.listGroupByUserId(StpUtil.getLoginId(null));
  }

  /**
   * 新增用户组
   * @param query 新增用户组请求体
   */
  @PostMapping()
  public void addGroup(@RequestBody AddGroupQuery query) {
    groupService.addGroup(query.to());
  }

  /**
   * 将用户加入用户组
   * @param groupId 用户组主键
   * @param userId 用户主键
   */
  @PostMapping("/{groupId}/member/{userId}")
  public void joinGroup(@PathVariable String groupId, @PathVariable String userId) {
    groupService.joinGroup(groupId, Collections.singleton(userId));
  }

  @PatchMapping("/{id}/secret-key")
  public String refreshSecretKey(@PathVariable String id, String publicKey) {
    if (publicKey == null) {
      throw new ServerException("缺少参数，无法生成密钥");
    }
    byte[] bytes = groupService.refreshSecretKey(id, publicKey);
    return HexUtil.encodeHexStr(bytes);
  }

  @GetMapping("/{id}/member")
  public Page<GroupMemberUser> listGroupMember(
      @PathVariable String id,
      @RequestParam(required = false, defaultValue = "1") int pageNum,
      @RequestParam(required = false, defaultValue = "15") int pageSize
  ) {
    return groupService.listGroupMember(id, pageNum - 1, pageSize);
  }
}
