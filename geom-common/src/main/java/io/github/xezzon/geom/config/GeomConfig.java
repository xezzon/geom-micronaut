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

  /**
   * ID 生成策略
   */
  protected String idGenerator;

  @Getter
  @Setter
  @ConfigurationProperties("jwt")
  public static class GeomJwtConfig {

    /**
     * JWT 签发机构
     */
    protected String issuer;
  }
}
