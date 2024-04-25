package io.github.xezzon.geom.role;

import io.github.xezzon.geom.role.domain.Role;
import io.github.xezzon.tao.exception.ClientException;
import io.github.xezzon.tao.tree.Tree;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Singleton
public class RoleService {

  private final RoleDAO roleDAO;

  public RoleService(RoleDAO roleDAO) {
    this.roleDAO = roleDAO;
  }

  /**
   * 添加角色
   * @param role 要添加的角色
   * @throws ClientException 如果角色不存在或不能继承，则抛出此异常
   */
  protected void addRole(Role role) {
    /* 前置校验 */
    // 校验是否可以新增
    Optional<Role> parent = roleDAO.get().findById(role.getParentId());
    if (parent.isEmpty()) {
      throw new ClientException("角色已失效");
    }
    if (!parent.get().getInheritable()) {
      throw new ClientException("该角色不能继承");
    }
    // TODO: 校验当前用户是否拥有对应的角色
    // 重复性校验
    checkRepeat(role);
    /* 数据持久化 */
    roleDAO.get().save(role);
  }

  /**
   * 修改角色信息
   * @param role 要修改的角色对象
   */
  protected void modifyRole(Role role) {
    /* 前置校验 */
    // 重复性校验
    checkRepeat(role);
    /* 数据持久化 */
    roleDAO.update(role);
  }

  /**
   * 从数据库中删除指定ID的角色及其所有子角色
   * @param id 要删除的角色ID
   */
  @Transactional(rollbackFor = {Exception.class})
  protected void removeRole(String id) {
    /* 前置校验 */
    // TODO: 校验当前用户是否拥有该角色的上级（含间接上级）角色
    /* 数据持久化 */
    Optional<Role> self = roleDAO.get().findById(id);
    List<Role> children = Tree.topDown(
        Collections.singleton(id), -1, roleDAO.get()::findByParentIdIn
    );
    self.ifPresent(children::add);
    roleDAO.get().deleteAll(children);
  }

  /**
   * 检查角色是否重复
   * @param role 待检查的角色
   * @throws ClientException 如果已存在相同代码的角色且ID不相等，则抛出此异常
   */
  private void checkRepeat(Role role) {
    Optional<Role> exist = roleDAO.get().findByCode(role.getCode());
    if (exist.isPresent() && !Objects.equals(exist.get().getId(), role.getId())) {
      throw new ClientException("该角色已存在");
    }
  }
}
