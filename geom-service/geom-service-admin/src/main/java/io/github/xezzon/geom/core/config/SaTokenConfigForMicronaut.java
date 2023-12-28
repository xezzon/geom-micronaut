package io.github.xezzon.geom.core.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaTokenConfig;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import java.io.Serial;

/**
 * @author xezzon
 */
@Singleton
@Context
@ConfigurationProperties("sa-token")
public class SaTokenConfigForMicronaut extends SaTokenConfig {

  @Serial
  private static final long serialVersionUID = -5479991619346805844L;

  @PostConstruct
  public void init() {
    SaManager.setConfig(this);
  }
}
