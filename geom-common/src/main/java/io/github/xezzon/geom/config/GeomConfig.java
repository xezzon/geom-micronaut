package io.github.xezzon.geom.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xezzon
 */
@Getter
@Setter
@ConfigurationProperties(GeomConfig.GEOM)
public class GeomConfig {

  public static final String GEOM = "geom";
  public static final String ID_GENERATOR = "id-generator";

  protected String idGenerator;

  @Getter
  @Setter
  @ConfigurationProperties("jwt")
  public static class GeomJwtConfig {

    protected String issuer;
  }
}
