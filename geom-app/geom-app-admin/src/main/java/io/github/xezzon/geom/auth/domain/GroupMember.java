package io.github.xezzon.geom.auth.domain;

import static io.github.xezzon.geom.auth.domain.GroupMember.GROUP_ID;
import static io.github.xezzon.geom.auth.domain.GroupMember.USER_ID;

import io.github.xezzon.geom.manager.HibernateIdGenerator;
import io.github.xezzon.tao.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用户组成员关系
 * @author xezzon
 */
@Getter
@Setter
@ToString
@Entity
@Table(
    name = "geom_group_member",
    uniqueConstraints = {@UniqueConstraint(columnNames = {GROUP_ID, USER_ID})}
)
public class GroupMember extends BaseEntity<String> {

  static final String GROUP_ID = "group_id";
  static final String USER_ID = "user_id";
  @Serial
  private static final long serialVersionUID = -6551440056829917356L;

  /**
   * 用户组成员关系主键
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
   * 用户组主键
   */
  @Column(name = GROUP_ID, nullable = false, updatable = false, length = BaseEntity.ID_LENGTH)
  private String groupId;
  /**
   * 用户主键
   */
  @Column(name = USER_ID, nullable = false, updatable = false, length = BaseEntity.ID_LENGTH)
  private String userId;

  @Override
  public GroupMember setId(String id) {
    this.id = id;
    return this;
  }
}
