package io.github.xezzon.geom.constant;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;

/**
 * @author xezzon
 */
@Factory
public class StaticConstants {

  private static String hashidsSalt = "hashids";

  @Value("${geom.system.hashids:hashids}")
  public void setHashidsSalt(String hashidsSalt) {
    StaticConstants.hashidsSalt = hashidsSalt;
  }

  public static String getHashidsSalt() {
    return hashidsSalt;
  }
}
