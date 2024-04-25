package io.github.xezzon.geom.openapi;

import io.micronaut.data.model.Page;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

/**
 * @author xezzon
 */
@Controller("/openapi-instance")
public class OpenapiInstanceController {

  private final transient OpenapiService openapiService;

  public OpenapiInstanceController(OpenapiService openapiService) {
    this.openapiService = openapiService;
  }

  /**
   * TODO
   */
  @Get()
  public Page<OpenapiInstance> getOpenapiInstancePage() {
    return Page.empty();
  }
}
