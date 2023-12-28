package io.github.xezzon.geom.manager;

import cn.hutool.core.util.IdUtil;
import io.github.xezzon.geom.config.GeomConfig;
import io.github.xezzon.tao.data.IdGenerator;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

/**
 * @author xezzon
 */
@Singleton
@Requires(
    property = GeomConfig.GEOM + "." + GeomConfig.ID_GENERATOR,
    value = "snowflake"
)
public class SnowflakeIdGenerator implements IdGenerator {

  @Override
  public String nextId() {
    return IdUtil.getSnowflakeNextIdStr();
  }
}
