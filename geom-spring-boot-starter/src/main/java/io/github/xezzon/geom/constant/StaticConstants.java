package io.github.xezzon.geom.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author xezzon
 */
@Configuration
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
