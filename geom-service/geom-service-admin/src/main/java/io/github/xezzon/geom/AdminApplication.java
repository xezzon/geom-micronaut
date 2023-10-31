package io.github.xezzon.geom;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.Micronaut;

/**
 * @author xezzon
 */
public class AdminApplication {

  public static void main(String[] args) {
    Micronaut.run(AdminApplication.class, args);
  }
}

@Controller("/actuator")
class ActuatorController {

  @Get("/health")
  public String health() {
    return "health";
  }
}
