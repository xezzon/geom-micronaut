package io.github.xezzon.geom.role;

import io.github.xezzon.geom.role.domain.Role;
import io.github.xezzon.tao.exception.ClientException;
import jakarta.inject.Singleton;
import java.util.Objects;
import java.util.Optional;

@Singleton
public class RoleService {

  private final RoleDAO roleDAO;

  public RoleService(RoleDAO roleDAO) {
    this.roleDAO = roleDAO;
  }

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

  private void checkRepeat(Role role) {
    Optional<Role> exist = roleDAO.get().findByCode(role.getCode());
    if (exist.isPresent() && !Objects.equals(exist.get().getId(), role.getId())) {
      throw new ClientException("该角色已存在");
    }
  }
}
