package io.github.xezzon.geom.constant;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import lombok.Getter;

/**
 * @author xezzon
 */
@Factory
public class StaticConstants {

  @Getter
  private static String hashidsSalt = "hashids";

  @Value("${geom.system.hashids:hashids}")
  public void setHashidsSalt(String hashidsSalt) {
    StaticConstants.hashidsSalt = hashidsSalt;
  }
}
