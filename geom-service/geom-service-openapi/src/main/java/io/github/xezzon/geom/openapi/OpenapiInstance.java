package io.github.xezzon.geom.openapi;

import static io.github.xezzon.geom.manager.HibernateIdGenerator.GENERATOR_NAME;
import static io.github.xezzon.geom.openapi.OpenapiInstance.API_ID_COLUMN;
import static io.github.xezzon.geom.openapi.OpenapiInstance.OWNER_ID_COLUMN;

import io.github.xezzon.geom.manager.HibernateIdGenerator;
import io.github.xezzon.tao.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serial;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * 订阅的接口
 * @author xezzon
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
@Table(
    name = "geom_openapi_instance",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {API_ID_COLUMN, OWNER_ID_COLUMN})
    }
)
public class OpenapiInstance extends BaseEntity<String> {

  static final String API_ID_COLUMN = "api_id";
  static final String OWNER_ID_COLUMN = "owner_id";

  @Serial
  private static final long serialVersionUID = -3829986502571840070L;

  @Id
  @Column(length = ID_LENGTH, unique = true, nullable = false, updatable = false)
  @GenericGenerator(name = GENERATOR_NAME, type = HibernateIdGenerator.class)
  @GeneratedValue(generator = GENERATOR_NAME)
  private String id;
  /**
   * 接口主键
   */
  @Column(name = API_ID_COLUMN, nullable = false)
  private String apiId;
  /**
   * 订阅者主键
   */
  @Column(name = OWNER_ID_COLUMN, nullable = false)
  private String ownerId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OpenapiInstance that)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
