package io.github.xezzon.geom.openapi;

import io.micronaut.http.annotation.Controller;

/**
 * @author xezzon
 */
@Controller("/openapi")
public class OpenapiRestController {

  protected final transient OpenapiService openapiService;

  public OpenapiRestController(OpenapiService openapiService) {
    this.openapiService = openapiService;
  }
}
