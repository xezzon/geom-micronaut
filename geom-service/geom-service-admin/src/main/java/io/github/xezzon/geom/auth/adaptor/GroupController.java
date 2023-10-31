package io.github.xezzon.geom.auth.adaptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import io.github.xezzon.geom.auth.domain.Group;
import io.github.xezzon.geom.auth.domain.GroupMemberUser;
import io.github.xezzon.geom.auth.domain.query.AddGroupQuery;
import io.github.xezzon.geom.auth.service.GroupService;
import io.github.xezzon.tao.exception.ServerException;
import io.micronaut.data.model.Page;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

/**
 * @author xezzon
 */
@Controller("/user-group")
public class GroupController {

  protected final transient GroupService groupService;

  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  /**
   * 获取当前用户所在用户组集合
   * @return 用户组集合
   */
  @Get()
  public List<Group> getMyGroup() {
    return groupService.listGroupByUserId(StpUtil.getLoginId(null));
  }

  /**
   * 新增用户组
   * @param query 新增用户组请求体
   */
  @Post()
  public void addGroup(@Body AddGroupQuery query) {
    Group group = query.to();
    group.setOwnerId(StpUtil.getLoginId(null));
    groupService.addGroup(group);
  }

  /**
   * 将用户加入用户组
   * @param groupId 用户组主键
   * @param userId 用户主键
   */
  @Post("/{groupId}/member/{userId}")
  public void joinGroup(@PathVariable String groupId, @PathVariable String userId) {
    groupService.joinGroup(groupId, Collections.singleton(userId));
  }

  /**
   * 刷新用户组密钥
   * @param id 用户组主键
   * @param publicKey 非对称加密的公钥
   * @return 加密后的用户组密钥
   */
  @Patch("/{id}/secret-key")
  public String refreshSecretKey(@PathVariable String id, String publicKey) {
    if (publicKey == null) {
      throw new ServerException("缺少参数，无法生成密钥");
    }
    String secretKey = groupService.generateSecretKey(id);
    RSA rsa = new RSA(
        null, PemUtil.readPemObject(new StringReader(publicKey)).getContent()
    );
    return HexUtil.encodeHexStr(rsa.encrypt(secretKey, KeyType.PublicKey));
  }

  /**
   * 分页查询用户组成员
   * @param id 用户组主键
   * @param pageNum 页码
   * @param pageSize 页大小
   * @return 分页的用户组成员
   */
  @Get("/{id}/member")
  public Page<GroupMemberUser> listGroupMember(
      @PathVariable String id,
      @QueryValue(defaultValue = "1") int pageNum,
      @QueryValue(defaultValue = "15") short pageSize
  ) {
    return groupService.listGroupMember(id, pageNum - 1, pageSize);
  }

  /**
   * 将成员批量移出用户组
   * @param groupId 用户组主键
   * @param membersId 用户组成员主键
   */
  @Delete("/{groupId}/member")
  public void removeGroupMember(@PathVariable String groupId, @Body List<String> membersId) {
    groupService.removeMember(groupId, membersId);
  }
}
