package io.github.xezzon.geom.config;

import io.github.xezzon.geom.constant.PropertyKeys;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Factory;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xezzon
 */
@Getter
@Setter
@Factory
@ConfigurationProperties(PropertyKeys.GEOM_SYSTEM)
public class GeomSystemConfig {

  protected String idGenerator;
}
