package io.github.xezzon.geom;

import io.github.xezzon.tao.logger.EnableLogRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xezzon
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableLogRecord
public class AdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
  }
}

@RequestMapping("/actuator")
@RestController
class ActuatorController {

  @GetMapping("/health")
  public String health() {
    return "health";
  }
}
