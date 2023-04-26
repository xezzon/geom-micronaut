package io.github.xezzon.geom.auth.domain;

import static io.github.xezzon.geom.manager.HibernateIdGenerator.GENERATOR_NAME;
import static io.github.xezzon.geom.manager.HibernateIdGenerator.GENERATOR_STRATEGY;

import io.github.xezzon.geom.constant.DatabaseConstant;
import io.github.xezzon.geom.manager.HibernateIdGenerator;
import io.github.xezzon.tao.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用户组
 * @author xezzon
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
@Table(name = "geom_group")
public class Group extends BaseEntity<String> {

  @Serial
  private static final long serialVersionUID = 2531563636447254439L;
  /**
   * 用户组主键
   */
  @Id
  @Column(unique = true, nullable = false, updatable = false, length = BaseEntity.ID_LENGTH)
  @GenericGenerator(name = GENERATOR_NAME, strategy = GENERATOR_STRATEGY)
  @GeneratedValue(generator = HibernateIdGenerator.GENERATOR_NAME)
  private String id;
  /**
   * 用户组编码
   */
  @Column(unique = true, nullable = false)
  private String code;
  /**
   * 用户组名
   */
  @Column
  private String name;
  /**
   * 用户组所属用户主键
   */
  @Column(length = DatabaseConstant.ID_LENGTH, nullable = false)
  private String ownerId;

  @Override
  public Group setId(String id) {
    this.id = id;
    return this;
  }
}
