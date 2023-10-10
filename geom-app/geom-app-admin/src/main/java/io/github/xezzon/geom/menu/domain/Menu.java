package io.github.xezzon.geom.menu.domain;

import io.github.xezzon.geom.manager.HibernateIdGenerator;
import io.github.xezzon.tao.jpa.BaseEntity;
import io.github.xezzon.tao.tree.TreeNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 * 菜单
 * @author xezzon
 */
@Getter
@Setter
@ToString(exclude = "parent")
@Entity
@Table(name = "geom_menu")
public class Menu implements TreeNode<Menu, String> {

  /**
   * 菜单主键
   */
  @Id
  @Column(unique = true, nullable = false, updatable = false, length = BaseEntity.ID_LENGTH)
  @GenericGenerator(
      name = HibernateIdGenerator.GENERATOR_NAME,
      type = HibernateIdGenerator.class
  )
  @GeneratedValue(generator = HibernateIdGenerator.GENERATOR_NAME)
  private String id;
  /**
   * 菜单路径
   */
  @Column(nullable = false)
  private String path;
  /**
   * 菜单的名字
   */
  @Column(nullable = false)
  private String name;
  /**
   * 组件路径
   */
  @Column()
  private String component;
  /**
   * 图标标识
   * @see <a href="https://iconify.design/">Iconify</a>
   */
  @Column()
  private String icon;
  /**
   * 排序
   */
  @Column(nullable = false)
  private Integer ordinal;
  /**
   * 是否隐藏
   */
  @Column(nullable = false)
  private Boolean hideInMenu;
  /**
   * 上级菜单主键
   */
  @Column(nullable = false)
  private String parentId;
  /**
   * 上级菜单
   */
  @Transient
  @Getter(AccessLevel.NONE)
  private transient Menu parent;
  /**
   * 子级菜单
   */
  @Transient
  private List<Menu> children;

  /**
   * 计算全路径
   * @return 全路径
   */
  public String getFullPath() {
    String fullPath = "";
    if (this.parent != null) {
      fullPath += this.parent.getFullPath();
    }
    fullPath = fullPath + "/" + this.path;
    return fullPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Menu menu = (Menu) o;
    return Objects.equals(id, menu.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
