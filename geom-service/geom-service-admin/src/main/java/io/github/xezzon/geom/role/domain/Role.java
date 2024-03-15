package io.github.xezzon.geom.role.domain;

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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 * 角色
 * @author xezzon
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "geom_role")
public class Role implements TreeNode<Role, String> {

  /**
   * 角色主键
   */
  @Id
  @Column(unique = true, nullable = false, updatable = false, length = BaseEntity.ID_LENGTH)
  @GenericGenerator(
      name = HibernateIdGenerator.GENERATOR_NAME,
      type = HibernateIdGenerator.class
  )
  @GeneratedValue(generator = HibernateIdGenerator.GENERATOR_NAME)
  String id;
  /**
   * 角色标识
   */
  @Column(unique = true, nullable = false)
  String code;
  /**
   * 角色描述
   */
  @Column(nullable = false)
  String name;
  /**
   * 可创建下级角色
   */
  @Column(nullable = false)
  private Boolean inheritable;
  /**
   * 上级角色主键
   */
  @Column(nullable = false)
  String parentId;
  /**
   * 上级角色
   */
  @Transient
  Role parent;
  /**
   * 下级角色
   */
  @Transient
  List<Role> children;
}
