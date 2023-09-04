package io.github.xezzon.geom.manager;

import io.github.xezzon.tao.data.IdGenerator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * ID 生成策略
 * @author xezzon
 */
@Singleton
public class HibernateIdGenerator implements IdentifierGenerator {

  @Serial
  private static final long serialVersionUID = 8170001034547524651L;
  public static final String GENERATOR_NAME = "id-generator";

  protected final transient IdGenerator idGenerator;

  @Inject
  public HibernateIdGenerator(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  @Override
  public Serializable generate(
      SharedSessionContractImplementor sharedSessionContractImplementor,
      Object o
  ) throws HibernateException {
    Serializable originId = (Serializable) sharedSessionContractImplementor
        .getEntityPersister(null, o)
        .getIdentifier(o, sharedSessionContractImplementor);
    if (originId != null) {
      return originId;
    }
    return idGenerator.nextId();
  }
}
