package io.github.xezzon.geom.config;

import io.github.xezzon.geom.constant.SpringConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xezzon
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = SpringConstants.GEOM_SYSTEM)
public class GeomSystemConfig {

}
