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
@Table(name = "geom_openapi_instance")
public class OpenapiInstance extends BaseEntity<String> {

  @Serial
  private static final long serialVersionUID = -3829986502571840070L;

  @Id
  @Column(length = ID_LENGTH, unique = true, nullable = false, updatable = false)
  @GenericGenerator(name = GENERATOR_NAME, type = HibernateIdGenerator.class)
  @GeneratedValue(generator = GENERATOR_NAME)
  private String id;
}
