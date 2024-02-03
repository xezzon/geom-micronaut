package io.github.xezzon.geom.openapi;

import static io.github.xezzon.geom.manager.HibernateIdGenerator.GENERATOR_NAME;

import io.github.xezzon.geom.manager.HibernateIdGenerator;
import io.github.xezzon.tao.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * 对外接口
 * @author xezzon
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
@Table(name = "geom_openapi")
public class Openapi extends BaseEntity<String> {

  @Serial
  private static final long serialVersionUID = -6568690721263775421L;

  @Id
  @Column(length = ID_LENGTH, unique = true, nullable = false, updatable = false)
  @GenericGenerator(name = GENERATOR_NAME, type = HibernateIdGenerator.class)
  @GeneratedValue(generator = GENERATOR_NAME)
  private String id;

  /**
   * 接口名称
   */
  @Column(nullable = false)
  private String name;
  /**
   * 调用路径
   */
  @Column(nullable = false)
  private String code;
  /**
   * 转发路径
   */
  @Column(nullable = false)
  private String path;
  /**
   * 发布时间
   */
  @Column()
  private Instant publishTime;

  /**
   * 当前时间大于等于发布时间则为已发布
   * @return 接口是否已发布
   */
  public boolean isPublished() {
    if (this.publishTime == null) {
      return false;
    }
    return !Instant.now().isBefore(this.publishTime);
  }
}
